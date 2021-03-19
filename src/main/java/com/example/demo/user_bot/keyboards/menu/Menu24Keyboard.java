package com.example.demo.user_bot.keyboards.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.user_bot.keyboards.init.InitBudgetKeyboard;
import com.example.demo.user_bot.model.UserChoice;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class Menu24Keyboard {
    private static final Logger LOGGER = Logger.getLogger(Menu24Keyboard.class);

    private final UserMenuVariables userMenuVariables;

    public Menu24Keyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню 2.4 (выбрать бюджет)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String budgetChoice = userChoice.getBudget();

        List<InlineKeyboardButton> budgetButtons = new ArrayList<>();

        if (userChoice.getIsRentFlat()) { // Если пользователь ищет арендовать квартиру
            for (RentalRange rentalRange: RentalRange.values()) {
                // Callback состоит из префикса и идентификатора бюджета
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text((budgetChoice.contains(rentalRange.getIdentifier()) ? Emoji.SELECTED + " " : "") +
                                rentalRange.toString())
                        .callbackData(userMenuVariables.getMenu24BtnRangePrefixCallback() +
                                rentalRange.getIdentifier())
                        .build();
                budgetButtons.add(button);
            }
        } else {
            for (BuyRange buyRange: BuyRange.values()) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text((budgetChoice.contains(buyRange.getIdentifier()) ? Emoji.SELECTED + " " : "") +
                                buyRange.toString())
                        .callbackData(userMenuVariables.getMenu24BtnRangePrefixCallback() +
                                buyRange.getIdentifier())
                        .build();
                budgetButtons.add(button);
            }
        }

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (budgetButtons.size() > 1) {
            int i;
            for (i = 1; i < budgetButtons.size(); i += 2) {
                rows.add(List.of(budgetButtons.get(i - 1), budgetButtons.get(i)));
            }
            if (i == budgetButtons.size()) { // Если одна кнопка осталась нечетной
                rows.add(List.of(budgetButtons.get(i - 1)));
            }
        }

        // Кнопка "выбрать все"
        InlineKeyboardButton selectAllButton = InlineKeyboardButton.builder()
                .text(userMenuVariables.getMenu24BtnBudgetSelectAllText())
                .callbackData(userMenuVariables.getMenu24BtnBudgetSelectAllCallback())
                .build();
        rows.add(List.of(selectAllButton));

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
