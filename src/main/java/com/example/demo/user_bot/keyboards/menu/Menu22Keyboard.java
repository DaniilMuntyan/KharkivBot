package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.model.UserChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu22Keyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu22Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню 2.2 (выбрать количество комнат)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String roomChoice = userChoice.getRooms();

        InlineKeyboardButton buttonZero = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.GOSTINKA.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenu22BtnRoom0Text())
                .callbackData(userMenuVariables.getMenu22BtnRoom0Callback())
                .build();
        InlineKeyboardButton buttonOne = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.ONE.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenu22BtnRoom1Text())
                .callbackData(userMenuVariables.getMenu22BtnRoom1Callback())
                .build();
        InlineKeyboardButton buttonTwo = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.TWO.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenu22BtnRoom2Text())
                .callbackData(userMenuVariables.getMenu22BtnRoom2Callback())
                .build();
        InlineKeyboardButton buttonThree = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.THREE.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenu22BtnRoom3Text())
                .callbackData(userMenuVariables.getMenu22BtnRoom3Callback())
                .build();
        InlineKeyboardButton buttonFour = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.FOUR.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenu22BtnRoom4Text())
                .callbackData(userMenuVariables.getMenu22BtnRoom4Callback())
                .build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(buttonOne, buttonTwo));
        rows.add(List.of(buttonThree, buttonFour));
        rows.add(List.of(buttonZero));

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
