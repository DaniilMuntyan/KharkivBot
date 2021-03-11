package com.example.demo.user_bot.service.handler.init;

import com.example.demo.common_part.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class InitCategoryCallbackHandler {
    public List<BotApiMethod<?>> handleCallback(CallbackQuery callbackQuery, User user) {
        return null;
    }
}
