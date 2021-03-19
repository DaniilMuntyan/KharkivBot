package com.example.demo.user_bot.service.handler.callback;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public final class SeeOthersOrEnoughCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(SeeOthersOrEnoughCallbackHandler.class);

    private final DataCache dataCache;
    private final ProgramVariables programVariables;
    private final UserBotSendingQueue userBotSendingQueue;
    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final FlatMessageService flatMessageService;
    private final SendFoundFlatsService sendFoundFlatsService;

    @Autowired
    public SeeOthersOrEnoughCallbackHandler(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FlatMessageService flatMessageService, SendFoundFlatsService sendFoundFlatsService) {
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
            response.add(this.deleteApiMethod(callbackQuery.getMessage())); // Удаляю сообщение "Показать еще"
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(user.getChatId().toString());
            sendMessage.setText(messagesVariables.getUserEnoughText());
            sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());

            user.setBotUserState(UserState.MENU1); // Перешли в главное меню
            this.dataCache.markNotSaved(user.getChatId());

            response.add(sendMessage);
        }
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }
}
