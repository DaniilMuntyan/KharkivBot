package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class InitBudgetCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(InitBudgetCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final UserService userService;
    private final MessagesVariables messagesVariables;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public InitBudgetCallbackHandler(UserMenuVariables userMenuVariables, UserService userService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.userService = userService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();
        if (user.getUserChoice().getIsRentFlat()) { // Если выбираем бюджет для аренды квартиры
            // Ищем, какой бюджет выбрали
            for (RentalRange temp: RentalRange.values()) {
                if (data.equals(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback() + temp.getIdentifier())) {
                    long time1 = System.currentTimeMillis();
                    budgetCallback(temp, response, callbackQuery, user);
                    LOGGER.info("TIME budgetCallback: " + (System.currentTimeMillis() - time1));
                    break;
                }
            }
        } else { // Если выбираем бюджет для покупки квартиры
            // Ищем, какой бюджет выбрали
            for (BuyRange temp: BuyRange.values()) {
                if (data.equals(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback() + temp.getIdentifier())) {
                    long time1 = System.currentTimeMillis();
                    budgetCallback(temp, response, callbackQuery, user);
                    LOGGER.info("TIME budgetCallback: " + (System.currentTimeMillis() - time1));
                    break;
                }
            }
        }

        // Если нажали кнопку "Выбрать все"
        if (data.equals(userMenuVariables.getMenuInitBudgetBtnSelectAllCallback())) {
            long time1 = System.currentTimeMillis();
            selectAllCallback(response, callbackQuery, user);
            LOGGER.info("TIME selectAllCallback: " + (System.currentTimeMillis() - time1));
        }

        // Если нажали кнопку "Дальше"
        if (data.equals(userMenuVariables.getMenuInitBudgetNextCallback())) {
            long time1 = System.currentTimeMillis();
            nextCallback(response, callbackQuery, user);
            LOGGER.info("TIME nextCallback: " + (System.currentTimeMillis() - time1));
        }
    }

    private void budgetCallback(RentalRange range, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isBudgetChecked = budgetChoice.contains(range.getIdentifier());

        if (isBudgetChecked) { // Если бюджет был выбран - снимаем галочку
            user.getUserChoice().setBudget(budgetChoice.replace(range.getIdentifier(), ""));
        } else { // Если нет - добавляем выбор
            user.getUserChoice().setBudget(budgetChoice + range.getIdentifier());
        }

        response.add(getEditMarkup(callbackQuery, user));
    }
    private void budgetCallback(BuyRange range, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isBudgetChecked = budgetChoice.contains(range.getIdentifier());

        if (isBudgetChecked) { // Если бюджет был выбран - снимаем галочку
            user.getUserChoice().setBudget(budgetChoice.replace(range.getIdentifier(), ""));
        } else { // Если нет - добавляем выбор
            user.getUserChoice().setBudget(budgetChoice + range.getIdentifier());
        }

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void selectAllCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isAllSelected = true; // Если все районы уже выбраны
        if (user.getUserChoice().getIsRentFlat()) {
            for (RentalRange temp: RentalRange.values()) { // Проверяю, были ли выбраны все варианты
                if (!budgetChoice.contains(temp.getIdentifier())) {
                    isAllSelected = false;
                    break;
                }
            }

            if (isAllSelected) { // Если все варианты были выбраны - снимаю галочку со всех
                user.getUserChoice().setBudget(""); // Снимаем выбор со всех
            } else { // Если нет - выбираю все варианты
                StringBuilder allRentalRangeChoice = new StringBuilder();
                for (RentalRange temp: RentalRange.values()) {
                    allRentalRangeChoice.append(temp.getIdentifier());
                }
                user.getUserChoice().setBudget(allRentalRangeChoice.toString());
            }
        } else {
            for (BuyRange temp: BuyRange.values()) { // Проверяю, были ли выбраны все варианты
                if (!budgetChoice.contains(temp.getIdentifier())) {
                    isAllSelected = false;
                    break;
                }
            }

            if (isAllSelected) { // Если все варианты были выбраны - снимаю галочку со всех
                user.getUserChoice().setBudget(""); // Снимаем выбор со всех
            } else { // Если нет - выбираю все варианты
                StringBuilder allBuyRangeChoice = new StringBuilder();
                for (BuyRange temp: BuyRange.values()) {
                    allBuyRangeChoice.append(temp.getIdentifier());
                }
                user.getUserChoice().setBudget(allBuyRangeChoice.toString());
            }
        }

        response.add(getEditMarkup(callbackQuery, user));
    }

    private void nextCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        user.setBotUserState(UserState.FIRST_INIT_END); // Ставлю следующее состояние

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getUserInitEndText());

        response.add(editMessageText);
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getInitBudgetKeyboard().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}