package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.submenu.SelectRoomsKeyboard;
import com.example.demo.admin_bot.keyboards.submenu.SquareKeyboard;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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

    public List<BotApiMethod<?>> handleAdminMenuCallback(CallbackQuery callbackQuery, User admin) {
        List<BotApiMethod<?>> response = new ArrayList<>();


        // Получаем айди сообщения с главным меню, если он отличается от сраненного.
        // Сохраняем его, чтобы не давать создавать другое меню.
        // Потому что в один момент времени может обрабатываться только одно меню.
        if (!callbackQuery.getMessage().getMessageId().equals(admin.getAdminChoice().getMenuMessageId())) {
            admin.getAdminChoice().setMenuMessageId(callbackQuery.getMessage().getMessageId());
            adminService.saveAdmin(admin);
        }


        // Отмена публикации квартиры
        if (callbackQuery.getData().equals(menuVariables.getAdminBtnCallbackCancel())) {
            admin.setBotState(State.INIT);
            admin.setAdminChoice(new AdminChoice());
            adminService.saveAdmin(admin);

            // Удаляю меню
            response.add(deleteApiMethod(callbackQuery.getMessage()));
            // Посылаю сообщение
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
            sendMessage.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
            sendMessage.setReplyMarkup(adminService.getMainMenu());
            response.add(sendMessage);
        }

        if (callbackQuery.getData().equals(menuVariables.getAdminBtnCallbackRooms())) {
            EditMessageText editMessageText = new EditMessageText();

            SelectRoomsKeyboard selectRoomsKeyboard = new SelectRoomsKeyboard();

            editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(menuVariables.getAdminMenuTextSelectRoom());
            editMessageText.setReplyMarkup(selectRoomsKeyboard.getSelectRoomsMenu());
            response.add(editMessageText);
        }

        if (callbackQuery.getData().equals(menuVariables.getAdminBtnCallbackSquare())) {
            EditMessageText editMessageText = new EditMessageText();

            SquareKeyboard squareKeyboard = new SquareKeyboard();

            editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(messagesVariables.getSquareMessageText());
            editMessageText.setReplyMarkup(squareKeyboard.getKeyboard());
            response.add(editMessageText);

            admin.setBotState(State.SUBMENU_SQUARE);
            adminService.saveAdmin(admin);
        }

        return response;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }
}
