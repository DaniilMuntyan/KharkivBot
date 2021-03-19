package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu2Keyboard {
    private static final Logger LOGGER = Logger.getLogger(Menu2Keyboard.class);

    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu2Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню "мои предпочтения"
    public ReplyKeyboardMarkup getKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(userMenuVariables.getMenu2BtnCategoryText()));
        row1.add(new KeyboardButton(userMenuVariables.getMenu2BtnRoomsText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(userMenuVariables.getMenu2BtnDistrictsText()));
        row2.add(new KeyboardButton(userMenuVariables.getMenu2BtnBudgetText()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(userMenuVariables.getMenu2BtnBackText()));
        row3.add(new KeyboardButton(userMenuVariables.getMenu2BtnSearchText()));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
