package com.example.demo.user_bot.service.handler.callback.flat;

import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.Look;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.DeleteMessageService;
import com.example.demo.user_bot.service.ErrorMessageService;
import com.example.demo.user_bot.service.entities.LookService;
import com.example.demo.user_bot.service.handler.message.menu.BackToMenu1;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public final class ConfirmSeeingCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(ConfirmSeeingCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final ErrorMessageService errorMessageService;
    private final DataCache dataCache;
    private final KeyboardsRegistry keyboardsRegistry;
    private final DeleteMessageService deleteMessageService;
    private final BackToMenu1 backToMenu1;
    private final LookService lookService;

    @Autowired
    public ConfirmSeeingCallbackHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, ErrorMessageService errorMessageService, DataCache dataCache, KeyboardsRegistry keyboardsRegistry, DeleteMessageService deleteMessageService, BackToMenu1 backToMenu1, LookService lookService) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.errorMessageService = errorMessageService;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
        this.deleteMessageService = deleteMessageService;
        this.backToMenu1 = backToMenu1;
        this.lookService = lookService;
    }

    // Обрабатываю запрос одной из кнопок меню подтверждения запроса на просмотр квартиры
    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        // Если отменяет запрос на просмотр
        if (data.equals(userMenuVariables.getUserConfirmSeeingCancelCallback())) {
            // Удаляю сообщение подтверждение запроса
            response.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), callbackQuery.getMessage().getMessageId()));
            user.getUserChoice().setMenuMessageId(null); // Удаляю меню
            response.add(this.backToMenu1.back(user)); // Возвращаю в главное меню
            this.dataCache.saveUserCache(user);
            return;
        }

        int flatId;
        try {
            // Достаю айдишник квартиры с callbackData
            flatId = Integer.parseInt(data.substring(data.lastIndexOf("_") + 1));
        } catch (NumberFormatException exception) {
            // Удаляю инлайн клавиатуру подтверждения запроса на просмотр квартиры
            response.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), callbackQuery.getMessage().getMessageId()));
            // Отсылаю сообщение об ошибке
            SendMessage error = this.errorMessageService.getUserError(user.getChatId());
            error.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(error);
            user.setBotUserState(UserState.MENU1); // Перевожу в главное меню
            this.dataCache.saveUserCache(user);
            LOGGER.error(exception);
            exception.printStackTrace();
            return; // Выхожу. Так как ошибка парсинга callback'а
        }

        Look newLook = this.getNewLook(user, flatId); // Создаю новый просмотр, чтобы сохранить в базу
        // Если хочет записаться на просмотр квартиры под аренду
        if (data.startsWith(userMenuVariables.getUserConfirmSeeingRentCallbackPrefix())) {
            newLook.setIsRentFlat(true); // Смотрим квартиру под аренду
            newLook = this.lookService.save(newLook); // Сохраняю просмотр в базу
            List<UserCache> admins = this.dataCache.findAllAdmins(); // Достаю всех админов
            AdminTelegramBot adminBot = BeanUtil.getBean(AdminTelegramBot.class); // Беру админ бота
            try {
                for (UserCache temp : admins) { // Всем админам отсылаю сообщение о новой заявке на просмотр
                    adminBot.executeAsync(this.getUserToSeeMessage(true, temp.getChatId(), newLook));
                }
                response.add(this.getSuccessMessage(user, callbackQuery.getMessage().getMessageId())); // Сообщение об успешном выполнении
                user.setBotUserState(UserState.MENU1); // Возвращаю в главное меню
                this.dataCache.saveUserCache(user);
            } catch(TelegramApiException e) {
                LOGGER.error(e);
                e.printStackTrace();
            }
        }
        // Если хочет записаться на просмотр квартиры под продажу
        if (data.startsWith(userMenuVariables.getUserConfirmSeeingBuyCallbackPrefix())) {
            newLook.setIsRentFlat(false); // Смотрим квартиру под продажу
            newLook = this.lookService.save(newLook); // Сохраняю просмотр в базу
            List<UserCache> admins = this.dataCache.findAllAdmins(); // Достаю всех админов
            AdminTelegramBot adminBot = BeanUtil.getBean(AdminTelegramBot.class); // Беру админ бота
            try {
                for (UserCache temp : admins) { // Всем админам отсылаю сообщение о новой заявке на просмотр
                    adminBot.executeAsync(this.getUserToSeeMessage(false, temp.getChatId(), newLook));
                }
                response.add(this.getSuccessMessage(user, callbackQuery.getMessage().getMessageId())); // Сообщение об успешном выполнении
                user.setBotUserState(UserState.MENU1); // Возвращаю в главное меню
                this.dataCache.saveUserCache(user);
            } catch(TelegramApiException e) {
                LOGGER.error(e);
                e.printStackTrace();
            }
        }
    }

    private Look getNewLook(UserCache user, Integer flatId) {
        return Look.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUsername())
                .phone(user.getPhone())
                .flatId(flatId)
                .build();
    }

    private EditMessageText getSuccessMessage(UserCache user, Integer messageId) {
        EditMessageText confirmSuccess = new EditMessageText();
        confirmSuccess.setText(messagesVariables.getUserConfirmedSeeingText());
        confirmSuccess.setMessageId(messageId);
        confirmSuccess.setChatId(user.getChatId().toString());
        return confirmSuccess;
    }

    private SendMessage getUserToSeeMessage(boolean isRentFlat, Long adminChatId, Look newLook) {
        SendMessage toAdmins = new SendMessage();
        if (isRentFlat) {
            toAdmins.setText(messagesVariables.getAdminUserWantsToSeeRentText(newLook.getFlatId().toString() +
                    messagesVariables.getAdminBotHashtagRent()) +
                    newLook.toString());
        } else {
            toAdmins.setText(messagesVariables.getAdminUserWantsToSeeBuyText(newLook.getFlatId().toString() +
                    messagesVariables.getAdminBotHashtagBuy()) +
                    newLook.toString());
        }
        toAdmins.setChatId(adminChatId.toString());
        return toAdmins;
    }
}