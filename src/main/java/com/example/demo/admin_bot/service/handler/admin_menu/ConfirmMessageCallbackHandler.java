package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CommonMethods;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public class ConfirmMessageCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(ConfirmMessageCallbackHandler.class);

    private final AdminMenuVariables adminMenuVariables;
    private final AdminService adminService;
    private final CommonMethods commonMethods;
    private final UserRepository userRepository;
    private final MessagesVariables messagesVariables;
    private final UserBotSendingQueue userBotSendingQueue;

    @Autowired
    public ConfirmMessageCallbackHandler(AdminMenuVariables adminMenuVariables, AdminService adminService, CommonMethods commonMethods, UserRepository userRepository, MessagesVariables messagesVariables, UserBotSendingQueue userBotSendingQueue) {
        this.adminMenuVariables = adminMenuVariables;
        this.adminService = adminService;
        this.commonMethods = commonMethods;
        this.userRepository = userRepository;
        this.messagesVariables = messagesVariables;
        this.userBotSendingQueue = userBotSendingQueue;
    }

    public BotApiMethod<?> handleConfirmCallback(CallbackQuery callbackQuery, UserCache admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> response;

        // ???????? ?????????????????????? ???????????????? ??????????????????
        if(data.equals(adminMenuVariables.getAdminBtnCallbackConfirmMessageYes())) {
            List<User> allUsers = userRepository.findAll();
            for (User user: allUsers) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(callbackQuery.getMessage().getText());
                sendMessage.setEntities(callbackQuery.getMessage().getEntities());
                sendMessage.setChatId(user.getChatId().toString());
                userBotSendingQueue.addBulkMessageToQueue(sendMessage);
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

        // ???????? ???????????????? ???????????????? ?????????????????? - ???????????? ?????????? ?????????????????? ?? ???????????? ???? ????????????

        // ???????????????????? ?????????????????? ?? ????????????????. ?????? ?????? ?? ???????? ?????????????????? ?????????? ?????? ????????????, ?????? ?? ???????????? ???? ????????????
        admin.setBotAdminState(AdminState.ADMIN_INIT);
        adminService.saveAdminState(admin);

        return response;
    }
}
