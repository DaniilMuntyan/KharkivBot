package com.example.demo.common_part.service;

import com.example.demo.admin_bot.service.AdminBotStateService;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.service.state_handler.UserBotStateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotStateService {
    private static final Logger LOGGER = Logger.getLogger(BotStateService.class);

    private final AdminBotStateService adminBotStateService;
    private final UserBotStateService userBotStateService;

    // В некоторых методах возможен Exception, поэтому не всегда надо возвращаться в предыдущее состояние
    private boolean notBack;

    @Autowired
    public BotStateService(AdminBotStateService adminBotStateService, UserBotStateService userBotStateService) {
        this.adminBotStateService = adminBotStateService;
        this.userBotStateService = userBotStateService;
    }

    public List<BotApiMethod<?>> processInput(Message message, User user, boolean isAdminBot) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        if (isAdminBot) {
            answer.addAll(this.adminBotStateService.processAdminInput(message, user));
        } else {
            answer.addAll(this.userBotStateService.processUserInput(message, user));
        }

        return answer;
    }
}