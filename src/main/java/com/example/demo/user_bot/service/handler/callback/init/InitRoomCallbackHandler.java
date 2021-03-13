package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class InitRoomCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(InitRoomCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final UserService userService;
    private final MessagesVariables messagesVariables;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public InitRoomCallbackHandler(UserMenuVariables userMenuVariables, UserService userService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если выбрали Гостинку
        if (data.equals(userMenuVariables.getMenuInitRoomsBtnRoom0Callback())) {
            long time1 = System.currentTimeMillis();
            room0Callback(response, callbackQuery, user);
            LOGGER.info("TIME room0Callback: " + (System.currentTimeMillis() - time1));
        }

        // Если выбрали 1 комнату
        if (data.equals(userMenuVariables.getMenuInitRoomsBtnRoom1Callback())) {
            long time1 = System.currentTimeMillis();
            room1Callback(response, callbackQuery, user);
            LOGGER.info("TIME room1Callback: " + (System.currentTimeMillis() - time1));
        }

        // Если выбрали 2 комнаты
        if (data.equals(userMenuVariables.getMenuInitRoomsBtnRoom2Callback())) {
            long time1 = System.currentTimeMillis();
            room2Callback(response, callbackQuery, user);
            LOGGER.info("TIME room2Callback: " + (System.currentTimeMillis() - time1));
        }

        // Если выбрали 3 комнаты
        if (data.equals(userMenuVariables.getMenuInitRoomsBtnRoom3Callback())) {
            long time1 = System.currentTimeMillis();
            room3Callback(response, callbackQuery, user);
            LOGGER.info("TIME room3Callback: " + (System.currentTimeMillis() - time1));
        }

        // Если выбрали 4 комнаты
        if (data.equals(userMenuVariables.getMenuInitRoomsBtnRoom4Callback())) {
            long time1 = System.currentTimeMillis();
            room4Callback(response, callbackQuery, user);
            LOGGER.info("TIME room4Callback: " + (System.currentTimeMillis() - time1));
        }

        // Если нажали кнопку "Дальше"
        if (data.equals(userMenuVariables.getMenuInitBtnRoomNextCallback())) {
            long time1 = System.currentTimeMillis();
            nextCallback(response, callbackQuery, user);
            LOGGER.info("TIME nextCallback: " + (System.currentTimeMillis() - time1));
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

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void nextCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        user.setBotUserState(UserState.FIRST_INIT_DISTRICTS); // Ставлю следующее состояние

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getUserInitDistrictsText());
        editMessageText.setReplyMarkup(keyboardsRegistry.getInitDistrictsKeyboard().getKeyboard(user.getUserChoice()));

        response.add(editMessageText);
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getInitRoomsMenu().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}