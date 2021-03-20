package com.example.demo.user_bot.keyboards.init;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.money_range.RentalRange;
import com.example.demo.user_bot.model.UserChoice;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class InitBudgetKeyboard {
    private static final Logger LOGGER = Logger.getLogger(InitBudgetKeyboard.class);

    private final UserMenuVariables userMenuVariables;

    public InitBudgetKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню инициализации №4 (выбор бюджета)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String budgetChoice = userChoice.getBudget();

        List<InlineKeyboardButton> budgetButtons = new ArrayList<>();

        if (userChoice.getIsRentFlat()) { // Если пользователь ищет арендовать квартиру
            for (RentalRange rentalRange: RentalRange.values()) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text((budgetChoice.contains(rentalRange.getIdentifier()) ? Emoji.SELECTED + " " : "") +
                                rentalRange.toString())
                        .callbackData(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback() +
                                rentalRange.getIdentifier())
                        .build();
                budgetButtons.add(button);
            }
        } else {
            for (BuyRange buyRange: BuyRange.values()) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text((budgetChoice.contains(buyRange.getIdentifier()) ? Emoji.SELECTED + " " : "") +
                                buyRange.toString())
                        .callbackData(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback() +
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
                .text(userMenuVariables.getMenuInitBudgetSelectAllText())
                .callbackData(userMenuVariables.getMenuInitBudgetBtnSelectAllCallback())
                .build();
        rows.add(List.of(selectAllButton));

        if (!userChoice.getBudget().isEmpty()) {
            InlineKeyboardButton buttonNext = InlineKeyboardButton.builder()
                    .text(userMenuVariables.getMenuInitBtnNextText())
                    .callbackData(userMenuVariables.getMenuInitBudgetNextCallback())
                    .build();
            rows.add(List.of(buttonNext));
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
