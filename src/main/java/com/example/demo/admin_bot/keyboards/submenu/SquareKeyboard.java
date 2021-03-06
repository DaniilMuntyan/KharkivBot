package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class SquareKeyboard {
    @Getter
    private InlineKeyboardMarkup keyboard;
    private final MenuVariables menuVariables;

    public SquareKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        menuVariables = BeanUtil.getBean(MenuVariables.class);

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuCancel())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        this.keyboard = inlineKeyboardMarkup;
    }
}
