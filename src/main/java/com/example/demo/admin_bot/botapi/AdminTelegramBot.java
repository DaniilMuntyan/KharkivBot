package com.example.demo.admin_bot.botapi;

import com.example.demo.admin_bot.service.MainService;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@PropertySource("classpath:application.properties")
@RestController
public class AdminTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = Logger.getLogger(AdminTelegramBot.class);

    @Value("${bot2.name}")
    private String botName;

    @Value("${bot2.token}")
    private String botToken;

    private final MainService mainService;

    @Autowired
    public AdminTelegramBot(MainService mainService) {
        this.mainService = mainService;
        LOGGER.info("AdminTelegramBot is creating...");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        mainService.handleUpdate(update, this);
    }
}
