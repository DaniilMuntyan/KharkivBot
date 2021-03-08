package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CommonMethods;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public class ConfirmMessageCallbackHandler {
    private final MenuVariables menuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;
    private final UserRepository userRepository;
    private final QueryService queryService;
    private final MessagesVariables messagesVariables;

    @Autowired
    public ConfirmMessageCallbackHandler(MenuVariables menuVariables, AdminService adminService, CommonMethods commonMethods, UserRepository userRepository, QueryService queryService, MessagesVariables messagesVariables) {
        this.menuVariables = menuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
        this.userRepository = userRepository;
        this.queryService = queryService;
        this.messagesVariables = messagesVariables;
    }

    public BotApiMethod<?> handleConfirmCallback(CallbackQuery callbackQuery, User admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response;

        // Если подтвердили рассылку сообщения
        if(data.equals(menuVariables.getAdminBtnCallbackConfirmMessageYes())) {
            List<User> allUsers = userRepository.findAll();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(callbackQuery.getMessage().getText());
            sendMessage.setEntities(callbackQuery.getMessage().getEntities());
            for (User user: allUsers) {
                sendMessage.setChatId(user.getChatId().toString());
                queryService.execute(sendMessage, admin);
            }

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId.toString());
            editMessageText.setText(messagesVariables.getAdminConfirmMessageSuccess());
            response = editMessageText;
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId.toString());
            editMessageText.setText(messagesVariables.getAdminConfirmMessageCancel());
            response = editMessageText;
        }

        // Если отменили рассылку сообщения - меняем текст сообщения и ничего не делаем

        // Возвращаем состояние в исходное. Так как в этом подпункте можем как писать, так и нажать на кнопку
        admin.setBotState(State.INIT);
        adminService.saveAdminState(admin);

        return response;
    }
}
