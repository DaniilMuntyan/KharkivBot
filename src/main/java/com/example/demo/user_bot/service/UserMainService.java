package com.example.demo.user_bot.service;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.service.SaveToFileService;
import com.example.demo.user_bot.botapi.RentalTelegramBot;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.service.handler.callback.UserBotCallbackHandler;
import com.example.demo.user_bot.service.handler.message.UserBotMessageHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMainService {
    private static final Logger LOGGER = Logger.getLogger(UserMainService.class);

    private final UserBotMessageHandler userBotMessageHandler;
    private final UserBotCallbackHandler userBotCallbackHandler;

    private final UserBotSendingQueue userBotSendingQueue;
    private final SaveToFileService saveToFileService;
    private final ProgramVariables programVariables;

    @Autowired
    public UserMainService(UserBotMessageHandler userBotMessageHandler, UserBotCallbackHandler userBotCallbackHandler, UserBotSendingQueue userBotSendingQueue, SaveToFileService saveToFileService, ProgramVariables programVariables) {
        this.userBotMessageHandler = userBotMessageHandler;
        this.userBotCallbackHandler = userBotCallbackHandler;
        this.userBotSendingQueue = userBotSendingQueue;
        this.saveToFileService = saveToFileService;
        this.programVariables = programVariables;
    }

    @Async
    public void handleUpdate(Update update, RentalTelegramBot bot) { // Главный обработчик
        try {
            long start = System.currentTimeMillis();
            String s = "";
            List<BotApiMethod<?>> methods = new ArrayList<>();
            if (update.hasMessage()) { // Если пришло сообщение
                methods = userBotMessageHandler.handleMessage(update.getMessage());
                s = update.getMessage().getText();
            }
            if (update.hasCallbackQuery()) { // Пришел callback
                methods = userBotCallbackHandler.handleCallback(update.getCallbackQuery());
                s = update.getCallbackQuery().getData();
            }

            for (BotApiMethod<?> method: methods) {
                //LOGGER.info("method: " + method.getMethod());
                if (method instanceof SendMessage) {
                    userBotSendingQueue.addMessageToQueue((SendMessage) method);
                } else {
                    userBotSendingQueue.addMethodToQueue(method);
                }
            }

            LOGGER.info("USER TIME: " + (double) (System.currentTimeMillis() - start));
            this.saveToFileService.writeTime(programVariables.getUserTimePath(), s,
                    (double) (System.currentTimeMillis() - start));
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception);
        }
    }
}
