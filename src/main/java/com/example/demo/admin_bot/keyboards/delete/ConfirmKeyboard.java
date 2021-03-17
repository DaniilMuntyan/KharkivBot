package com.example.demo.admin_bot.keyboards.delete;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class ConfirmKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;

    public ConfirmKeyboard() {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.keyboard = getConfirmMenu();
    }

    private InlineKeyboardMarkup getConfirmMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonConfirm = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextConfirm())
                .callbackData(adminMenuVariables.getDeleteBtnCallbackConfirm())
                .build();
        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextCancel())
                .callbackData(adminMenuVariables.getDeleteBtnCallbackCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonConfirm, buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));

        return inlineKeyboardMarkup;
    }

}
