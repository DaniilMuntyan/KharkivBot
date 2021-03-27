package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.common_part.utils.money_range.Budget;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ChangeBudgetChoiceService {
    private static final Logger LOGGER = Logger.getLogger(ChangeBudgetChoiceService.class);

    private final DataCache dataCache;

    @Autowired
    public ChangeBudgetChoiceService(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    // Метод для изменения предпочитаемого бюджета (при клике на клавиатуру изменения бюджета)
    public void getNewBudgetChoice(Budget range, UserCache user) throws Exception {
        String budgetChoice = user.getUserChoice().getBudget();
        boolean isBudgetChecked = budgetChoice.contains(range.getIdentifier());

        Budget[] allBudgets = range.getAllRanges();

        int startOrdinal = -1, endOrdinal = -1; // Первый и последний выбранный бюджет
        int selectedCount = 0;
        for (Budget temp: allBudgets) {
            if (budgetChoice.contains(temp.getIdentifier())) { // Ищу первый и последний выбранный бюджет
                selectedCount++;
                if (selectedCount == 1) {
                    startOrdinal = temp.ordinal();
                }
                endOrdinal = temp.ordinal();
            }
        }

        if (isBudgetChecked) { // Если этот бюджет уже был выбран - снимаю галочку
            if (range.ordinal() == startOrdinal || range.ordinal() == endOrdinal) { // Если нажали на крайние - убираю только их
                user.removeBudget(range);
            } else { // Если нажали на какой-то "внутренний" бюджет
                if (range.ordinal() - startOrdinal < endOrdinal - range.ordinal()) { // Если нам ближе убрать бюджеты меньшие от текущего
                    for (int i = startOrdinal; i <= range.ordinal(); ++i) {
                        user.removeBudget(allBudgets[i]);
                    }
                } else { // Если нам ближе убрать бюджеты большие от текущего
                    for (int i = range.ordinal(); i <= endOrdinal; ++i) {
                        user.removeBudget(allBudgets[i]);
                    }
                }
            }
        } else { // Если текущий бюджет не был выбран ранее - добавляю выбор
            //budgetChoice.append(range.getIdentifier());
            if (selectedCount > 0) { // Если что-то уже было выбрано бюджетов
                if (endOrdinal < range.ordinal()) { // Если нажали на бюджет, который больше по порядку, чем конечный выбранный
                    for (int i = endOrdinal + 1; i <= range.ordinal(); ++i) { // Отмечаю все бюджеты, начиная с последнего и до текущего
                        user.addBudget(allBudgets[i]);
                    }
                } else { // Если нажали на бюджет, который меньше по порядку, чем начальный выбранный
                    for (int i = range.ordinal(); i < startOrdinal; ++i) { // Отмечаю все бюджеты с текущего до стартового
                        user.addBudget(allBudgets[i]);
                    }
                }
            } else { // Выбранных раньше вариантов нет - добавляем текущим выбор
                user.addBudget(range);
            }
        }
        this.dataCache.saveUserCache(user);
    }
}
