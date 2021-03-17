package com.example.demo.admin_bot.keyboards.delete;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class CategoryKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;

    public CategoryKeyboard() {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.keyboard = getCategoryMenu();
    }

    private InlineKeyboardMarkup getCategoryMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonRental = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextRental())
                .callbackData(adminMenuVariables.getDeleteBtnCallbackRental())
                .build();
        InlineKeyboardButton buttonBuy = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextBuy())
                .callbackData(adminMenuVariables.getDeleteBtnTextRental())
                .build();
        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextCancel())
                .callbackData(adminMenuVariables.getDeleteBtnCallbackCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonRental);
        List<InlineKeyboardButton> row2 = List.of(buttonBuy);
        List<InlineKeyboardButton> row3 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1, row2, row3));

        return inlineKeyboardMarkup;
    }

}
