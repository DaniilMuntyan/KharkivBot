package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public final class PublishKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    public PublishKeyboard() {
        this.keyboard = getPublishMenu();
    }

    private InlineKeyboardMarkup getPublishMenu() {
        AdminMenuVariables adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnPublishConfirmYes())
                .callbackData(adminMenuVariables.getAdminBtnCallbackSubmenuConfirmYes())
                .build();

        InlineKeyboardButton buttonNo = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnPublishConfirmNo())
                .callbackData(adminMenuVariables.getAdminBtnCallbackSubmenuConfirmNo())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonYes, buttonNo);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        return inlineKeyboardMarkup;
    }
}
