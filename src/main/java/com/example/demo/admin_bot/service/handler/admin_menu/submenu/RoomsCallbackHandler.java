package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class RoomsCallbackHandler {
    private final AdminMenuVariables adminMenuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;

    @Autowired
    public RoomsCallbackHandler(AdminMenuVariables adminMenuVariables, AdminService adminService, CommonMethods commonMethods) {
        this.adminMenuVariables = adminMenuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
    }

    public BotApiMethod<?> handleRoomCallback(CallbackQuery callbackQuery, UserCache admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response;

        // Если выбрали 1-у комнату
        if(data.equals(adminMenuVariables.getAdminBtnCallbackRoomsOne())) {
            admin.getAdminChoice().setRooms(Rooms.ONE);
        }

        // Если выбрали 2-е комнаты
        if(data.equals(adminMenuVariables.getAdminBtnCallbackRoomsTwo())) {
            admin.getAdminChoice().setRooms(Rooms.TWO);
        }

        // Если выбрали 3 комнаты
        if(data.equals(adminMenuVariables.getAdminBtnCallbackRoomsThree())) {
            admin.getAdminChoice().setRooms(Rooms.THREE);
        }

        // Если выбрали 4 комнаты
        if(data.equals(adminMenuVariables.getAdminBtnCallbackRoomsFour())) {
            admin.getAdminChoice().setRooms(Rooms.FOUR);
        }

        // Если выбрали гостинку
        if(data.equals(adminMenuVariables.getAdminBtnCallbackRoomsZero())) {
            admin.getAdminChoice().setRooms(Rooms.GOSTINKA);
        }

        // Если нажали на любую кнопку, кроме "отмена" - сохраняем изменения данных.
        // Если же нажали "отмена" - остается без изменений
        if(!data.equals(adminMenuVariables.getAdminBtnCallbackSubmenuCancel())) {
            adminService.saveChoice(admin.getAdminChoice());
        }

        response = commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin);
        return response;
    }
}
