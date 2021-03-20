package com.example.demo.user_bot.service.handler.message.menu;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public final class Menu1MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu1MessageHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final BackToMenu1 backToMenu1;
    private final DataCache dataCache;

    @Autowired
    public Menu1MessageHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, BackToMenu1 backToMenu1, DataCache dataCache) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.backToMenu1 = backToMenu1;
        this.dataCache = dataCache;
    }

    public List<BotApiMethod<?>> handleMessage(Message message, UserCache user) {
        String text = message.hasText() ? message.getText(): "";
        Long chatId = message.getChatId();

        boolean dontUnderstand = true; // Не понимаю пользователя (пришло левое сообщение)

        List<BotApiMethod<?>> response = new ArrayList<>();

        LOGGER.info("handleMessage");
        if (text.equals(userMenuVariables.getMenu1BtnChoiceText())) { // Нажали "Мои предпочтения"
            dontUnderstand = false; // Поняли пользователя
            SendMessage menu2 = new SendMessage();
            menu2.setChatId(message.getChatId().toString());
            menu2.setText(messagesVariables.getUserMenu2Text());
            menu2.setReplyMarkup(keyboardsRegistry.getMenu2().getKeyboard()); // Кнопки меню2
            response.add(menu2);
            LOGGER.info("To menu 2");
            user.setBotUserState(UserState.MENU2); // Перешли в меню изменения параметров
            this.dataCache.saveUserCache(user);
            //dataCache.markNotSaved(chatId); // Чтобы потом сохранить в базу
        }

        if (text.equals(userMenuVariables.getMenu1BtnSettingsText())) { // Нажали "Настройки"
            dontUnderstand = false; // Поняли пользователя
            SendMessage menu3 = new SendMessage();
            menu3.setChatId(message.getChatId().toString());
            menu3.setText(messagesVariables.getUserMenu3Text());
            menu3.setReplyMarkup(keyboardsRegistry.getMenu3().getKeyboard(user));
            response.add(menu3);
            LOGGER.info("To menu 3");
            user.setBotUserState(UserState.MENU3); // Перешли в меню настройки
            this.dataCache.saveUserCache(user);
            //dataCache.markNotSaved(chatId);
        }

        if (dontUnderstand) { // Не понимаю юзера
            response.add(this.backToMenu1.dontUnderstand(user));
        }

        return response;
    }
}
