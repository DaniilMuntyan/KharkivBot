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
import com.example.demo.user_bot.service.publishing.ShowOrEnoughService;
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
    private final ShowOrEnoughService showOrEnoughService;

    @Autowired
    public SeeOthersOrEnoughCallbackHandler(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FlatMessageService flatMessageService, SendFoundFlatsService sendFoundFlatsService, ShowOrEnoughService showOrEnoughService) {
        this.dataCache = dataCache;
        this.programVariables = programVariables;
        this.userBotSendingQueue = userBotSendingQueue;
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.flatMessageService = flatMessageService;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.showOrEnoughService = showOrEnoughService;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Нажали "Показать еще"
        if (callbackQuery.getData().equals(userMenuVariables.getUserNotAllBtnSeeOthersCallback())) {
            this.showOrEnoughService.more(response, callbackQuery, user);
        }

        // Если нажали "Достаточно", убираем кнопки и меняем текст сообщения
        if (callbackQuery.getData().equals(userMenuVariables.getUserBotNotAllBtnEnoughCallback())) {
            this.showOrEnoughService.enough(response, callbackQuery.getMessage().getMessageId(), user);
        }
    }
}
