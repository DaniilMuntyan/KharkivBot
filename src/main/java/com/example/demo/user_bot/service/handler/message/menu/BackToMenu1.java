package com.example.demo.user_bot.service.handler.message.menu;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.utils.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public final class BackToMenu1 {
    private final DataCache dataCache;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public BackToMenu1(DataCache dataCache, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.dataCache = dataCache;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    // Возврат в главное меню
    public SendMessage back(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserMenu1Text());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());

        user.setBotUserState(UserState.MENU1); // Возвращаемся обратно
        dataCache.markNotSaved(user.getChatId()); // Чтобы потом сохранить в базу

        return sendMessage;
    }

    // Возврат в главное меню, потому что пользователь прислал "левое" сообщение
    public SendMessage dontUnderstand(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserDontUnderstandText());
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());

        user.setBotUserState(UserState.MENU1); // Перешли в главное меню
        dataCache.markNotSaved(user.getChatId()); // Чтобы потом сохранить в базу

        return sendMessage;
    }
}
