package com.example.demo.user_bot.service.handler.callback.flat;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.ErrorMessageService;
import com.example.demo.user_bot.utils.MenuSendMessage;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public final class FlatButtonCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(FlatButtonCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final ErrorMessageService errorMessageService;
    private final DataCache dataCache;
    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public FlatButtonCallbackHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, ErrorMessageService errorMessageService, DataCache dataCache, KeyboardsRegistry keyboardsRegistry) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.errorMessageService = errorMessageService;
        this.dataCache = dataCache;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    // Обрабатываю запрос. Кнопка "Хочу посмотреть"
    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        String data = callbackQuery.getData();

        if (user.getUserChoice().getMenuMessageId() != null) { // Если открыто меню - закрываю
            // Удаляю прошлое меню
            response.add(this.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
            user.getUserChoice().setMenuMessageId(null);
            this.dataCache.saveUserCache(user);
        }

        // Если не указал телефон, и юзернейма нет
        if (user.getPhone() == null && user.getUsername() == null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(messagesVariables.getUserNoPhoneText());
            sendMessage.setChatId(user.getChatId().toString());
            sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(sendMessage);

            user.setBotUserState(UserState.MENU1); // Перешли в главное меню
            this.dataCache.saveUserCache(user);
        } else { // Если можем связаться с юзером
            try {
                // Достаю айдишник квартиры, которую хотят посмотреть
                Integer flatId = Integer.valueOf(data.substring(data.lastIndexOf("_") + 1));
                MenuSendMessage confirmMessage = new MenuSendMessage(); // Сообщение для подтверждения записи на просмотр
                confirmMessage.setChangeMenuMessageId(true); // Меняю айдишник меню для юзера
                confirmMessage.setChatId(user.getChatId().toString());
                confirmMessage.setText(messagesVariables.getUserBotConfirmSeeingText(flatId.toString()));
                // Если хочет посмотреть квартира под аренду
                if (data.startsWith(userMenuVariables.getUserBotFlatMsgSeeRentCallbackPrefix())) {
                    confirmMessage.setReplyMarkup(keyboardsRegistry.getMenuSeeFlatKeyboard().getKeyboard(true, flatId));
                } else { // Если хочет посмотреть квартиру под продажу
                    confirmMessage.setReplyMarkup(keyboardsRegistry.getMenuSeeFlatKeyboard().getKeyboard(false, flatId));
                }
                response.add(confirmMessage);

                user.setBotUserState(UserState.CONFIRMING_SEEING); // Перехожу в состояние подтверждения записи на просмотр
                this.dataCache.saveUserCache(user);

            } catch(NumberFormatException ex) {
                // Отсылаю сообщение об ошибке
                SendMessage error = this.errorMessageService.getUserError(user.getChatId());
                error.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
                response.add(error);
                user.setBotUserState(UserState.MENU1); // Перевожу в главное меню
                this.dataCache.saveUserCache(user);

                LOGGER.error(ex);
                ex.printStackTrace();
            }
        }
    }

    private DeleteMessage deleteApiMethod(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }
}