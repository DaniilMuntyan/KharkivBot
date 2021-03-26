package com.example.demo.user_bot.botapi;

import com.example.demo.user_bot.service.UserMainService;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@PropertySource("classpath:application.properties")
@RestController
public class RentalTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = Logger.getLogger(RentalTelegramBot.class);

    private final UserMainService userMainService;

    @Value("${bot1.name}")
    private String botName;

    @Value("${bot1.token}")
    private String botToken;

    @Autowired
    public RentalTelegramBot(UserMainService userMainService) {
        this.userMainService = userMainService;
        LOGGER.info("RentalTelegramBot is creating...");
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
        userMainService.handleUpdate(update, this);
    }
}
