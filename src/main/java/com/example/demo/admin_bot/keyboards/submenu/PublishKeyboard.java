package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
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
        MenuVariables menuVariables = BeanUtil.getBean(MenuVariables.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnPublishConfirmYes())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuConfirmYes())
                .build();

        InlineKeyboardButton buttonNo = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnPublishConfirmNo())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuConfirmNo())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonYes, buttonNo);

        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        return inlineKeyboardMarkup;
    }
}
