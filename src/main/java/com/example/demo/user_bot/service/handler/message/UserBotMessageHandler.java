package com.example.demo.user_bot.service.handler.message;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.service.RefreshUserDataService;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.DeleteMessageService;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.message.menu.BackToMenu1;
import com.example.demo.user_bot.service.handler.callback.see_others.ShowOrEnoughService;
import com.example.demo.user_bot.service.state_handler.UserBotStateService;
import com.example.demo.user_bot.utils.UserCommands;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public final class UserBotMessageHandler {
    private static final Logger LOGGER = Logger.getLogger(UserBotMessageHandler.class);

    private final MessagesVariables messagesVariables;
    private final UserService userService;
    private final UserBotStateService userBotStateService;
    private final ProgramVariables programVariables;
    private final ShowOrEnoughService showOrEnoughService;
    private final RefreshUserDataService refreshUserDataService;
    private final DeleteMessageService deleteMessageService;

    private final BackToMenu1 backToMenu1;

    private final DataCache dataCache;

    @Autowired
    public UserBotMessageHandler(MessagesVariables messagesVariables, UserService userService, UserBotStateService userBotStateService, ProgramVariables programVariables, ShowOrEnoughService showOrEnoughService, RefreshUserDataService refreshUserDataService, DeleteMessageService deleteMessageService, BackToMenu1 backToMenu1, DataCache dataCache) {
        this.messagesVariables = messagesVariables;
        this.userService = userService;
        this.userBotStateService = userBotStateService;
        this.programVariables = programVariables;
        this.showOrEnoughService = showOrEnoughService;
        this.refreshUserDataService = refreshUserDataService;
        this.deleteMessageService = deleteMessageService;
        this.backToMenu1 = backToMenu1;
        this.dataCache = dataCache;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();
        long time1 = System.currentTimeMillis();

        // Достаю юзера из базы или кэша
        Optional<UserCache> user = userService.findUserInCacheOrDb(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        if (user.isEmpty()) { // Если пользователь новый
            User newUser = new User(message);
            UserCache newUserCache = userService.saveNewUser(newUser);
            response.addAll(handleUserMessage(message, newUserCache));
        } else {
            // Если написали во время подбора квартир - игнор
            if (user.get().getBotUserState() == UserState.FLATS_MASSAGING) {
                return response;
            }
            long timeFromLastMsg = time1 - user.get().lastMessage();
            if (timeFromLastMsg < programVariables.getDelayUserSpam()) { // Если сообщения идут слишком часто
                if (!user.get().getSpam()) { // Если пользователь не был в спаме - отправляю в спам
                    response.add(this.antiSpam(user.get()));
                    user.get().setSpam(true); // Отправляю в спам
                }
                // Если пользователь уже в спаме - ничего не делаем (ждем пока сделает паузу)
            } else {
                user.get().setSpam(false); // Убираю со спама
                user.get().setLastAction(new Date()); // Фиксируем последнее действие
                response.addAll(handleUserMessage(message, user.get()));
            }
            user.get().setLastMessage(time1); // Запоминаем время последнего сообщения
        }

        return response;
    }

    private List<BotApiMethod<?>> handleUserMessage(Message message, UserCache user) {
        this.refreshUserDataService.refreshUserName(message); // Обновляю каждый раз, когда получаю новое сообщение

        boolean hasText = message.hasText();

        List<BotApiMethod<?>> answer = new ArrayList<>();

        // Если прислали сообщение в состоянии SENT_NOT_ALL
        if (user.getBotUserState() == UserState.SENT_NOT_ALL) {
            this.showOrEnoughService.enough(answer, user.getUserChoice().getMenuMessageId(), user); // Логика такая же, если б нажали "Достаточно"
        } else { // Если не "Назад"
            if (hasText) { // Если прислали текст
                String text = message.getText().trim();

                // Порядок важен! Так как в обработчиках может поменяться состояние юзера
                // и запрос может войти в следующие по порядку if-ы

                // Пользователь первый раз зашел в бота (/start) - меню инициализации
                if (text.equals(UserCommands.START) && user.getBotUserState() == UserState.FIRST_INIT) {
                    user.setBotUserState(UserState.FIRST_INIT_CATEGORY);
                    this.dataCache.saveUserCache(user);
                    //this.dataCache.markNotSaved(user.getChatId());
                } else {
                    boolean initMenu = UserState.getFirstStates().contains(user.getBotUserState());
                    // Если пользователь прислал /start во время меню инициализации
                    if (text.equals(UserCommands.START) && initMenu) {
                        user.setBotUserState(UserState.FIRST_INIT_CATEGORY); // Снова показываю категорию
                        if (user.getUserChoice().getMenuMessageId() != null) { // Если меню есть - удаляю его, чтобы создать новое
                            answer.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
                        }
                        user.getUserChoice().setMenuMessageId(null); // Удаляю прошлое меню
                        this.dataCache.saveUserCache(user);
                    } else {
                        if (text.equals(UserCommands.START)) { // Если пользователь прислал /start в любом другом состоянии
                            if (user.getUserChoice().getMenuMessageId() != null) { // Если меню есть - удаляю его
                                answer.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
                            }
                            return List.of(this.backToMenu1.back(user)); // Возвращаемся в главное меню
                        }
                        if (text.equals(UserCommands.HELP)) { // Если пользователь прислал /help
                            if (user.getUserChoice().getMenuMessageId() != null) { // Если меню есть - удаляю его
                                answer.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
                            }
                            return List.of(this.backToMenu1.help(user)); // Возвращаемся в главное меню
                        }
                        if (initMenu) { // Если пользователь прислал "левое" сообщение во время меню инициализации
                            // Удаляю левое сообщение
                            return List.of(this.deleteMessageService.deleteApiMethod(user.getChatId(), message.getMessageId()));
                        }
                    }
                }
            }
            answer.addAll(userBotStateService.processUserInput(message, user));
        }
        return answer;
    }

    private SendMessage antiSpam(UserCache userCache) {
        SendMessage antiSpam = new SendMessage();
        antiSpam.setChatId(userCache.getChatId().toString());
        antiSpam.setText(messagesVariables.getUserAntiSpamText());
        return antiSpam;
    }
}
