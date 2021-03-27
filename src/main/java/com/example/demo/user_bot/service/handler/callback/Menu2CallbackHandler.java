package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.handler.callback.menu.Menu24BudgetCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.menu.Menu21CategoryCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.menu.Menu23DistrictCallbackHandler;
import com.example.demo.user_bot.service.handler.callback.menu.Menu22RoomCallbackHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class Menu2CallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu2CallbackHandler.class);

    private final UserMenuVariables userMenuVariables;

    private final DataCache dataCache;

    private final Menu22RoomCallbackHandler menu22RoomCallbackHandler;
    private final Menu21CategoryCallbackHandler menu21CategoryCallbackHandler;
    private final Menu23DistrictCallbackHandler menu23DistrictCallbackHandler;
    private final Menu24BudgetCallbackHandler menu24BudgetCallbackHandler;

    @Autowired
    public Menu2CallbackHandler(UserMenuVariables userMenuVariables, DataCache dataCache, Menu22RoomCallbackHandler menu22RoomCallbackHandler, Menu21CategoryCallbackHandler menu21CategoryCallbackHandler, Menu23DistrictCallbackHandler menu23DistrictCallbackHandler, Menu24BudgetCallbackHandler menu24BudgetCallbackHandler) {
        this.userMenuVariables = userMenuVariables;
        this.dataCache = dataCache;
        this.menu22RoomCallbackHandler = menu22RoomCallbackHandler;
        this.menu21CategoryCallbackHandler = menu21CategoryCallbackHandler;
        this.menu23DistrictCallbackHandler = menu23DistrictCallbackHandler;
        this.menu24BudgetCallbackHandler = menu24BudgetCallbackHandler;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если нажали какую-то кнопку из меню изменения категории
        if (data.startsWith(userMenuVariables.getMenu21BtnCallbackPrefix())) {
            this.menu21CategoryCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если нажали на какую-то кнопку из меню выбора комнат
        if (data.startsWith(userMenuVariables.getMenu22BtnCallbackPrefix())) {
            this.menu22RoomCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если нажали на какую-то кнопку из меню выбора районов
        if (data.startsWith(userMenuVariables.getMenu23BtnDistrictCallbackPrefix())) {
            this.menu23DistrictCallbackHandler.handleCallback(response, callbackQuery, user);
        }

        // Если нажали на какую-то кнопку из меню выбора бюджета
        if (data.startsWith(userMenuVariables.getMenu24BtnRangeCallbackPrefix())) {
            this.menu24BudgetCallbackHandler.handleCallback(response, callbackQuery, user);
        }
    }
}
