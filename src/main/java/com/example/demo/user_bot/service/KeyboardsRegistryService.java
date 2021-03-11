package com.example.demo.user_bot.service;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.keyboards.init.InitCategoryKeyboard;
import com.example.demo.user_bot.keyboards.init.InitRoomsKeyboard;
import com.example.demo.user_bot.keyboards.menu.Menu1Keyboard;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class KeyboardsRegistryService {
    private final UserMenuVariables userMenuVariables;

    @Getter
    private final InitRoomsKeyboard initRoomsMenu;
    @Getter
    private final InitCategoryKeyboard initCategoryMenu;
    @Getter
    private final Menu1Keyboard menu1;

    @Autowired
    public KeyboardsRegistryService(UserMenuVariables userMenuVariables, InitRoomsKeyboard initRoomsMenu, InitCategoryKeyboard initCategoryMenu, Menu1Keyboard menu1) {
        this.userMenuVariables = userMenuVariables;
        this.initRoomsMenu = initRoomsMenu;
        this.initCategoryMenu = initCategoryMenu;
        this.menu1 = menu1;
    }
}
