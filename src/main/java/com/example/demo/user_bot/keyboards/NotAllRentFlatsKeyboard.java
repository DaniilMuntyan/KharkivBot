package com.example.demo.user_bot.keyboards;

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
public final class NotAllRentFlatsKeyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public NotAllRentFlatsKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура сообщения после рассылки N квартир ("Показать еще")
    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonSeeOthers = InlineKeyboardButton.builder()
                .text(userMenuVariables.getUserNotAllBtnSeeOthersText())
                .callbackData(userMenuVariables.getUserNotAllBtnSeeOthersCallback())
                .build();

        InlineKeyboardButton buttonEnough = InlineKeyboardButton.builder()
                .text(userMenuVariables.getUserNotAllBtnEnoughText())
                .callbackData(userMenuVariables.getUserBotNotAllBtnEnoughCallback())
                .build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(buttonSeeOthers));
        rows.add(List.of(buttonEnough));

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
