package com.example.demo.admin_bot.service;

import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.admin_bot.service.handler.AdminBotCallbackHandler;
import com.example.demo.admin_bot.service.handler.AdminBotMessageHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMainService {
    private static final Logger LOGGER = Logger.getLogger(AdminMainService.class);
    private final AdminBotMessageHandler adminBotMessageHandler;
    private final AdminBotCallbackHandler adminBotCallbackHandler;
    private final UserBotSendingQueue userBotSendingQueue;

    @Autowired
    public AdminMainService(AdminBotMessageHandler adminBotMessageHandler, AdminBotCallbackHandler adminBotCallbackHandler, UserBotSendingQueue userBotSendingQueue) {
        this.adminBotMessageHandler = adminBotMessageHandler;
        this.adminBotCallbackHandler = adminBotCallbackHandler;
        this.userBotSendingQueue = userBotSendingQueue;
    }

    @Async
    public void handleUpdate(Update update, AdminTelegramBot bot) { // Главный обработчик
        try {
            long start = System.currentTimeMillis();
            String s = "";
            List<BotApiMethod<?>> methods = new ArrayList<>();
            if (update.hasMessage()) { // Если пришло сообщение
                methods = adminBotMessageHandler.handleMessage(update.getMessage());
                s = update.getMessage().getText();
            }
            if (update.hasCallbackQuery()) { // Пришел callback
                methods = adminBotCallbackHandler.handleCallback(update.getCallbackQuery());
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

    private synchronized void write(String s, double time) {
        File csvFile = new File("./files/timeAdminBot.csv");
        try {
            csvFile.getParentFile().mkdirs();
            csvFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(csvFile, true)) {

            String sb = "\n" + s + "," + time;
            writer.write(sb);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
