package com.example.demo.user_bot.service;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.admin_bot.utils.AdminState;
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
import java.util.List;

@Service
public class UserBotStateService {
    private static final Logger LOGGER = Logger.getLogger(UserBotStateService.class);

    private final UserService userService;
    private final MessagesVariables messagesVariables;

    // В некоторых методах возможен Exception, поэтому не всегда надо возвращаться в предыдущее состояние
    private boolean notBack;

    @Autowired
    public UserBotStateService(UserService userService, MessagesVariables messagesVariables) {
        this.userService = userService;
        this.messagesVariables = messagesVariables;
    }

    public List<BotApiMethod<?>> processUserInput(Message message, User user) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        notBack = true;
        LOGGER.info(user.getBotUserState());
        // Если прислали любое сообщение в начальном состоянии
        if (user.getBotUserState() == UserState.USER_INIT) {
            this.processInit(answer, message, user);
        }

        userService.saveUser(user); // Сохраняем измененные параметры администратора

        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private EditMessageText getEditKeyboard(Long chatId, Integer messageId, User admin) {
        // TODO: возврат в menu2

        /*NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                adminMenuVariables.getAdminAddRentFlatText() : adminMenuVariables.getAdminAddBuyFlatText());
        this.notBack = false; // Поменяли клавиатуру - значит можем вернуться в предыдущее состояние поубликации квартиры
        return editMessageText;*/
        return null;
    }

    private void processInit(List<BotApiMethod<?>> answer, Message message, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserHi(user.getName(false)));
        sendMessage.setReplyMarkup(userService.getMenu1());

        answer.add(sendMessage);
        /*if (admin.getAdminChoice().getMenuMessageId() != null) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(admin.getAdminChoice().getMenuMessageId());
            deleteMessage.setChatId(message.getChatId().toString());
            admin.getAdminChoice().setMenuMessageId(null); // Удалили меню
            answer.add(deleteMessage);
        }

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(message.getChatId().toString());
        textResponse.setReplyMarkup(adminService.getMainMenu());
        textResponse.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
        answer.add(textResponse);

        adminService.setAdminChoice(admin, new AdminChoice());*/
    }
}