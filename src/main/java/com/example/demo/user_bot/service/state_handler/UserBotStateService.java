package com.example.demo.user_bot.service.state_handler;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBotStateService {
    private static final Logger LOGGER = Logger.getLogger(UserBotStateService.class);

    private final UserService userService;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public UserBotStateService(UserService userService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public List<BotApiMethod<?>> processUserInput(Message message, UserCache user) {
        long time1 = System.currentTimeMillis();
        List<BotApiMethod<?>> answer = new ArrayList<>();

        if (user.getBotUserState() == UserState.FIRST_INIT_CATEGORY) {
            this.processCategory(answer, message, user);
        }

        // Если прислали любое сообщение в начальном состоянии
        if (user.getBotUserState() == UserState.INIT) {
            this.processInit(answer, message, user);
        }

        userService.saveUserCache(user); // Сохраняем измененные параметры администратора
        LOGGER.info("TIME processUserInput: " + (System.currentTimeMillis() - time1));
        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private void processCategory(List<BotApiMethod<?>> answer, Message message, UserCache user) {
        SendMessage messageHi = new SendMessage(); // Приветственное сообщение
        messageHi.setChatId(message.getChatId().toString());
        messageHi.setText(messagesVariables.getUserFirstHi(user.getName(false)));

        SendMessage messageCategory = new SendMessage(); // Сообщение с меню инициализации №1 (категории)
        messageCategory.setChatId(message.getChatId().toString());
        messageCategory.setText(messagesVariables.getUserInitCategoryText());
        messageCategory.setReplyMarkup(keyboardsRegistry.getInitCategoryMenu().getKeyboard(user.getUserChoice()));

        answer.addAll(List.of(messageHi, messageCategory));
        user.setBotUserState(UserState.FIRST_INIT_CATEGORY); // Меняю состояние бота, теперь выбираем категорию
    }

    private void processInit(List<BotApiMethod<?>> answer, Message message, UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserHi(user.getName(false)));
        sendMessage.setReplyMarkup(keyboardsRegistry.getInitCategoryMenu().getKeyboard(user.getUserChoice()));

        answer.add(sendMessage);
    }
}