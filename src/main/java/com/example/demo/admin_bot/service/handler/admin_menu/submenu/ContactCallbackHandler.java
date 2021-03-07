package com.example.demo.admin_bot.service.handler.admin_menu.submenu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.common_part.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class ContactCallbackHandler {
    private final MenuVariables menuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;

    @Autowired
    public ContactCallbackHandler(MenuVariables menuVariables, AdminService adminService, CommonMethods commonMethods) {
        this.menuVariables = menuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
    }

    public BotApiMethod<?> handleContactCallback(CallbackQuery callbackQuery, User admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response;

        if (data.equals(menuVariables.getAdminBtnCallbackSubmenuMyContact()) && admin.getUsername() != null) {
            admin.getAdminChoice().setContact("https://t.me/" + admin.getUsername());
        }

        // Если нажали на любую кнопку, кроме "отмена" - сохраняем изменения данных.
        // Если же нажали "отмена" - все остается без изменений
        if (!data.equals(menuVariables.getAdminBtnCallbackSubmenuCancel())) {
            adminService.saveChoice(admin.getAdminChoice());
        }

        response = commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin);

        // Возвращаем состояние в исходное. Так как в этом подпункте можем как писать, так и нажать на кнопку
        admin.setBotState(admin.getAdminChoice().getIsRentFlat() ? State.ADD_RENT_FLAT : State.ADD_BUY_FLAT);
        adminService.saveAdminState(admin);

        return response;
    }
}
