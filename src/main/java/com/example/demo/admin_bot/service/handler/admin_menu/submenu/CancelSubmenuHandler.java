package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.common_part.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class CancelSubmenuHandler {

    private final CommonMethods commonMethods;

    @Autowired
    public CancelSubmenuHandler(CommonMethods commonMethods) {
        this.commonMethods = commonMethods;
    }

    public BotApiMethod<?> handleCancelSubmenuCallback(CallbackQuery callbackQuery, User admin) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin);
    }
}
