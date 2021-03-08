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

import java.io.*;
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
            String s = "";
            List<BotApiMethod<?>> methods = new ArrayList<>();
            if (update.hasMessage()) { // Если пришло сообщение
                methods = messageHandler.handleMessage(update.getMessage());
                s = update.getMessage().getText();
            }
            if (update.hasCallbackQuery()) { // Пришел callback
                methods = callbackHandler.handleCallback(update.getCallbackQuery());
                s = update.getCallbackQuery().getData();
            }
            for (BotApiMethod<?> method : methods) {
                if (method != null) {
                    bot.executeAsync(method);
                }
            }
            LOGGER.info("TIME: " + (double) (System.currentTimeMillis() - start));
            write(s, (double) (System.currentTimeMillis() - start));
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception);
        }
    }

    private void write(String s, double time) {
        try (FileWriter writer = new FileWriter("time.csv", true)) {

            String sb = "\n" + s + "," + time;
            writer.write(sb);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
