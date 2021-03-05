package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.SelectRoomsKeyboard;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.text.MessageFormat;

@Service
public class AdminMenuCallbackHandlerService {
    private final MenuVariables menuVariables;
    private final AdminService adminService;
    private final MessagesVariables messagesVariables;

    @Autowired
    public AdminMenuCallbackHandlerService(MenuVariables menuVariables, AdminService adminService, MessagesVariables messagesVariables) {
        this.menuVariables = menuVariables;
        this.adminService = adminService;
        this.messagesVariables = messagesVariables;
    }

    public BotApiMethod<?> handleAdminMenuCallback(CallbackQuery callbackQuery, User admin) {
        BotApiMethod<?> response = null;

        // Получаем айди сообщения с главным меню. Сохраняем его, чтобы не давать создавать другое
        admin.getAdminChoice().setMenuMessageId(callbackQuery.getMessage().getMessageId());
        adminService.saveAdmin(admin);

        if (callbackQuery.getData().equals(menuVariables.getAdminBtnCallbackCancel())) {
            admin.setBotState(State.INIT);
            admin.setAdminChoice(new AdminChoice());
            adminService.saveAdmin(admin);

            response = new EditMessageText();
            ((EditMessageText) response).setChatId(callbackQuery.getMessage().getChatId().toString());
            ((EditMessageText) response).setMessageId(callbackQuery.getMessage().getMessageId());
            ((EditMessageText) response).setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
        }

        if (callbackQuery.getData().equals(menuVariables.getAdminBtnCallbackRooms())) {
            response = new EditMessageText();

            SelectRoomsKeyboard selectRoomsKeyboard = new SelectRoomsKeyboard();

            ((EditMessageText) response).setChatId(callbackQuery.getMessage().getChatId().toString());
            ((EditMessageText) response).setMessageId(callbackQuery.getMessage().getMessageId());
            ((EditMessageText) response).setText(menuVariables.getAdminMenuTextSelectRoom());
            ((EditMessageText) response).setReplyMarkup(selectRoomsKeyboard.getSelectRoomsMenu());
        }

        return response;
    }
}
