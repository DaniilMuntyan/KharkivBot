package com.example.demo.user_bot.service;

import com.example.demo.user_bot.botapi.RentalTelegramBot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public final class QueryService {
    private static final Logger LOGGER = Logger.getLogger(QueryService.class);

    private final RentalTelegramBot rentalTelegramBot;

    @Autowired
    public QueryService(RentalTelegramBot rentalTelegramBot) {
        this.rentalTelegramBot = rentalTelegramBot;
    }

    public void execute(BotApiMethod<?> method) {
        LOGGER.info("rentalTelegramBot: " + rentalTelegramBot);
        try {
            LOGGER.info("executeAsync. Method: " + method);
            this.rentalTelegramBot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
    }

}
