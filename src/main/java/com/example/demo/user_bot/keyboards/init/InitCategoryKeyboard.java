package com.example.demo.user_bot.keyboards.init;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class InitCategoryKeyboard {

    private final UserMenuVariables userMenuVariables;

    public InitCategoryKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню инициализации №1 (выбор категории: аренда/продажа)
    public InlineKeyboardMarkup getKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton rentBtn = new InlineKeyboardButton();
        rentBtn.setText(userMenuVariables.getMenuInitBtnCategoryRentalText());
        rentBtn.setCallbackData(userMenuVariables.getMenuInitCategoryBtnRentalCallback());

        InlineKeyboardButton buyBtn = new InlineKeyboardButton();
        buyBtn.setText(userMenuVariables.getMenuInitBtnCategoryBuyText());
        buyBtn.setCallbackData(userMenuVariables.getMenuInitCategoryBtnBuyCallback());

        rows.add(List.of(rentBtn));
        rows.add(List.of(buyBtn));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
