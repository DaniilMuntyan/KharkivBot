package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.common_part.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public final class CommonMethods {
    private final MenuVariables menuVariables;

    @Autowired
    public CommonMethods(MenuVariables menuVariables) {
        this.menuVariables = menuVariables;
    }

    public EditMessageText getEditNewFlatKeyboard(Long chatId, Integer messageId, User admin) {
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
