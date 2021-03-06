package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.AdminMenuCallbackHandlerService;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CancelSubmenuHandler;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.RoomsCallbackHandlerService;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;

@Service
public final class CallbackHandlerService {
    private static final Logger LOGGER = Logger.getLogger(CallbackHandlerService.class);

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final MenuVariables menuVariables;
    private final MessagesVariables messagesVariables;

    private final AdminMenuCallbackHandlerService adminMenuCallbackHandlerService;
    private final RoomsCallbackHandlerService roomsCallbackHandlerService;
    private final CancelSubmenuHandler cancelSubmenuHandler;

    @Autowired
    public CallbackHandlerService(AdminService adminService, UserRepository userRepository, MenuVariables menuVariables, MessagesVariables messagesVariables, AdminMenuCallbackHandlerService adminMenuCallbackHandlerService, RoomsCallbackHandlerService roomsCallbackHandlerService, CancelSubmenuHandler cancelSubmenuHandler) {
        this.adminService = adminService;
        this.userRepository = userRepository;
        this.menuVariables = menuVariables;
        this.messagesVariables = messagesVariables;
        this.adminMenuCallbackHandlerService = adminMenuCallbackHandlerService;
        this.roomsCallbackHandlerService = roomsCallbackHandlerService;
        this.cancelSubmenuHandler = cancelSubmenuHandler;
    }

    // Обработка callback'а
    public List<BotApiMethod<?>> handleCallback(CallbackQuery callback) {
        LOGGER.info("CALLBACK: " + callback.getId());
        Long chatId = callback.getMessage().getChatId();
        String data = callback.getData();
        Integer messageId = callback.getMessage().getMessageId();

        Optional<User> user = userRepository.findByChatId(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            response.addAll(handleAdminCallback(callback, user.get()));
        }

        // Если ответ - это кастомный AnswerCallbackQuery, то возвращаем только его
        if (response.size() == 1 && response.get(0) instanceof AnswerCallbackQuery) {
            // Если получили forbidden - удалить сообщение, потому что только одно меню может работать в этот момент времени
            if (((AnswerCallbackQuery) response.get(0)).getText().equals(messagesVariables.getAdminMenuForbidden())) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(chatId.toString());
                deleteMessage.setMessageId(messageId);

                response.add(0, deleteMessage);
            }
        } else {
            response.add(0, adminService.getAnswerCallback(callback));
        }
        return response;
    }

    // Callback пришел от админа
    private List<BotApiMethod<?>> handleAdminCallback(CallbackQuery callbackQuery, User admin) {
        List<BotApiMethod<?>> response = new ArrayList<>();

        // Только одно меню может работать в данный момент времени.
        boolean forbidden = admin.getAdminChoice().getMenuMessageId() != null &&
                !admin.getAdminChoice().getMenuMessageId().equals(callbackQuery.getMessage().getMessageId());
        if(forbidden) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            answerCallbackQuery.setShowAlert(true);
            answerCallbackQuery.setText(messagesVariables.getAdminMenuForbidden());
            response.add(answerCallbackQuery);
            return response;
        }

        if(callbackQuery.getData().startsWith("SUBMENU")) {
            response.add(cancelSubmenuHandler.handleRoomCallback(callbackQuery, admin));
        }

        if(callbackQuery.getData().startsWith("ADMIN_MENU")) {
            response.addAll(adminMenuCallbackHandlerService.handleAdminMenuCallback(callbackQuery, admin));
        }

        if(callbackQuery.getData().startsWith("ROOMS")) {
            response.add(roomsCallbackHandlerService.handleRoomCallback(callbackQuery, admin));
        }

        return response;
    }
}
