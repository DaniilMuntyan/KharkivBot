package com.example.demo.user_bot.service.handler;

import com.example.demo.common_part.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class UserBotCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(UserBotCallbackHandler.class);

    public List<BotApiMethod<?>> handleCallback(CallbackQuery callbackQuery, User user) {
        return null;
    }
}
