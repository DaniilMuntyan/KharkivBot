package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CommonMethods;
import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.service.publishing.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ConfirmPublishCallbackHandler {
    private final AdminMenuVariables adminMenuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;
    private final UserRepository userRepository;
    private final MessagesVariables messagesVariables;

    private final PublishingService publishingService;

    @Autowired
    public ConfirmPublishCallbackHandler(AdminMenuVariables adminMenuVariables, AdminService adminService, CommonMethods commonMethods, UserRepository userRepository, MessagesVariables messagesVariables, PublishingService publishingService) {
        this.adminMenuVariables = adminMenuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
        this.userRepository = userRepository;
        this.messagesVariables = messagesVariables;
        this.publishingService = publishingService;
    }

    public List<BotApiMethod<?>> handlePublishConfirm(CallbackQuery callbackQuery, User admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        List<BotApiMethod<?>> response = new ArrayList<>();

        // Если подтвердили публикацию
        if(data.equals(adminMenuVariables.getAdminBtnCallbackSubmenuConfirmYes())) {
            // Передаю параметром response - для публикации в канал
            // Если была ошибка при отправлении - вернется "ERROR", если нет - айдишник квартиры
            String result = publishingService.publish(admin, response);

            if (!result.equals("ERROR")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId.toString());
                editMessageText.setText(messagesVariables.getAdminConfirmPublishSuccess(result));
                response.add(editMessageText);

                // Возвращаем состояние в исходное
                admin.setBotAdminState(AdminState.ADMIN_INIT);
                adminService.setAdminChoice(admin, new AdminChoice()); // Обновляем выбор
            }
        } else { // Если отменяем публикацию
            response.add(commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin));
            // Отмена публикации - возвращаемся обратно
            admin.setBotAdminState(admin.getAdminChoice().getIsRentFlat() ?
                    AdminState.ADMIN_ADD_RENT_FLAT : AdminState.ADMIN_ADD_BUY_FLAT);
        }

        adminService.saveAdminState(admin); // Сохраняю измененное состояние админа

        return response;
    }

}
