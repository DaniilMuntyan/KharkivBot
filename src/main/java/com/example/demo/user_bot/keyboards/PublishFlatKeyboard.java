package com.example.demo.user_bot.keyboards;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.common_part.model.AdminChoice;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.RentalRange;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class PublishFlatKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final MenuVariables menuVariables;
    private final AdminChoice adminChoice;

    public PublishFlatKeyboard(AdminChoice adminChoice) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.adminChoice = adminChoice;
        this.keyboard = getNewFlatMenu();
    }

    private InlineKeyboardMarkup getNewFlatMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (this.adminChoice.getMapLink() != null) {
            InlineKeyboardButton buttonMap = InlineKeyboardButton.builder()
                    .text(menuVariables.getNewFlatBtnMap())
                    .url(this.adminChoice.getMapLink())
                    .build();
            rows.add(List.of(buttonMap));
        }

        if (this.adminChoice.getContact() != null) {
            InlineKeyboardButton buttonContact = InlineKeyboardButton.builder()
                    .text(menuVariables.getNewFlatBtnContact())
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
