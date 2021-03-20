package com.example.demo.user_bot.service.handler.message;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.message.menu.BackToMenu1;
import com.example.demo.user_bot.service.state_handler.UserBotStateService;
import com.example.demo.user_bot.utils.UserCommands;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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

    private final BackToMenu1 backToMenu1;

    private final DataCache dataCache;

    @Autowired
    public UserBotMessageHandler(MessagesVariables messagesVariables, UserService userService, UserBotStateService userBotStateService, ProgramVariables programVariables, BackToMenu1 backToMenu1, DataCache dataCache) {
        this.messagesVariables = messagesVariables;
        this.userService = userService;
        this.userBotStateService = userBotStateService;
        this.programVariables = programVariables;
        this.backToMenu1 = backToMenu1;
        this.dataCache = dataCache;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();

        long time1 = System.currentTimeMillis();
        Optional<UserCache> user = userService.findUserInCache(chatId);
        LOGGER.info("Time findByChatId: " + (System.currentTimeMillis() - time1));

        List<BotApiMethod<?>> response = new ArrayList<>();

        if (user.isEmpty()) { // Если пользователь новый
            User newUser = new User(message);

            time1 = System.currentTimeMillis();
            UserCache newUserCache = userService.saveNewUser(newUser);
            LOGGER.info("Time saveUser in cache: " + (System.currentTimeMillis() - time1));

            LOGGER.info("New user: " + newUser.getName(true));

            time1 = System.currentTimeMillis();
            response.addAll(handleUserMessage(message, newUserCache));
        } else {
            // Если написали во время подбора квартир - игнор
            if (user.get().getBotUserState() == UserState.FLATS_MASSAGING) {
                return response;
            }
            LOGGER.info("Time from last message: " + (time1 - user.get().lastMessage()));
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
            //this.dataCache.markNotSaved(chatId);
        }
        LOGGER.info("Time handleUserMessage: " + (System.currentTimeMillis() - time1));

        return response;
    }

    private List<BotApiMethod<?>> handleUserMessage(Message message, UserCache user) {
        this.refreshUserName(message); // Обновляю каждый раз, когда получаю новое сообщение

        boolean hasText = message.hasText();

        List<BotApiMethod<?>> answer = new ArrayList<>();

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
                        answer.add(this.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
                    }
                    user.getUserChoice().setMenuMessageId(null); // Удаляю прошлое меню
                    this.dataCache.saveUserCache(user);
                    //this.dataCache.markNotSaved(user.getChatId());
                } else {
                    if (text.equals(UserCommands.START)) { // Если пользователь прислал /start
                        if (user.getUserChoice().getMenuMessageId() != null) { // Если меню есть - удаляю его
                            answer.add(this.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
                        }
                        return List.of(this.backToMenu1.back(user)); // Возвращаемся в главное меню
                    }
                    if (initMenu) { // Если пользователь прислал "левое" сообщение во время меню инициализации
                        return List.of(this.deleteApiMethod(user.getChatId(), message.getMessageId())); // Удаляю левое сообщение
                    }
                }
            }
        }

        answer.addAll(userBotStateService.processUserInput(message, user));
        return answer;
    }

    private void refreshUserName(Message message) {
        String username = message.getFrom().getUserName();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        this.dataCache.refreshUserName(message.getChatId(), username, firstName, lastName); // Обновляю имена пользователя
    }

    private SendMessage antiSpam(UserCache userCache) {
        SendMessage antiSpam = new SendMessage();
        antiSpam.setChatId(userCache.getChatId().toString());
        antiSpam.setText(messagesVariables.getUserAntiSpamText());
        return antiSpam;
    }

    private DeleteMessage deleteApiMethod(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }
}
