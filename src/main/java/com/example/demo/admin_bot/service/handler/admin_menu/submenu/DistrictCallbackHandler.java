package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public final class DistrictCallbackHandler {
    private final AdminMenuVariables adminMenuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;

    @Autowired
    public DistrictCallbackHandler(AdminMenuVariables adminMenuVariables, AdminService adminService, CommonMethods commonMethods) {
        this.adminMenuVariables = adminMenuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
    }

    public BotApiMethod<?> handleDistrictCallback(CallbackQuery callbackQuery, User admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response;

        // Ищем на какую кнопку нажали (цикл вместо if-ов)
        for(District temp: District.values()) {
            if (callbackQuery.getData().equals(adminMenuVariables.getCallbackSubmenuDistrict(temp.getName()))) {
                admin.getAdminChoice().setDistrict(temp);
                break;
            }
        }

        // Если нажали на любую кнопку, кроме "отмена" - сохраняем изменения данных.
        // Если же нажали "отмена" - все остается без изменений
        if(!data.equals(adminMenuVariables.getAdminBtnCallbackSubmenuCancel())) {
            adminService.saveChoice(admin.getAdminChoice());
        }

        response = commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin);

        return response;
    }
}
