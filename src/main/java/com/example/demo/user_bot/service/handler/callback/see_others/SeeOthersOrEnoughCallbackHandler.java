package com.example.demo.user_bot.service.handler.callback.see_others;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.UserCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class SeeOthersOrEnoughCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(SeeOthersOrEnoughCallbackHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final ShowOrEnoughService showOrEnoughService;

    @Autowired
    public SeeOthersOrEnoughCallbackHandler(UserMenuVariables userMenuVariables, ShowOrEnoughService showOrEnoughService) {
        this.userMenuVariables = userMenuVariables;
        this.showOrEnoughService = showOrEnoughService;
    }

    public void handleCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        // Нажали "Показать еще"
        if (callbackQuery.getData().equals(userMenuVariables.getUserNotAllBtnSeeOthersCallback())) {
            this.showOrEnoughService.more(response, callbackQuery, user);
        }

        // Если нажали "Достаточно", убираем кнопки и меняем текст сообщения
        if (callbackQuery.getData().equals(userMenuVariables.getUserBotNotAllBtnEnoughCallback())) {
            this.showOrEnoughService.enough(response, callbackQuery.getMessage().getMessageId(), user);
        }
    }
}
