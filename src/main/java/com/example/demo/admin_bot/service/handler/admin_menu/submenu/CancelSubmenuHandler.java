package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class CancelSubmenuHandler {

    private final CommonMethods commonMethods;
    private final DataCache dataCache;

    @Autowired
    public CancelSubmenuHandler(CommonMethods commonMethods, DataCache dataCache) {
        this.commonMethods = commonMethods;
        this.dataCache = dataCache;
    }

    public BotApiMethod<?> handleCancelSubmenuCallback(CallbackQuery callbackQuery, UserCache admin) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        // В зависимости от категории квартиры - возвращаюсь в главное меню (добавление квартиры)
        if (admin.getAdminChoice() != null) {
            admin.setBotAdminState(admin.getAdminChoice().getIsRentFlat() ? AdminState.ADMIN_ADD_RENT_FLAT :
                    AdminState.ADMIN_ADD_BUY_FLAT);
            this.dataCache.saveUserCache(admin);
        }

        return commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin);
    }
}
