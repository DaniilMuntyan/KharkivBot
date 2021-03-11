package com.example.demo.user_bot.keyboards;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.model.AdminChoice;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class PublishFlatKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;
    private final AdminChoice adminChoice;

    public PublishFlatKeyboard(AdminChoice adminChoice) {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.adminChoice = adminChoice;
        this.keyboard = getNewFlatMenu();
    }

    private InlineKeyboardMarkup getNewFlatMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (this.adminChoice.getMapLink() != null) {
            InlineKeyboardButton buttonMap = InlineKeyboardButton.builder()
                    .text(adminMenuVariables.getNewFlatBtnMap())
                    .url(this.adminChoice.getHtmlMapLink())
                    .build();
            rows.add(List.of(buttonMap));
        }

        if (this.adminChoice.getContact() != null) {
            InlineKeyboardButton buttonContact = InlineKeyboardButton.builder()
                    .text(adminMenuVariables.getNewFlatBtnContact())
                    .url(this.adminChoice.getContact())
                    .build();
            rows.add(List.of(buttonContact));
        }


        if (rows.size() == 0) {
            return null;
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
