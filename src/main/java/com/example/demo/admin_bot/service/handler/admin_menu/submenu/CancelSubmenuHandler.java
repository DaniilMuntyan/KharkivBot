package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class CancelSubmenuHandler {

    private final MenuVariables menuVariables;

    @Autowired
    public CancelSubmenuHandler(MenuVariables menuVariables) {
        this.menuVariables = menuVariables;
    }

    public BotApiMethod<?> handleRoomCallback(CallbackQuery callbackQuery, User admin) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        return getEditKeyboard(chatId, messageId, admin);
    }

    private EditMessageText getEditKeyboard(Long chatId, Integer messageId, User admin) {
        NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                menuVariables.getAdminAddRentFlatText() : menuVariables.getAdminAddBuyFlatText());
        return editMessageText;
    }

}
