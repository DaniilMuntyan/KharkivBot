package com.example.demo.admin_bot.keyboards.delete;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class EnterIdKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;

    public EnterIdKeyboard() {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.keyboard = getEnterIdMenu();
    }

    private InlineKeyboardMarkup getEnterIdMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getDeleteBtnTextCancel())
                .callbackData(adminMenuVariables.getDeleteBtnCallbackCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));

        return inlineKeyboardMarkup;
    }

}
