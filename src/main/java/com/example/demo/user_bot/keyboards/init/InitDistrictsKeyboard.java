package com.example.demo.user_bot.keyboards.init;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.model.UserChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class InitDistrictsKeyboard {
    private final UserMenuVariables userMenuVariables;

    @Autowired
    public InitDistrictsKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню инициализации №3 (выбор районов)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String districtChoice = userChoice.getDistricts();

        // Callback'и состоят из префикса и идентификатора района
        List<InlineKeyboardButton> districtButtons = new ArrayList<>();
        for (District district: District.values()) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(district.getName())
                    .callbackData(userMenuVariables.getMenuInitDistrictsBtnPrefixCallback() +
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

        if (!userChoice.getDistricts().isEmpty()) {
            InlineKeyboardButton buttonNext = InlineKeyboardButton.builder()
                    .text(userMenuVariables.getMenuInitBtnNextText())
                    .callbackData(userMenuVariables.getMenuInitBtnNextCallback())
                    .build();
            rows.add(List.of(buttonNext));
        }

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
