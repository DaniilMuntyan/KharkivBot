package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.entities.UserService;
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
public final class InitDistrictCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(InitDistrictCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final UserService userService;
    private final MessagesVariables messagesVariables;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public InitDistrictCallbackHandler(UserMenuVariables userMenuVariables, UserService userService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();
        // Ищем, какой район выбрали
        for(District temp: District.values()) {
            if (data.equals(userMenuVariables.getMenuInitDistrictsBtnPrefixCallback() + temp.getIdentifier())) {
                long time1 = System.currentTimeMillis();
                districtCallback(temp, response, callbackQuery, user);
                LOGGER.info("TIME districtCallback: " + (System.currentTimeMillis() - time1));
                break;
            }
        }

        // Если нажали кнопку "Выбрать все"
        if (data.equals(userMenuVariables.getMenuInitDistrictBtnSelectAllCallback())) {
            long time1 = System.currentTimeMillis();
            selectAllCallback(response, callbackQuery, user);
            LOGGER.info("TIME selectAllCallback: " + (System.currentTimeMillis() - time1));
        }

        // Если нажали кнопку "Дальше"
        if (data.equals(userMenuVariables.getMenuInitDistrictNextCallback())) {
            long time1 = System.currentTimeMillis();
            nextCallback(response, callbackQuery, user);
            LOGGER.info("TIME nextCallback: " + (System.currentTimeMillis() - time1));
        }
    }

    private void districtCallback(District district, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String districtChoice = user.getUserChoice().getDistricts();
        boolean isDistrictChecked = districtChoice.contains(district.getIdentifier());

        if (isDistrictChecked) { // Если район был выбран - снимаем галочку
            user.getUserChoice().setDistricts(districtChoice.replace(district.getIdentifier(), ""));
        } else { // Если нет - добавляем выбор
            user.getUserChoice().setDistricts(districtChoice + district.getIdentifier());
        }

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void selectAllCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String districtChoice = user.getUserChoice().getDistricts();
        boolean isAllSelected = true; // Если все районы уже выбраны
        for (District temp: District.values()) {
            if (!districtChoice.contains(temp.getIdentifier())) {
                isAllSelected = false;
                break;
            }
        }
        if (isAllSelected) {
            user.getUserChoice().setDistricts(""); // Снимаем выбор со всех
        } else {
            StringBuilder allDistrictChoice = new StringBuilder();
            for (District temp: District.values()) {
                allDistrictChoice.append(temp.getIdentifier());
            }
            user.getUserChoice().setDistricts(allDistrictChoice.toString());
        }

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void nextCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        user.setBotUserState(UserState.FIRST_INIT_BUDGET); // Ставлю следующее состояние

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getUserInitBudgetText());
        editMessageText.setReplyMarkup(keyboardsRegistry.getInitBudgetKeyboard().getKeyboard(user.getUserChoice()));

        response.add(editMessageText);
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getInitDistrictsKeyboard().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}