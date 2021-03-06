package com.example.demo.user_bot.service.handler.callback.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.money_range.RentalRange;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class Menu21CategoryCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu21CategoryCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final DataCache dataCache;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public Menu21CategoryCallbackHandler(UserMenuVariables userMenuVariables, DataCache dataCache, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если выбрали категорию "Арендовать квартиру"
        if (data.equals(userMenuVariables.getMenu21BtnRentalCallback())) {
            rentalCallback(response, callbackQuery, user);
        }

        // Если нажали "Купить квартиру"
        if (data.equals(userMenuVariables.getMenu21BtnBuyCallback())) {
            buyCallback(response, callbackQuery, user);
        }
    }

    private void rentalCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        Boolean isRentFlat = user.getUserChoice().getIsRentFlat();
        if (isRentFlat == null || !isRentFlat) { // Если ничего не было выбрано или выбрана покупка - ставлю галочку возле аренды
            user.getUserChoice().setIsRentFlat(true);
            user.getUserChoice().setBudget(RentalRange.selectAll());
            this.dataCache.saveUserCache(user);
        }
        // Если аренда была выбрана и мы снова нажали на аренду - ничего не делаем
        response.add(getEditMarkup(callbackQuery, user));
    }

    private void buyCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        Boolean isRentFlat = user.getUserChoice().getIsRentFlat();
        if (isRentFlat == null || isRentFlat) { // Если ничего не было выбрано или выбрана аренда - ставлю галочку возле покупки
            user.getUserChoice().setIsRentFlat(false);
            user.getUserChoice().setBudget(BuyRange.selectAll());
            this.dataCache.saveUserCache(user);
        }
        response.add(getEditMarkup(callbackQuery, user));
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getMenu21().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}
