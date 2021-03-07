package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class MoneyRangeKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final MenuVariables menuVariables;
    private final boolean isRentFlat;

    public MoneyRangeKeyboard(boolean isRentFlat) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.isRentFlat = isRentFlat;
        this.keyboard = getMoneyRangeMenu();
    }

    private InlineKeyboardMarkup getMoneyRangeMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<String> texts = this.getTexts();

        List<InlineKeyboardButton> rangeButtons = new ArrayList<>();
        for (String temp: texts) {
            // Callback состоит из префикса и самого диапазона бюджета
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(temp)
                    .callbackData(menuVariables.getCallbackSubmenuRange(temp))
                    .build();
            rangeButtons.add(button);
        }

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuCancel())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (rangeButtons.size() > 1) {
            int i;
            for (i = 1; i < rangeButtons.size(); i += 2) {
                rows.add(List.of(rangeButtons.get(i - 1), rangeButtons.get(i)));
            }
            if (i == rangeButtons.size()) { // Если одна кнопка осталась нечетной
                rows.add(List.of(rangeButtons.get(i - 1)));
            }
        } else { // Только один диапазон бюджета
            rows.add(rangeButtons);
        }

        rows.add(List.of(buttonCancel));
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    private List<String> getTexts() {
        return this.isRentFlat ? RentalRange.getAllNames() : BuyRange.getAllNames();
    }
}
