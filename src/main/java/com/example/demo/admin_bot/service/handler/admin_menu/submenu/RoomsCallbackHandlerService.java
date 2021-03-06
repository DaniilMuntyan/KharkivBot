package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.model.User;
import com.example.demo.user_bot.utils.Rooms;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class RoomsCallbackHandlerService {
    private final MenuVariables menuVariables;
    private final AdminService adminService;

    public RoomsCallbackHandlerService(MenuVariables menuVariables, AdminService adminService) {
        this.menuVariables = menuVariables;
        this.adminService = adminService;
    }

    public BotApiMethod<?> handleRoomCallback(CallbackQuery callbackQuery, User admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response = null;

        // Если выбрали 1-у комнату
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsOne())) {
            admin.getAdminChoice().setRooms(Rooms.ONE);
        }

        // Если выбрали 2-е комнаты
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsTwo())) {
            admin.getAdminChoice().setRooms(Rooms.TWO);
        }

        // Если выбрали 3 комнаты
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsThree())) {
            admin.getAdminChoice().setRooms(Rooms.THREE);
        }

        // Если выбрали 4 комнаты
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsFour())) {
            admin.getAdminChoice().setRooms(Rooms.FOUR);
        }

        // Если выбрали гостинку
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsZero())) {
            admin.getAdminChoice().setRooms(Rooms.GOSTINKA);
        }

        // Если выбрали гостинку
        if(data.equals(menuVariables.getAdminBtnCallbackRoomsZero())) {
            admin.getAdminChoice().setRooms(Rooms.GOSTINKA);
        }

        // Если нажали на любую кнопку, кроме "отмена" - сохраняем изменения данных.
        // Если же нажали "отмена" - остается без изменений
        if(!data.equals(menuVariables.getAdminBtnCallbackSubmenuCancel())) {
            adminService.saveAdmin(admin);
        }

        response = getEditKeyboard(chatId, messageId, admin);
        return response;
    }

    private EditMessageText getEditKeyboard(Long chatId, Integer messageId, User admin) {
        NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                menuVariables.getAdminAddRentFlatText() : menuVariables.getAdminAddBuyFlatText());
        return editMessageText;
    }
}
