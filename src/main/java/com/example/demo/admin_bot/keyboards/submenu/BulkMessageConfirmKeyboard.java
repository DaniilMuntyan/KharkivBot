package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class BulkMessageConfirmKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    public BulkMessageConfirmKeyboard() {
        AdminMenuVariables adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnConfirmMessageYes())
                .callbackData(adminMenuVariables.getAdminBtnCallbackConfirmMessageYes())
                .build();

        InlineKeyboardButton buttonNo = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnConfirmMessageNo())
                .callbackData(adminMenuVariables.getAdminBtnCallbackConfirmMessageNo())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonYes, buttonNo);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        this.keyboard = inlineKeyboardMarkup;
    }
}
