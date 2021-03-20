package com.example.demo.user_bot.service.handler.message.menu;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.publishing.FindFlatsService;
import com.example.demo.user_bot.service.publishing.SendFoundFlatsService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public final class Menu3MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu3MessageHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    private final FindFlatsService findFlatsService;
    private final SendFoundFlatsService sendFoundFlatsService;

    private final DataCache dataCache;

    private final BackToMenu1 backToMenu1;

    @Autowired
    public Menu3MessageHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FindFlatsService findFlatsService, SendFoundFlatsService sendFoundFlatsService, DataCache dataCache, BackToMenu1 backToMenu1) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.findFlatsService = findFlatsService;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.dataCache = dataCache;
        this.backToMenu1 = backToMenu1;
    }

    public List<BotApiMethod<?>> handleMessage(Message message, UserCache user) {
        String text = message.getText();
        Long chatId = message.getChatId();

        boolean dontUnderstand = true; // Не понимаю пользователя (пришло левое сообщение)

        List<BotApiMethod<?>> response = new ArrayList<>();

        LOGGER.info(text);

        if (text.equals(userMenuVariables.getMenu3BtnStopMailingText())) { // Нажали "Не присылать обновления"
            dontUnderstand = false;

            user.setWantsUpdates(false); // Не отправлять новые квартиры пользователю

            SendMessage stopUpdates = new SendMessage(); // Сообщение "Хорошо, не буду присылать обновления"
            stopUpdates.setChatId(chatId.toString());
            stopUpdates.setText(messagesVariables.getUserMenu31StopMailingText());
            stopUpdates.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(stopUpdates);

            user.setBotUserState(UserState.MENU1);
            this.dataCache.saveUserCache(user);
        }

        if (text.equals(userMenuVariables.getMenu3BtnStartMailingText())) { // Нажали "Присылать обновления"
            dontUnderstand = false;

            user.setWantsUpdates(true); // Включаю уведомления пользователю

            SendMessage startUpdates = new SendMessage(); // Сообщение "Обновления включены"
            startUpdates.setChatId(chatId.toString());
            startUpdates.setText(messagesVariables.getUserMenu31StartMailingText());
            startUpdates.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
            response.add(startUpdates);

            user.setBotUserState(UserState.MENU1);
            this.dataCache.saveUserCache(user);
        }

        if (text.equals(userMenuVariables.getMenu3BtnEnterPhoneText())) { // Нажали "Указать номер телефона"
            dontUnderstand = false;

            this.checkMenuForDelete(user, message, response); // Проверяю и удаляю прошлое меню, если оно еще открыто

            SendMessage menu32 = new SendMessage(); // Переход в меню 3.2
            menu32.setChatId(chatId.toString());
            menu32.setReplyMarkup(keyboardsRegistry.getMenu32().getKeyboard());
            menu32.setText(messagesVariables.getUserMenu32Text());
            response.add(menu32);

            user.setBotUserState(UserState.MENU32);
            this.dataCache.saveUserCache(user);
        }

        if (text.equals(userMenuVariables.getMenu3BtnBackText())) { // Нажали "назад"
            dontUnderstand = false;

            this.checkMenuForDelete(user, message, response); // Проверяю и удаляю прошлое меню, если оно еще открыто
            response.add(this.backToMenu1.back(user));
        }

        if (dontUnderstand) { // Не понимаю юзера
            this.checkMenuForDelete(user, message, response); // Проверяю и удаляю прошлое меню, если оно еще открыто
            response.add(this.backToMenu1.dontUnderstand(user));
        }

        return response;
    }

    private void checkMenuForDelete(UserCache user, Message message, List<BotApiMethod<?>> response) {
        boolean newMenuMessageId = user.getUserChoice().getMenuMessageId() != null &&
                !user.getUserChoice().getMenuMessageId().equals(message.getMessageId());
        if (newMenuMessageId) { // Если открыли новое меню (выбрали другой подпункт меню "Мои предпочтения")
            response.add(this.deleteApiMethod(user.getChatId(), user.getUserChoice().getMenuMessageId()));
        }
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }
    private DeleteMessage deleteApiMethod(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }
}
