package com.example.demo.admin_bot.keyboards;

import com.example.demo.common_part.constants.AdminMenuVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public final class MainMenuKeyboard {

    private final AdminMenuVariables adminMenuVariables;

    private ReplyKeyboardMarkup mainMenu;

    @Autowired
    public MainMenuKeyboard(AdminMenuVariables adminMenuVariables) {
        this.adminMenuVariables = adminMenuVariables;
    }

    public ReplyKeyboardMarkup getMainMenu() {
        if (mainMenu != null) {
            return mainMenu;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(adminMenuVariables.getAddRentFlatBtnText()));
        row1.add(new KeyboardButton(adminMenuVariables.getAddBuyFlatBtnText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(adminMenuVariables.getDeleteRentFlat()));
        row2.add(new KeyboardButton(adminMenuVariables.getDeleteBuyFlat()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(adminMenuVariables.getBulkMessageText()));


        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);
        this.mainMenu = replyKeyboardMarkup;

        return this.mainMenu;
    }
}
