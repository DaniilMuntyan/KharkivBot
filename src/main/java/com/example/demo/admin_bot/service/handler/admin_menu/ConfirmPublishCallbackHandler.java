package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CommonMethods;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.common_part.model.AdminChoice;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.service.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ConfirmPublishCallbackHandler {
    private final MenuVariables menuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;
    private final UserRepository userRepository;
    private final MessagesVariables messagesVariables;

    private final PublishingService publishingService;

    @Autowired
    public ConfirmPublishCallbackHandler(MenuVariables menuVariables, AdminService adminService, CommonMethods commonMethods, UserRepository userRepository, MessagesVariables messagesVariables, PublishingService publishingService) {
        this.menuVariables = menuVariables;
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
        if(data.equals(menuVariables.getAdminBtnCallbackSubmenuConfirmYes())) {
            // Передаю параметром response - для публикации в канал
            String flatNumber = publishingService.publish(admin, response);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId.toString());
            editMessageText.setText(messagesVariables.getAdminConfirmPublishSuccess(flatNumber));
            response.add(editMessageText);

            // Возвращаем состояние в исходное
            admin.setBotState(State.INIT);
            adminService.setAdminChoice(admin, new AdminChoice()); // Обновляем выбор
        } else {
            response.add(commonMethods.getEditNewFlatKeyboard(chatId, messageId, admin));

            // Отмена публикации - возвращаемся обратно
            admin.setBotState(admin.getAdminChoice().getIsRentFlat() ?
                    State.ADD_RENT_FLAT : State.ADD_BUY_FLAT);
        }

        adminService.saveAdminState(admin); // Сохраняю измененное состояние админа

        return response;
    }

}
