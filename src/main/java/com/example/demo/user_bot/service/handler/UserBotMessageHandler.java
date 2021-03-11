package com.example.demo.user_bot.service.handler;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.Commands;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.common_part.service.BotStateService;
import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.user_bot.service.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public final class UserBotMessageHandler {
    private static final Logger LOGGER = Logger.getLogger(UserBotMessageHandler.class);

    private final UserRepository userRepository;
    private final MessagesVariables messagesVariables;
    private final UserService userService;
    private final BotStateService botStateService;

    @Autowired
    public UserBotMessageHandler(UserRepository userRepository, MessagesVariables messagesVariables, UserService userService, BotStateService botStateService) {
        this.userRepository = userRepository;
        this.messagesVariables = messagesVariables;
        this.userService = userService;
        this.botStateService = botStateService;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();
        Optional<User> user = userRepository.findByChatId(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        if (user.isEmpty()) { // Если пользователь новый
            User newUser = new User(message);
            newUser = userRepository.save(newUser);
            LOGGER.info("New user: " + newUser.getName(true));
            response.addAll(handleUserMessage(message, newUser));
        } else {
            response.addAll(handleUserMessage(message, user.get()));
        }

        return response;
    }

    private List<BotApiMethod<?>> handleUserMessage(Message message, User user) {
        Long chatId = message.getChatId();
        String text = message.getText().trim();
        String username = message.getFrom().getUserName(); // Обновляю каждый раз, когда получаю новое сообщение

        if(text.equals(Commands.START)) {
            user.setBotUserState(UserState.USER_INIT);
        }

        return botStateService.processInput(message, user, false);
    }
}
