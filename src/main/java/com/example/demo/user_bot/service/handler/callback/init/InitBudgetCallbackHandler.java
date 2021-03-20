package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.money_range.Budget;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.money_range.RentalRange;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.handler.callback.ChangeBudgetChoiceService;
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
    private final MessagesVariables messagesVariables;

    private final DataCache dataCache;

    private final ChangeBudgetChoiceService changeBudgetChoiceService;

    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public InitBudgetCallbackHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, DataCache dataCache, ChangeBudgetChoiceService changeBudgetChoiceService, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.dataCache = dataCache;
        this.changeBudgetChoiceService = changeBudgetChoiceService;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        Budget[] allBudgets = user.getUserChoice().getIsRentFlat() ? RentalRange.values() : BuyRange.values();

        // Ищу какой бюджет выбрали
        for (Budget temp: allBudgets) {
            if (data.equals(userMenuVariables.getMenuInitBudgetBtnRangePrefixCallback() + temp.getIdentifier())) {
                long time1 = System.currentTimeMillis();
                budgetCallback(temp, response, callbackQuery, user);
                LOGGER.info("TIME budgetCallback: " + (System.currentTimeMillis() - time1));
                break;
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

    private void budgetCallback(Budget range, List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        try {
            this.changeBudgetChoiceService.getNewBudgetChoice(range, user); // Меняю бюджет пользователя
        } catch (Exception exception) {
            LOGGER.error(exception);
            exception.printStackTrace();
        }
        response.add(getEditMarkup(callbackQuery, user));
    }

    private void selectAllCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isAllSelected = true; // Если все районы уже выбраны

        Budget[] allBudgets = user.getUserChoice().getIsRentFlat() ? RentalRange.values() : BuyRange.values();

        for (Budget temp: allBudgets) { // Проверяю, были ли выбраны все варианты
            if (!budgetChoice.contains(temp.getIdentifier())) {
                isAllSelected = false;
                break;
            }
        }

        if (isAllSelected) { // Если все варианты были выбраны - снимаю галочку со всех
            user.getUserChoice().setBudget(""); // Снимаем выбор со всех
        } else { // Если нет - выбираю все варианты
            StringBuilder allRangeChoice = new StringBuilder();
            for (Budget temp: allBudgets) {
                allRangeChoice.append(temp.getIdentifier());
            }
            user.getUserChoice().setBudget(allRangeChoice.toString());
        }

        this.dataCache.saveUserCache(user);
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