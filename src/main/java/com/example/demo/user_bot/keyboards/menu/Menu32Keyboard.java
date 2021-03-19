package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu32Keyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu32Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Квиатура для меню 3.2 (отправить номер)
    public ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton phoneBtn = new KeyboardButton();
        phoneBtn.setText(userMenuVariables.getMenu32BtnSendMyPhoneText());
        phoneBtn.setRequestContact(true);
        row1.add(phoneBtn);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(userMenuVariables.getMenu32BtnBackText()));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
