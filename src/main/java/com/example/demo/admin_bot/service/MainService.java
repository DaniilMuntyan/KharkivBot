package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.admin_bot.service.handler.CallbackHandler;
import com.example.demo.admin_bot.service.handler.MessageHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MainService {
    private static final Logger LOGGER = Logger.getLogger(MainService.class);
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    @Autowired
    public MainService(MessageHandler messageHandler, CallbackHandler callbackHandler) {
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
    }

    @Async
    public void handleUpdate(Update update, AdminTelegramBot bot) { // Главный обработчик
        try {
            long start = System.currentTimeMillis();
            List<BotApiMethod<?>> methods = new ArrayList<>();
            if (update.hasMessage()) { // Если пришло сообщение
                methods = messageHandler.handleMessage(update.getMessage());
            }
            if (update.hasCallbackQuery()) { // Пришел callback
                methods = callbackHandler.handleCallback(update.getCallbackQuery());
            }
            for (BotApiMethod<?> method : methods) {
                if (method != null) {
                    bot.executeAsync(method);
                }
            }
            LOGGER.info("TIME: " + (double) (System.currentTimeMillis() - start) / 1000);
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception);
        }
    }
}
