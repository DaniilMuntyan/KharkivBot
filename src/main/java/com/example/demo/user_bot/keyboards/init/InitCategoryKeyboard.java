package com.example.demo.user_bot.keyboards.init;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.model.UserChoice;
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
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton rentBtn = new InlineKeyboardButton();
        if (userChoice.getIsRentFlat() != null) { // Если еще не было выбора категории
            rentBtn.setText((userChoice.getIsRentFlat() ? Emoji.SELECTED + " " : "") +
                    userMenuVariables.getMenuInitBtnCategoryRentalText());
        } else {
            rentBtn.setText(userMenuVariables.getMenuInitBtnCategoryRentalText());
        }
        rentBtn.setCallbackData(userMenuVariables.getMenuInitCategoryBtnRentalCallback());

        InlineKeyboardButton buyBtn = new InlineKeyboardButton();
        if (userChoice.getIsRentFlat() != null) { // Если еще не было выбора категории
            buyBtn.setText((!userChoice.getIsRentFlat() ? Emoji.SELECTED + " " : "") +
                    userMenuVariables.getMenuInitBtnCategoryBuyText());
        } else {
            buyBtn.setText(userMenuVariables.getMenuInitBtnCategoryBuyText());
        }
        buyBtn.setCallbackData(userMenuVariables.getMenuInitCategoryBtnBuyCallback());

        rows.add(List.of(rentBtn));
        rows.add(List.of(buyBtn));

        // Если выбрали что-то - добавляем кнопку "Дальше"
        if (userChoice.getIsRentFlat() != null) {
            InlineKeyboardButton nextBtn = new InlineKeyboardButton();
            nextBtn.setText(userMenuVariables.getMenuInitBtnNextText());
            nextBtn.setCallbackData(userMenuVariables.getMenuInitBtnCategoryNextCallback());
            rows.add(List.of(nextBtn));
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
