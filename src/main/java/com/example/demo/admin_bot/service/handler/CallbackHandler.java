package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.service.handler.admin_menu.AdminMenuCallbackHandler;
import com.example.demo.admin_bot.service.handler.admin_menu.ConfirmMessageCallbackHandler;
import com.example.demo.admin_bot.service.handler.admin_menu.ConfirmPublishCallbackHandler;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.*;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;

@Service
public final class CallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(CallbackHandler.class);

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final MessagesVariables messagesVariables;
    private final MenuVariables menuVariables;

    private final AdminMenuCallbackHandler adminMenuCallbackHandler;
    private final RoomsCallbackHandler roomsCallbackHandler;
    private final DistrictCallbackHandler districtCallbackHandler;
    private final CancelSubmenuHandler cancelSubmenuHandler;
    private final MoneyRangeCallbackHandler moneyRangeCallbackHandler;
    private final ContactCallbackHandler contactCallbackHandler;
    private final ConfirmMessageCallbackHandler confirmMessageCallbackHandler;
    private final ConfirmPublishCallbackHandler confirmPublishCallbackHandler;

    @Autowired
    public CallbackHandler(AdminService adminService, UserRepository userRepository, MessagesVariables messagesVariables, MenuVariables menuVariables, AdminMenuCallbackHandler adminMenuCallbackHandler, RoomsCallbackHandler roomsCallbackHandler, DistrictCallbackHandler districtCallbackHandler, CancelSubmenuHandler cancelSubmenuHandler, MoneyRangeCallbackHandler moneyRangeCallbackHandler, ContactCallbackHandler contactCallbackHandler, ConfirmMessageCallbackHandler confirmMessageCallbackHandler, ConfirmPublishCallbackHandler confirmPublishCallbackHandler) {
        this.adminService = adminService;
        this.userRepository = userRepository;
        this.messagesVariables = messagesVariables;
        this.menuVariables = menuVariables;
        this.adminMenuCallbackHandler = adminMenuCallbackHandler;
        this.roomsCallbackHandler = roomsCallbackHandler;
        this.districtCallbackHandler = districtCallbackHandler;
        this.cancelSubmenuHandler = cancelSubmenuHandler;
        this.moneyRangeCallbackHandler = moneyRangeCallbackHandler;
        this.contactCallbackHandler = contactCallbackHandler;
        this.confirmMessageCallbackHandler = confirmMessageCallbackHandler;
        this.confirmPublishCallbackHandler = confirmPublishCallbackHandler;
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

        LOGGER.info("CALLBACK DATA: " + callbackQuery.getData());

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

        // Кнопка "отмена"
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackSubmenuCancelPrefix())) {
            response.add(cancelSubmenuHandler.handleCancelSubmenuCallback(callbackQuery, admin));
        }

        // Кнопки главного меню (кол-во комнат, площадь и тд)
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackMenuPrefix())) {
            response.addAll(adminMenuCallbackHandler.handleAdminMenuCallback(callbackQuery, admin));
        }

        // Кнопки подпункта меню "Кол-во комнат"
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackSubmenuRoomsPrefix())) {
            response.add(roomsCallbackHandler.handleRoomCallback(callbackQuery, admin));
        }

        // Кнопки подпункта меню "Район"
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackSubmenuDistrictPrefix())) {
            response.add(districtCallbackHandler.handleDistrictCallback(callbackQuery, admin));
        }

        // Кнопки подпункта меню "Бюджет"
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackSubmenuRangePrefix())) {
            response.add(moneyRangeCallbackHandler.handleMoneyRangeCallback(callbackQuery, admin));
        }

        // Кнопка подпункта "Контакт"
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackSubmenuContactPrefix())) {
            response.add(contactCallbackHandler.handleContactCallback(callbackQuery, admin));
        }

        // Кнопки подтверждения отсылки сообщения всем пользователям
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackConfirmPrefix())) {
            response.add(confirmMessageCallbackHandler.handleConfirmCallback(callbackQuery, admin));
        }

        // Кнопки подтверждения публикации квартиры
        if(callbackQuery.getData().startsWith(menuVariables.getAdminBtnCallbackPublishPrefix())) {
            response.addAll(confirmPublishCallbackHandler.handlePublishConfirm(callbackQuery, admin));
        }

        return response;
    }
}
