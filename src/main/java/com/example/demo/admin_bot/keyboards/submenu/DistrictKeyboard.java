package com.example.demo.admin_bot.keyboards.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.common_part.utils.District;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class DistrictKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final MenuVariables menuVariables;

    public DistrictKeyboard() {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.keyboard = getDistrictMenu();
    }

    private InlineKeyboardMarkup getDistrictMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<String> texts = this.getTexts();

        List<InlineKeyboardButton> districtButtons = new ArrayList<>();
        for (String temp: texts) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(temp)
                    .callbackData(menuVariables.getCallbackSubmenuDistrict(temp))
                    .build();
            districtButtons.add(button);
        }

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnSubmenuCancel())
                .callbackData(menuVariables.getAdminBtnCallbackSubmenuCancel())
                .build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (districtButtons.size() > 1) {
            int i;
            for (i = 1; i < districtButtons.size(); i += 2) {
                rows.add(List.of(districtButtons.get(i - 1), districtButtons.get(i)));
            }
            if (i == districtButtons.size()) { // Если одна кнопка осталась нечетной
                rows.add(List.of(districtButtons.get(i - 1)));
            }
        } else { // Только один район
            rows.add(districtButtons);
        }

        rows.add(List.of(buttonCancel));
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    private List<String> getTexts() {
        return District.getAllNames();
    }
}
