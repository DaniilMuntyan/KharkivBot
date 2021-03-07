package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class BulkMessageConfirmKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    public BulkMessageConfirmKeyboard() {
        MenuVariables menuVariables = BeanUtil.getBean(MenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnConfirmMessageYes())
                .callbackData(menuVariables.getAdminBtnCallbackConfirmMessageYes())
                .build();

        InlineKeyboardButton buttonNo = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnConfirmMessageNo())
                .callbackData(menuVariables.getAdminBtnCallbackConfirmMessageNo())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonYes, buttonNo);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        this.keyboard = inlineKeyboardMarkup;
    }
}
