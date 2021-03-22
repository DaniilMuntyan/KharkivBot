package com.example.demo.user_bot.keyboards.flat;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.menu.Menu2Keyboard;
import com.example.demo.user_bot.model.UserChoice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class MenuSeeFlatKeyboard {
    private static final Logger LOGGER = Logger.getLogger(MenuSeeFlatKeyboard.class);

    private final UserMenuVariables userMenuVariables;

    @Autowired
    public MenuSeeFlatKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню уточнения просмотра
    public InlineKeyboardMarkup getKeyboard(boolean isRentFlat, Integer flatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // В зависимости от квартиры (аренда/продажа) - определяю callback.
        // Состоит из префикса (rent/buy) + айдишник квартиры
        String confirmData = (isRentFlat ? userMenuVariables.getUserConfirmSeeingRentCallbackPrefix() :
                userMenuVariables.getUserConfirmSeeingBuyCallbackPrefix()) + flatId.toString();
        InlineKeyboardButton confirmSeeing = new InlineKeyboardButton();
        confirmSeeing.setText(userMenuVariables.getUserConfirmSeeingYesText());
        confirmSeeing.setCallbackData(confirmData);

        InlineKeyboardButton cancelSeeing = new InlineKeyboardButton();
        cancelSeeing.setText(userMenuVariables.getUserConfirmSeeingCancelText());
        cancelSeeing.setCallbackData(userMenuVariables.getUserConfirmSeeingCancelCallback());

        rows.add(List.of(confirmSeeing, cancelSeeing));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
