package com.example.demo.user_bot.service.handler.callback.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.Rooms;
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
public final class Menu22RoomCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu22RoomCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;

    private final DataCache dataCache;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public Menu22RoomCallbackHandler(UserMenuVariables userMenuVariables, DataCache dataCache, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если выбрали Гостинку
        if (data.equals(userMenuVariables.getMenu22BtnRoom0Callback())) {
            room0Callback(response, callbackQuery, user);
        }

        // Если выбрали 1 комнату
        if (data.equals(userMenuVariables.getMenu22BtnRoom1Callback())) {
            room1Callback(response, callbackQuery, user);
        }

        // Если выбрали 2 комнаты
        if (data.equals(userMenuVariables.getMenu22BtnRoom2Callback())) {
            room2Callback(response, callbackQuery, user);
        }

        // Если выбрали 3 комнаты
        if (data.equals(userMenuVariables.getMenu22BtnRoom3Callback())) {
            room3Callback(response, callbackQuery, user);
        }

        // Если выбрали 4 комнаты
        if (data.equals(userMenuVariables.getMenu22BtnRoom4Callback())) {
            room4Callback(response, callbackQuery, user);
        }
    }

    private void room0Callback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Перемненная isRoom0Checked - Выбран ли уже вариант "Гостинка"
        String roomChoice = user.getUserChoice().getRooms();
        boolean isRoom0Checked = roomChoice.contains(Rooms.GOSTINKA.getIdentifier());
        if (isRoom0Checked) { // Если вариант был выбран - снимаем галочку выбора
            user.getUserChoice().setRooms(roomChoice.replace(Rooms.GOSTINKA.getIdentifier(), ""));
        } else { // Если вариант не был выбран - ставим галочку
            user.getUserChoice().setRooms(roomChoice + Rooms.GOSTINKA.getIdentifier());
        }

        this.dataCache.saveUserCache(user);

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void room1Callback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Перемненная isRoom1Checked - Выбран ли уже вариант "1"
        String roomChoice = user.getUserChoice().getRooms();
        boolean isRoom1Checked = roomChoice.contains(Rooms.ONE.getIdentifier());
        if (isRoom1Checked) { // Если вариант был выбран - снимаем галочку выбора
            user.getUserChoice().setRooms(roomChoice.replace(Rooms.ONE.getIdentifier(), ""));
        } else { // Если вариант не был выбран - ставим галочку
            user.getUserChoice().setRooms(roomChoice + Rooms.ONE.getIdentifier());
        }

        this.dataCache.saveUserCache(user);

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void room2Callback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Перемненная isRoom2Checked - Выбран ли уже вариант "2"
        String roomChoice = user.getUserChoice().getRooms();
        boolean isRoom2Checked = roomChoice.contains(Rooms.TWO.getIdentifier());
        if (isRoom2Checked) { // Если вариант был выбран - снимаем галочку выбора
            user.getUserChoice().setRooms(roomChoice.replace(Rooms.TWO.getIdentifier(), ""));
        } else { // Если вариант не был выбран - ставим галочку
            user.getUserChoice().setRooms(roomChoice + Rooms.TWO.getIdentifier());
        }

        this.dataCache.saveUserCache(user);

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void room3Callback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Перемненная isRoom3Checked - Выбран ли уже вариант "3"
        String roomChoice = user.getUserChoice().getRooms();
        boolean isRoom3Checked = roomChoice.contains(Rooms.THREE.getIdentifier());
        if (isRoom3Checked) { // Если вариант был выбран - снимаем галочку выбора
            user.getUserChoice().setRooms(roomChoice.replace(Rooms.THREE.getIdentifier(), ""));
        } else { // Если вариант не был выбран - ставим галочку
            user.getUserChoice().setRooms(roomChoice + Rooms.THREE.getIdentifier());
        }

        this.dataCache.saveUserCache(user);

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void room4Callback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Перемненная isRoom4Checked - Выбран ли уже вариант "4"
        String roomChoice = user.getUserChoice().getRooms();
        boolean isRoom4Checked = roomChoice.contains(Rooms.FOUR.getIdentifier());
        if (isRoom4Checked) { // Если вариант был выбран - снимаем галочку выбора
            user.getUserChoice().setRooms(roomChoice.replace(Rooms.FOUR.getIdentifier(), ""));
        } else { // Если вариант не был выбран - ставим галочку
            user.getUserChoice().setRooms(roomChoice + Rooms.FOUR.getIdentifier());
        }

        this.dataCache.saveUserCache(user);

        response.add(getEditMarkup(callbackQuery, user));
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getMenu22().getKeyboard((user.getUserChoice())));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());

        return editKeyboard;
    }
}
