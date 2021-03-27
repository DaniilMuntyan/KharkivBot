package com.example.demo.user_bot.service.handler.message.menu;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.utils.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public final class BackToMenu3 {
    private final DataCache dataCache;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public BackToMenu3(DataCache dataCache, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.dataCache = dataCache;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    // Возврат в меню 3 (настройки)
    public SendMessage back(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(messagesVariables.getUserMenu3Text());
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu3().getKeyboard(user));

        user.setBotUserState(UserState.MENU3); // Возвращаемся обратно
        this.dataCache.saveUserCache(user);

        return sendMessage;
    }
}
