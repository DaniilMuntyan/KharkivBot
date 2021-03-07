package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class ContactKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    public ContactKeyboard() {
        MenuVariables menuVariables = BeanUtil.getBean(MenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonMyContact = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuContact())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuMyContact())
                .build();

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuCancel())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonMyContact);
        List<InlineKeyboardButton> row2 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1, row2));
        this.keyboard = inlineKeyboardMarkup;
    }
}
