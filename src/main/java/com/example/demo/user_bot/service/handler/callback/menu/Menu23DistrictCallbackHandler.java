package com.example.demo.user_bot.service.handler.callback.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.District;
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
public final class Menu23DistrictCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu23DistrictCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;

    private final DataCache dataCache;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public Menu23DistrictCallbackHandler(UserMenuVariables userMenuVariables, DataCache dataCache, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();
        // Ищу какой район выбрали
        for(District temp: District.values()) {
            if (data.equals(userMenuVariables.getMenu23BtnDistrictCallbackPrefix() + temp.getIdentifier())) {
                long time1 = System.currentTimeMillis();
                districtCallback(temp, response, callbackQuery, user);
                LOGGER.info("TIME districtCallback: " + (System.currentTimeMillis() - time1));
                break;
            }
        }

        // Если нажали кнопку "Выбрать все"
        if (data.equals(userMenuVariables.getMenu23BtnSelectAllCallback())) {
            long time1 = System.currentTimeMillis();
            selectAllCallback(response, callbackQuery, user);
            LOGGER.info("TIME selectAllCallback: " + (System.currentTimeMillis() - time1));
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

        this.dataCache.saveUserCache(user);
        //this.dataCache.markNotSaved(user.getChatId());
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
            user.getUserChoice().setDistricts(""); // Снимаю выбор со всех
        } else {
            StringBuilder allDistrictChoice = new StringBuilder();
            for (District temp: District.values()) {
                allDistrictChoice.append(temp.getIdentifier());
            }
            user.getUserChoice().setDistricts(allDistrictChoice.toString());
        }

        this.dataCache.saveUserCache(user);
        //this.dataCache.markNotSaved(user.getChatId());
        response.add(getEditMarkup(callbackQuery, user));
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getMenu23().getKeyboard((user.getUserChoice())));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}
