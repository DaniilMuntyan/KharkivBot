package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu21Keyboard {
    private static final Logger LOGGER = Logger.getLogger(Menu2Keyboard.class);

    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu21Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню "изменить категорию"
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton rentBtn = new InlineKeyboardButton();
        if (userChoice.getIsRentFlat() != null) { // Если еще уже был выбор категории
            rentBtn.setText((userChoice.getIsRentFlat() ? Emoji.SELECTED + " " : "") +
                    userMenuVariables.getMenu21BtnRentalText());
        } else {
            rentBtn.setText(userMenuVariables.getMenu21BtnRentalText());
        }
        rentBtn.setCallbackData(userMenuVariables.getMenu21BtnRentalCallback());

        InlineKeyboardButton buyBtn = new InlineKeyboardButton();
        if (userChoice.getIsRentFlat() != null) {
            buyBtn.setText((!userChoice.getIsRentFlat() ? Emoji.SELECTED + " " : "") +
                    userMenuVariables.getMenu21BtnBuyText());
        } else {
            buyBtn.setText(userMenuVariables.getMenu21BtnBuyText());
        }
        buyBtn.setCallbackData(userMenuVariables.getMenu21BtnBuyCallback());

        rows.add(List.of(rentBtn));
        rows.add(List.of(buyBtn));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
