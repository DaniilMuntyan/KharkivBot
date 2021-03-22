package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.keyboards.delete.EnterIdKeyboard;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public final class CommonMethods {
    private final AdminMenuVariables adminMenuVariables;

    @Autowired
    public CommonMethods(AdminMenuVariables adminMenuVariables) {
        this.adminMenuVariables = adminMenuVariables;
    }

    public EditMessageText getEditNewFlatKeyboard(Long chatId, Integer messageId, UserCache admin) {
        NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                adminMenuVariables.getAdminAddRentFlatText() : adminMenuVariables.getAdminAddBuyFlatText());
        return editMessageText;
    }
}
