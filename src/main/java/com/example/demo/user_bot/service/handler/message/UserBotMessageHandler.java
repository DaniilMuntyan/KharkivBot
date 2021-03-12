package com.example.demo.user_bot.service.handler.message;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.Commands;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.service.BotStateService;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
    private final BotStateService botStateService;

    @Autowired
    public UserBotMessageHandler(MessagesVariables messagesVariables, UserService userService, BotStateService botStateService) {
        this.messagesVariables = messagesVariables;
        this.userService = userService;
        this.botStateService = botStateService;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();

        long time1 = System.currentTimeMillis();
        Optional<User> user = userService.findByChatId(chatId);
        LOGGER.info("Time findByChatId: " + (System.currentTimeMillis() - time1));

        List<BotApiMethod<?>> response = new ArrayList<>();

        if (user.isEmpty()) { // Если пользователь новый
            User newUser = new User(message);

            time1 = System.currentTimeMillis();
            newUser = userService.saveUser(newUser);
            LOGGER.info("Time saveUser: " + (System.currentTimeMillis() - time1));

            LOGGER.info("New user: " + newUser.getName(true));

            time1 = System.currentTimeMillis();
            response.addAll(handleUserMessage(message, newUser));
            LOGGER.info("Time handleUserMessage: " + (System.currentTimeMillis() - time1));
        } else {
            time1 = System.currentTimeMillis();
            response.addAll(handleUserMessage(message, user.get()));
            LOGGER.info("Time handleUserMessage: " + (System.currentTimeMillis() - time1));

            user.get().setLastAction(new Date()); // Фиксируем последнее действие
        }

        return response;
    }

    private List<BotApiMethod<?>> handleUserMessage(Message message, User user) {
        Long chatId = message.getChatId();
        String text = message.getText().trim();
        String username = message.getFrom().getUserName(); // Обновляю каждый раз, когда получаю новое сообщение

        // Порядок важен!

        if (text.equals(Commands.START) && user.getBotUserState() != UserState.FIRST_INIT) {
            user.setBotUserState(UserState.INIT);
        }

        // Пользователь первый раз зашел в бота (/start)
        if (text.equals(Commands.START) && user.getBotUserState() == UserState.FIRST_INIT) {
            user.setBotUserState(UserState.FIRST_INIT_CATEGORY);
        }

        return botStateService.processInput(message, user, false);
    }
}
