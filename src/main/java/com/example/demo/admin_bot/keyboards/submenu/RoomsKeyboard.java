package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class RoomsKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;

    private final MenuVariables menuVariables;

    public RoomsKeyboard() {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);

        this.keyboard = getRoomsMenu();
    }

    private InlineKeyboardMarkup getRoomsMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<String> texts = this.getTexts();

        InlineKeyboardButton buttonZero = InlineKeyboardButton.builder()
                .text(texts.get(0))
                .callbackData(menuVariables.getAdminBtnCallbackRoomsZero())
                .build();
        InlineKeyboardButton buttonOne = InlineKeyboardButton.builder()
                .text(texts.get(1))
                .callbackData(menuVariables.getAdminBtnCallbackRoomsOne())
                .build();
        InlineKeyboardButton buttonTwo = InlineKeyboardButton.builder()
                .text(texts.get(2))
                .callbackData(menuVariables.getAdminBtnCallbackRoomsTwo())
                .build();
        InlineKeyboardButton buttonThree = InlineKeyboardButton.builder()
                .text(texts.get(3))
                .callbackData(menuVariables.getAdminBtnCallbackRoomsThree())
                .build();
        InlineKeyboardButton buttonFour = InlineKeyboardButton.builder()
                .text(texts.get(4))
                .callbackData(menuVariables.getAdminBtnCallbackRoomsFour())
                .build();
        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuCancel())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonOne, buttonTwo);
        List<InlineKeyboardButton> row2 = List.of(buttonThree, buttonFour);
        List<InlineKeyboardButton> row3 = List.of(buttonZero);
        List<InlineKeyboardButton> row4 = List.of(buttonCancel);

        inlineKeyboardMarkup.setKeyboard(List.of(row1, row2, row3, row4));

        return inlineKeyboardMarkup;
    }

    private List<String> getTexts() {
        List<String> answer = new ArrayList<>();
        answer.add(menuVariables.getAdminBtnRoomsZero());
        answer.add(Emoji.ONE.toString());
        answer.add(Emoji.TWO.toString());
        answer.add(Emoji.THREE.toString());
        answer.add(Emoji.FOUR.toString());

        return answer;
    }
}
