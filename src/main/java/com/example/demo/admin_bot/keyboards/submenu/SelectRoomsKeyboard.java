package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class SelectRoomsKeyboard {
    @Getter
    private InlineKeyboardMarkup selectRoomsMenu;

    @Getter
    @Setter
    private boolean[] choice;

    private final MenuVariables menuVariables;

    public SelectRoomsKeyboard() {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);

        this.choice = new boolean[] {false, false, false, false, false};

        this.selectRoomsMenu = getKeyboardFromChoice();
    }

    public SelectRoomsKeyboard(boolean[] choice) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        if (choice.length == 5) {
            this.choice = choice;
            this.selectRoomsMenu = getKeyboardFromChoice();
        }
    }

    private InlineKeyboardMarkup getKeyboardFromChoice() {
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
        answer.add((this.choice[0] ? Emoji.SELECTED : "") + " " + menuVariables.getAdminBtnRoomsZero());
        answer.add((this.choice[1] ? Emoji.SELECTED : "") + " " + Emoji.ONE.toString());
        answer.add((this.choice[2] ? Emoji.SELECTED : "") + " " + Emoji.TWO.toString());
        answer.add((this.choice[3] ? Emoji.SELECTED : "") + " " + Emoji.THREE.toString());
        answer.add((this.choice[4] ? Emoji.SELECTED : "") + " " + Emoji.FOUR.toString());

        return answer;
    }

    public void refreshKeyboard() {
        this.selectRoomsMenu = getKeyboardFromChoice();
    }

    public void pickZero() {
        this.choice[0] = !this.choice[0];
    }

    public void pickOne() {
        this.choice[1] = !this.choice[1];
    }

    public void pickTwo() {
        this.choice[2] = !this.choice[2];
    }

    public void pickThree() {
        this.choice[3] = !this.choice[3];
    }

    public void pickFour() {
        this.choice[4] = !this.choice[4];
    }
}
