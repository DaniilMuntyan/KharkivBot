package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.model.UserChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu23Keyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public Menu23Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню 2.3 (выбрать районы)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String districtChoice = userChoice.getDistricts();

        // Callback'и состоят из префикса и идентификатора района
        // Текст - если districtChoice имеет в себе идентификатор текущего dictrict - то SELECTED
        List<InlineKeyboardButton> districtButtons = new ArrayList<>();
        for (District district: District.values()) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text((districtChoice.contains(district.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                            district.getName())
                    .callbackData(userMenuVariables.getMenu23BtnDistrictCallbackPrefix() +
                            district.getIdentifier())
                    .build();
            districtButtons.add(button);
        }

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (districtButtons.size() > 1) {
            int i;
            for (i = 1; i < districtButtons.size(); i += 2) {
                rows.add(List.of(districtButtons.get(i - 1), districtButtons.get(i)));
            }
            if (i == districtButtons.size()) { // Если одна кнопка осталась нечетной
                rows.add(List.of(districtButtons.get(i - 1)));
            }
        }

        // Кнопка "выбрать все"
        InlineKeyboardButton selectAllButton = InlineKeyboardButton.builder()
                .text(userMenuVariables.getMenu23BtnSelectAllText())
                .callbackData(userMenuVariables.getMenu23BtnSelectAllCallback())
                .build();
        rows.add(List.of(selectAllButton));

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
