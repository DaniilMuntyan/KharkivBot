package com.example.demo.user_bot.service.handler.message;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.UserService;
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

    private final MessageHandlerRegistry messageHandlerRegistry;

    private final DataCache dataCache;

    @Autowired
    public UserBotMessageHandler(MessagesVariables messagesVariables, UserService userService, UserBotStateService userBotStateService, ProgramVariables programVariables, MessageHandlerRegistry messageHandlerRegistry, DataCache dataCache) {
        this.messagesVariables = messagesVariables;
        this.userService = userService;
        this.userBotStateService = userBotStateService;
        this.programVariables = programVariables;
        this.messageHandlerRegistry = messageHandlerRegistry;
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
        }
        LOGGER.info("Time handleUserMessage: " + (System.currentTimeMillis() - time1));

        return response;
    }

    private List<BotApiMethod<?>> handleUserMessage(Message message, UserCache user) {
        String text = message.getText().trim(); // TODO: Exception, если прислали свой номер (контакт)

        this.refreshUserName(message); // Обновляю каждый раз, когда получаю новое сообщение

        // Порядок важен! Так как в обработчиках может поменяться состояние юзера
        // и запрос может войти в следующие по порядку if-ы

        /*if (text.equals(Commands.START) && user.getBotUserState() != UserState.FIRST_INIT) {
            user.setBotUserState(UserState.INIT);
        }*/

        // Пользователь первый раз зашел в бота (/start) - меню инициализации
        if (text.equals(UserCommands.START) && user.getBotUserState() == UserState.FIRST_INIT) {
            user.setBotUserState(UserState.FIRST_INIT_CATEGORY);
        }

        return userBotStateService.processUserInput(message, user);
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
}
