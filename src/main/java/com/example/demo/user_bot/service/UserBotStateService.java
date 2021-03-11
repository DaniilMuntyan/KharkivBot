package com.example.demo.user_bot.service;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserBotStateService {
    private static final Logger LOGGER = Logger.getLogger(UserBotStateService.class);

    private final UserService userService;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistryService keyboardsRegistryService;

    // В некоторых методах возможен Exception, поэтому не всегда надо возвращаться в предыдущее состояние
    private boolean notBack;

    @Autowired
    public UserBotStateService(UserService userService, MessagesVariables messagesVariables, KeyboardsRegistryService keyboardsRegistryService) {
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistryService = keyboardsRegistryService;
    }

    public List<BotApiMethod<?>> processUserInput(Message message, User user) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        notBack = true;

        // Если юзер зашел первый раз
        if (user.getBotUserState() == UserState.FIRST_INIT_CATEGORY) {
            this.processFirstInitCategory(answer, message, user);
        }

        // Если прислали любое сообщение в начальном состоянии
        if (user.getBotUserState() == UserState.INIT) {
            this.processInit(answer, message, user);
        }

        user.setLastAction(new Date()); // Фиксируем последнее действие
        userService.saveUser(user); // Сохраняем измененные параметры администратора

        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private void processInit(List<BotApiMethod<?>> answer, Message message, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserHi(user.getName(false)));
        sendMessage.setReplyMarkup(keyboardsRegistryService.getInitCategoryMenu().getKeyboard());

        answer.add(sendMessage);
    }

    private void processFirstInitCategory(List<BotApiMethod<?>> answer, Message message, User user) {
        SendMessage messageHi = new SendMessage();
        messageHi.setChatId(message.getChatId().toString());
        messageHi.setText(messagesVariables.getUserFirstHi(user.getName(false)));

        SendMessage chooseCategory = new SendMessage();
        chooseCategory.setChatId(message.getChatId().toString());
        chooseCategory.setText(messagesVariables.getUserInitCategoryText());
        chooseCategory.setReplyMarkup(keyboardsRegistryService.getInitCategoryMenu().getKeyboard());

        answer.addAll(List.of(messageHi, chooseCategory));
    }
}