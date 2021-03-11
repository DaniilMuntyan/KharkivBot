package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class MapKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    public MapKeyboard() {
        AdminMenuVariables adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonGoogleMaps = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnSubmenuGoogleMaps())
                .url(adminMenuVariables.getAdminBtnSubmenuGoogleMapsLink())
                .callbackData(adminMenuVariables.getAdminBtnCallbackSubmenuGoogleMaps())
                .build();

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnSubmenuCancel())
                .callbackData(adminMenuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonGoogleMaps);
        List<InlineKeyboardButton> row2 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1, row2));
        this.keyboard = inlineKeyboardMarkup;
    }
}
