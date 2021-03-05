package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.admin_bot.service.handler.CallbackHandlerService;
import com.example.demo.admin_bot.service.handler.MessageHandlerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    private static final Logger LOGGER = Logger.getLogger(MainService.class);
    private final MessageHandlerService messageHandlerService;
    private final CallbackHandlerService callbackHandlerService;

    @Autowired
    public MainService(MessageHandlerService messageHandlerService, CallbackHandlerService callbackHandlerService) {
        this.messageHandlerService = messageHandlerService;
        this.callbackHandlerService = callbackHandlerService;
    }

    @Async
    public void handleUpdate(Update update, AdminTelegramBot bot) { // Главный обработчик
        try {
            long start = System.currentTimeMillis();
            List<BotApiMethod<?>> methods = new ArrayList<>();
            if (update.hasMessage()) { // Если пришло сообщение
                LOGGER.info("MESSAGE: " + update.getMessage().getText());
                methods = messageHandlerService.handleMessage(update.getMessage());
            }
            if (update.hasCallbackQuery()) { // Пришел callback
                methods = callbackHandlerService.handleCallback(update.getCallbackQuery());
            }
            for (BotApiMethod<?> method : methods) {
                if (method != null) {
                    bot.executeAsync(method);
                }
            }
            LOGGER.info("TIME: " + (double) (System.currentTimeMillis() - start));
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception);
        }
    }
}
