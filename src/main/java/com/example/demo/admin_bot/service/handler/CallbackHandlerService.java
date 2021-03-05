package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.AdminMenuCallbackHandlerService;
import com.example.demo.admin_bot.service.handler.admin_menu.rooms.RoomsCallbackHandlerService;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public final class CallbackHandlerService {
    private static final Logger LOGGER = Logger.getLogger(CallbackHandlerService.class);

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final MenuVariables menuVariables;
    private final MessagesVariables messagesVariables;

    private final AdminMenuCallbackHandlerService adminMenuCallbackHandlerService;
    private final RoomsCallbackHandlerService roomsCallbackHandlerService;

    @Autowired
    public CallbackHandlerService(AdminService adminService, UserRepository userRepository, MenuVariables menuVariables, MessagesVariables messagesVariables, AdminMenuCallbackHandlerService adminMenuCallbackHandlerService, RoomsCallbackHandlerService roomsCallbackHandlerService) {
        this.adminService = adminService;
        this.userRepository = userRepository;
        this.menuVariables = menuVariables;
        this.messagesVariables = messagesVariables;
        this.adminMenuCallbackHandlerService = adminMenuCallbackHandlerService;
        this.roomsCallbackHandlerService = roomsCallbackHandlerService;
    }

    // Обработка callback'а
    public List<BotApiMethod<?>> handleCallback(CallbackQuery callback) {
        LOGGER.info("CALLBACK: " + callback.getId());
        Long chatId = callback.getMessage().getChatId();
        String data = callback.getData();
        Integer messageId = callback.getMessage().getMessageId();

        Optional<User> user = userRepository.findByChatId(chatId);

        BotApiMethod<?> response = null;

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            response = handleAdminCallback(callback, user.get());
        }

        // Если ответ - это кастомный AnswerCallbackQuery, то возвращаем только его
        if (response instanceof AnswerCallbackQuery) {
            // Если получили forbidden - удалить сообщение, потому что только одно меню может работать в этот момент времени
            if (((AnswerCallbackQuery) response).getText().equals(messagesVariables.getAdminMenuForbidden())) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(chatId.toString());
                deleteMessage.setMessageId(messageId);
                return Arrays.asList(deleteMessage, response);
            }
            return Collections.singletonList(response);
        } else {
            return Arrays.asList(adminService.getAnswerCallback(callback), response);
        }
    }

    // Callback пришел от админа
    private BotApiMethod<?> handleAdminCallback(CallbackQuery callbackQuery, User admin) {
        BotApiMethod<?> response = null;

        // Только одно меню может работать в данный момент времени.
        boolean forbidden = admin.getAdminChoice().getMenuMessageId() != null &&
                !admin.getAdminChoice().getMenuMessageId().equals(callbackQuery.getMessage().getMessageId());
        if(forbidden) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            answerCallbackQuery.setShowAlert(true);
            answerCallbackQuery.setText(messagesVariables.getAdminMenuForbidden());
            return answerCallbackQuery;
        }

        if(callbackQuery.getData().startsWith("ADMIN_MENU")) {
            response = adminMenuCallbackHandlerService.handleAdminMenuCallback(callbackQuery, admin);
        }

        if(callbackQuery.getData().startsWith("ROOMS")) {
            response = roomsCallbackHandlerService.handleRoomCallback(callbackQuery, admin);
        }

        return response;
    }
}
