package com.example.demo.user_bot.keyboards;

import com.example.demo.user_bot.keyboards.init.InitBudgetKeyboard;
import com.example.demo.user_bot.keyboards.init.InitCategoryKeyboard;
import com.example.demo.user_bot.keyboards.init.InitDistrictsKeyboard;
import com.example.demo.user_bot.keyboards.init.InitRoomsKeyboard;
import com.example.demo.user_bot.keyboards.menu.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public final class KeyboardsRegistry {

    private final InitRoomsKeyboard initRoomsMenu;
    private final InitCategoryKeyboard initCategoryMenu;
    private final InitDistrictsKeyboard initDistrictsKeyboard;
    private final InitBudgetKeyboard initBudgetKeyboard;

    private final NotAllRentFlatsKeyboard notAllRentFlatsKeyboard;

    private final Menu1Keyboard menu1;
    private final Menu2Keyboard menu2;
    private final Menu21Keyboard menu21;
    private final Menu22Keyboard menu22;
    private final Menu23Keyboard menu23;
    private final Menu24Keyboard menu24;
    private final Menu3Keyboard menu3;
    private final Menu32Keyboard menu32;

    @Autowired
    public KeyboardsRegistry(InitRoomsKeyboard initRoomsMenu, InitCategoryKeyboard initCategoryMenu, InitDistrictsKeyboard initDistrictsKeyboard, InitBudgetKeyboard initBudgetKeyboard, Menu1Keyboard menu1, NotAllRentFlatsKeyboard notAllRentFlatsKeyboard, Menu2Keyboard menu2, Menu21Keyboard menu21, Menu22Keyboard menu22, Menu23Keyboard menu23, Menu24Keyboard menu24, Menu3Keyboard menu3, Menu32Keyboard menu32) {
        this.initRoomsMenu = initRoomsMenu;
        this.initCategoryMenu = initCategoryMenu;
        this.initDistrictsKeyboard = initDistrictsKeyboard;
        this.initBudgetKeyboard = initBudgetKeyboard;
        this.notAllRentFlatsKeyboard = notAllRentFlatsKeyboard;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu21 = menu21;
        this.menu22 = menu22;
        this.menu23 = menu23;
        this.menu24 = menu24;
        this.menu3 = menu3;
        this.menu32 = menu32;
    }
}
