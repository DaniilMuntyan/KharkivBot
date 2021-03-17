package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.service.publishing.FlatMessageService;
import com.example.demo.user_bot.service.publishing.SendFoundFlatsService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public final class SeeOthersCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(SeeOthersCallbackHandler.class);

    private final DataCache dataCache;
    private final ProgramVariables programVariables;
    private final UserBotSendingQueue userBotSendingQueue;
    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final FlatMessageService flatMessageService;
    private final SendFoundFlatsService sendFoundFlatsService;

    @Autowired
    public SeeOthersCallbackHandler(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FlatMessageService flatMessageService, SendFoundFlatsService sendFoundFlatsService) {
        this.dataCache = dataCache;
        this.programVariables = programVariables;
        this.userBotSendingQueue = userBotSendingQueue;
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.flatMessageService = flatMessageService;
        this.sendFoundFlatsService = sendFoundFlatsService;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        if (callbackQuery.getData().equals(userMenuVariables.getUserNotAllBtnSeeOthersCallback())) { // Нажали "Показать еще"
            response.add(this.deleteApiMethod(callbackQuery.getMessage())); // Удаляю сообщение "Показать еще"
            if (user.getUserChoice().getIsRentFlat()) {
                this.sendFoundFlatsService.sendNotSentRentFlats(user);
            } else {
                this.sendFoundFlatsService.sendNotSentBuyFlats(user);
            }
            this.dataCache.markNotSaved(user.getChatId()); // Помечаю юзера несохраненным - чтобы обновить в базе
            //this.dataCache.saveUser(user); // Сохраняю изменения юзера
        }
        // Если нажали "Достаточно", убираем кнопки и меняем текст сообщения
        if (callbackQuery.getData().equals(userMenuVariables.getUserBotNotAllBtnEnoughCallback())) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(user.getChatId().toString());
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(messagesVariables.getUserEnoughText());
            response.add(editMessageText); // Удаляю сообщение "Показать еще"
        }
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }
}
