package com.example.demo.user_bot.keyboards;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.keyboards.init.InitBudgetKeyboard;
import com.example.demo.user_bot.keyboards.init.InitCategoryKeyboard;
import com.example.demo.user_bot.keyboards.init.InitDistrictsKeyboard;
import com.example.demo.user_bot.keyboards.init.InitRoomsKeyboard;
import com.example.demo.user_bot.keyboards.menu.Menu1Keyboard;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class KeyboardsRegistry {
    private final UserMenuVariables userMenuVariables;

    @Getter
    private final InitRoomsKeyboard initRoomsMenu;
    @Getter
    private final InitCategoryKeyboard initCategoryMenu;
    @Getter
    private final InitDistrictsKeyboard initDistrictsKeyboard;
    @Getter
    private final InitBudgetKeyboard initBudgetKeyboard;
    @Getter
    private final Menu1Keyboard menu1;

    @Autowired
    public KeyboardsRegistry(UserMenuVariables userMenuVariables, InitRoomsKeyboard initRoomsMenu, InitCategoryKeyboard initCategoryMenu, InitDistrictsKeyboard initDistrictsKeyboard, InitBudgetKeyboard initBudgetKeyboard, Menu1Keyboard menu1) {
        this.userMenuVariables = userMenuVariables;
        this.initRoomsMenu = initRoomsMenu;
        this.initCategoryMenu = initCategoryMenu;
        this.initDistrictsKeyboard = initDistrictsKeyboard;
        this.initBudgetKeyboard = initBudgetKeyboard;
        this.menu1 = menu1;
    }
}
