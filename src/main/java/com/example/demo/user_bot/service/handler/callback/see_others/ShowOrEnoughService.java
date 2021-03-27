package com.example.demo.user_bot.service.handler.callback.see_others;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.DeleteMessageService;
import com.example.demo.user_bot.service.searching.SendFoundFlatsService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public final class ShowOrEnoughService {
    private static final Logger LOGGER = Logger.getLogger(ShowOrEnoughService.class);

    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final DataCache dataCache;
    private final SendFoundFlatsService sendFoundFlatsService;
    private final DeleteMessageService deleteMessageService;

    @Autowired
    public ShowOrEnoughService(MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, DataCache dataCache, SendFoundFlatsService sendFoundFlatsService, DeleteMessageService deleteMessageService) {
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.dataCache = dataCache;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.deleteMessageService = deleteMessageService;
    }

    // Нажали на "Показать еще"
    public void more(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        response.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), callbackQuery.getMessage().getMessageId())); // Удаляю сообщение "Показать еще"
        if (user.getUserChoice().getIsRentFlat()) {
            this.sendFoundFlatsService.sendFoundRentFlats(user);
        } else {
            this.sendFoundFlatsService.sendFoundSentBuyFlats(user);
        }
        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }

    // Нажали на "Достаточно"
    public void enough(List<BotApiMethod<?>> response, Integer callbackMessageId, UserCache user) {
        if (callbackMessageId != null) { // Если есть открытое меню
            response.add(this.deleteMessageService.deleteApiMethod(user.getChatId(), callbackMessageId)); // Удаляю сообщение "Показать еще"
            user.getUserChoice().setMenuMessageId(null); // Удалил меню
        }

        SendMessage okEnough = new SendMessage(); // Сообщение-ответ на кнопку "Достаточно"
        okEnough.setChatId(user.getChatId().toString());
        okEnough.setText(messagesVariables.getUserEnoughText());

        SendMessage menu1 = new SendMessage(); // Сообщение меню "Мои предпочтения"
        menu1.setChatId(user.getChatId().toString());
        menu1.setText(messagesVariables.getUserMenu1Text());
        menu1.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());

        user.setBotUserState(UserState.MENU1); // Перешли в главное меню
        this.dataCache.saveUserCache(user);

        response.add(okEnough);
        response.add(menu1);
    }
}
