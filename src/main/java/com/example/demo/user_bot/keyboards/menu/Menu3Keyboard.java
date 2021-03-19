package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu3Keyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu3Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Квиатура для меню №3 (настройки)
    public ReplyKeyboardMarkup getKeyboard(UserCache user) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        // Если уведомления были включены - выключить, если наоборот - включить
        row1.add(new KeyboardButton(user.getIsWantsUpdates() ? userMenuVariables.getMenu3BtnStopMailingText() :
                userMenuVariables.getMenu3BtnStartMailingText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(userMenuVariables.getMenu3BtnEnterPhoneText()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(userMenuVariables.getMenu3BtnBackText()));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
