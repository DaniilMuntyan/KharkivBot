package com.example.demo.user_bot.service.publishing;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.handler.message.UserBotMessageHandler;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public final class ShowOrEnoughService {
    private static final Logger LOGGER = Logger.getLogger(ShowOrEnoughService.class);

    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final DataCache dataCache;
    private final SendFoundFlatsService sendFoundFlatsService;

    @Autowired
    public ShowOrEnoughService(MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, DataCache dataCache, SendFoundFlatsService sendFoundFlatsService) {
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.dataCache = dataCache;
        this.sendFoundFlatsService = sendFoundFlatsService;
    }

    // Нажали на "Показать еще"
    public void more(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache user) {
        response.add(this.deleteApiMethod(user.getChatId(), callbackQuery.getMessage().getMessageId())); // Удаляю сообщение "Показать еще"
        if (user.getUserChoice().getIsRentFlat()) {
            this.sendFoundFlatsService.sendNotSentRentFlats(user);
        } else {
            this.sendFoundFlatsService.sendNotSentBuyFlats(user);
        }
        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }

    // Нажали на "Достаточно"
    public void enough(List<BotApiMethod<?>> response, Integer callbackMessageId, UserCache user) {
        if (callbackMessageId != null) { // Если есть открытое меню
            response.add(this.deleteApiMethod(user.getChatId(), callbackMessageId)); // Удаляю сообщение "Показать еще"
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

    private DeleteMessage deleteApiMethod(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }
}
