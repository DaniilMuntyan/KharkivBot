package com.example.demo.admin_bot.service;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.service.SaveToFileService;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMainService {
    private static final Logger LOGGER = Logger.getLogger(AdminMainService.class);
    private final AdminBotMessageHandler adminBotMessageHandler;
    private final AdminBotCallbackHandler adminBotCallbackHandler;
    private final SaveToFileService saveToFileService;
    private final ProgramVariables programVariables;

    @Autowired
    public AdminMainService(AdminBotMessageHandler adminBotMessageHandler, AdminBotCallbackHandler adminBotCallbackHandler, SaveToFileService saveToFileService, ProgramVariables programVariables) {
        this.adminBotMessageHandler = adminBotMessageHandler;
        this.adminBotCallbackHandler = adminBotCallbackHandler;
        this.saveToFileService = saveToFileService;
        this.programVariables = programVariables;
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
            this.saveToFileService.writeTime(programVariables.getAdminTimePath(), s,
                    (double) (System.currentTimeMillis() - start));
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error(exception);
        }
    }
}
