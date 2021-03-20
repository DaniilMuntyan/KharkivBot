package com.example.demo.user_bot.service.handler.callback.menu;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.money_range.Budget;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.money_range.RentalRange;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.handler.callback.ChangeBudgetChoiceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class Menu24BudgetCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu24BudgetCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;

    private final DataCache dataCache;

    private final KeyboardsRegistry keyboardsRegistry;

    private final ChangeBudgetChoiceService changeBudgetChoiceService;

    @Autowired
    public Menu24BudgetCallbackHandler(UserMenuVariables userMenuVariables, DataCache dataCache, KeyboardsRegistry keyboardsRegistry, ChangeBudgetChoiceService changeBudgetChoiceService) {
        this.userMenuVariables = userMenuVariables;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
        this.changeBudgetChoiceService = changeBudgetChoiceService;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();
        LOGGER.info("MENU 24 DATA: " + data);
        if (user.getUserChoice().getIsRentFlat()) { // Если выбираем бюджет для аренды квартиры
            // Ищем, какой бюджет выбрали
            for (RentalRange temp: RentalRange.values()) {
                if (data.equals(userMenuVariables.getMenu24BtnRangeCallbackPrefix() + temp.getIdentifier())) {
                    long time1 = System.currentTimeMillis();
                    budgetCallback(temp, response, callbackQuery, user);
                    LOGGER.info("TIME budgetCallback: " + (System.currentTimeMillis() - time1));
                    break;
                }
            }
        } else { // Если выбираем бюджет для покупки квартиры
            // Ищем, какой бюджет выбрали
            for (BuyRange temp: BuyRange.values()) {
                if (data.equals(userMenuVariables.getMenu24BtnRangeCallbackPrefix() + temp.getIdentifier())) {
                    long time1 = System.currentTimeMillis();
                    budgetCallback(temp, response, callbackQuery, user);
                    LOGGER.info("TIME budgetCallback: " + (System.currentTimeMillis() - time1));
                    break;
                }
            }
        }

        // Если нажали кнопку "Выбрать все"
        if (data.equals(userMenuVariables.getMenu24BtnBudgetSelectAllCallback())) {
            long time1 = System.currentTimeMillis();
            selectAllCallback(response, callbackQuery, user);
            LOGGER.info("TIME selectAllCallback: " + (System.currentTimeMillis() - time1));
        }
    }

    private void budgetCallback(Budget range, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        try {
            this.changeBudgetChoiceService.getNewBudgetChoice(range, user); // Меняю бюджет пользователя
        } catch (Exception exception) {
            LOGGER.error(exception);
            exception.printStackTrace();
        }
        response.add(getEditMarkup(callbackQuery, user));
    }
    /*private void budgetCallback(RentalRange range, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isBudgetChecked = budgetChoice.contains(range.getIdentifier());

        if (isBudgetChecked) { // Если бюджет был выбран - снимаем галочку
            user.getUserChoice().setBudget(budgetChoice.replace(range.getIdentifier(), ""));
        } else { // Если нет - добавляем выбор
            user.getUserChoice().setBudget(budgetChoice + range.getIdentifier());
        }

        this.dataCache.saveUserCache(user);
        //this.dataCache.markNotSaved(user.getChatId());
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

        this.dataCache.saveUserCache(user);
        //this.dataCache.markNotSaved(user.getChatId());
        response.add(getEditMarkup(callbackQuery, user));
    }*/

    private void selectAllCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        LOGGER.info("IN SELECT ALL CALLBACK");

        String budgetChoice = user.getUserChoice().getBudget();
        boolean isAllSelected = true; // Если все районы уже выбраны
        if (user.getUserChoice().getIsRentFlat()) { // Если выбираем квартиру для аренды
            for (RentalRange temp: RentalRange.values()) { // Проверяю, выбраны ли все варианты
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
        } else { // Если выбираем квартиру для покупки
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

        this.dataCache.saveUserCache(user);
        //this.dataCache.markNotSaved(user.getChatId());
        response.add(getEditMarkup(callbackQuery, user));
    }

    private EditMessageReplyMarkup getEditMarkup(CallbackQuery callbackQuery, UserCache user) {
        // Меняю клавиатуру в зависимости от выбора пользователя
        EditMessageReplyMarkup editKeyboard = new EditMessageReplyMarkup();
        editKeyboard.setReplyMarkup(keyboardsRegistry.getMenu24().getKeyboard(user.getUserChoice()));
        editKeyboard.setChatId(user.getChatId().toString());
        editKeyboard.setMessageId(callbackQuery.getMessage().getMessageId());
        return editKeyboard;
    }
}
