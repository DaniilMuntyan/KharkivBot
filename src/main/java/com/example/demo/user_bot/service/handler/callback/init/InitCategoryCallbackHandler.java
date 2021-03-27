package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class InitCategoryCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(InitCategoryCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public InitCategoryCallbackHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если выбрали категорию "Арендовать квартиру"
        if (data.equals(userMenuVariables.getMenuInitCategoryBtnRentalCallback())) {
            rentalCallback(response, callbackQuery, user);
        }

        // Если нажали "Купить квартиру"
        if (data.equals(userMenuVariables.getMenuInitCategoryBtnBuyCallback())) {
            buyCallback(response, callbackQuery, user);
        }

        // Если нажали "Далее"
        if (data.equals(userMenuVariables.getMenuInitBtnCategoryNextCallback())) {
            nextCallback(response, callbackQuery, user);
        }
    }

    private void rentalCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        Boolean isRentFlat = user.getUserChoice().getIsRentFlat();
        if (isRentFlat == null) { // Если ничего не было выбрано - ставлю галочку возле аренды
            user.getUserChoice().setIsRentFlat(true);
        } else {
            // Если аренда уже была выбрана - убираю галочку. Если нет - ставлю
            user.getUserChoice().setIsRentFlat(isRentFlat ? null : true);
        }

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void buyCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        Boolean isRentFlat = user.getUserChoice().getIsRentFlat();
        if (isRentFlat == null) { // Если ничего не было выбрано - ставлю галочку возле покупки
            user.getUserChoice().setIsRentFlat(false);
        } else {
            // Если покупка уже была выбрана - убираю галочку. Если нет - ставлю
            user.getUserChoice().setIsRentFlat(!isRentFlat ? null : false);
        }
        response.add(getEditMarkup(callbackQuery, user));
    }

    private void nextCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        user.setBotUserState(UserState.FIRST_INIT_ROOMS); // Ставлю следующее состояние

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getUserInitRoomsText());
        editMessageText.setReplyMarkup(keyboardsRegistry.getInitRoomsMenu().getKeyboard(user.getUserChoice()));

        response.add(editMessageText);
    }


    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getInitCategoryMenu().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}