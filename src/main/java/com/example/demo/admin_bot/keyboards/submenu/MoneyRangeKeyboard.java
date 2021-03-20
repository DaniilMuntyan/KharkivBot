package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.money_range.RentalRange;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class MoneyRangeKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;
    private final boolean isRentFlat;

    public MoneyRangeKeyboard(boolean isRentFlat) {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
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
                    .callbackData(adminMenuVariables.getCallbackSubmenuRange(temp))
                    .build();
            rangeButtons.add(button);
        }

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnSubmenuCancel())
                .callbackData(adminMenuVariables.getAdminBtnCallbackSubmenuCancel())
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
