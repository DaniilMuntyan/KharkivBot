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
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public final class Menu32MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(Menu3MessageHandler.class);

    private final UserMenuVariables userMenuVariables;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    private final FindFlatsService findFlatsService;
    private final SendFoundFlatsService sendFoundFlatsService;

    private final DataCache dataCache;

    private final BackToMenu3 backToMenu3;

    @Autowired
    public Menu32MessageHandler(UserMenuVariables userMenuVariables, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FindFlatsService findFlatsService, SendFoundFlatsService sendFoundFlatsService, DataCache dataCache, BackToMenu3 backToMenu3) {
        this.userMenuVariables = userMenuVariables;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.findFlatsService = findFlatsService;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.dataCache = dataCache;
        this.backToMenu3 = backToMenu3;
    }

    public List<BotApiMethod<?>> handleMessage(Message message, UserCache user) {
        String text = message.hasText() ? message.getText(): "MESSAGE HAS NO TEXT!";
        Long chatId = message.getChatId();

        List<BotApiMethod<?>> response = new ArrayList<>();

        LOGGER.info(text);

        if (message.hasContact()) { // Прислали свой контакт. Кнопка "Отправить мой контакт"
            Contact contact = message.getContact();
            user.setPhone(contact.getPhoneNumber()); // Сохраняю телефон пользователя

            response.add(this.getAcceptPhoneMessage(user));
        } else { // Прислали вручную
            if (checkPhoneText(text)) { // Если номер корректный
                user.setPhone(text); // Сохраняю телефон пользователя
                response.add(this.getAcceptPhoneMessage(user)); // Отправляю сообщение, что принял номер телефона
            } else { // Неверно указан номер
                response.add(this.getWrongPhoneMessage(user));
            }
        }

        if (text.equals(userMenuVariables.getMenu3BtnBackText())) { // Нажали "назад"
            response.add(this.backToMenu3.back(user));
        }

        user.setBotUserState(UserState.MENU3); // Возвращаемся обратно в меню "Настройки"
        this.dataCache.saveUserCache(user);
        //dataCache.markNotSaved(chatId); // Чтобы потом сохранить в базу

        return response;
    }

    private SendMessage getWrongPhoneMessage(UserCache user) {
        SendMessage wrongPhone = new SendMessage();
        wrongPhone.setChatId(user.getChatId().toString());
        wrongPhone.setText(messagesVariables.getUserMenu32WrongPhoneText());
        wrongPhone.setReplyMarkup(keyboardsRegistry.getMenu3().getKeyboard(user));
        return wrongPhone;
    }

    private SendMessage getAcceptPhoneMessage(UserCache user) {
        SendMessage acceptPhone = new SendMessage();
        acceptPhone.setChatId(user.getChatId().toString());
        acceptPhone.enableMarkdown(true);
        acceptPhone.setText(messagesVariables.getUserMenu32AcceptPhoneText());
        acceptPhone.setReplyMarkup(keyboardsRegistry.getMenu3().getKeyboard(user));
        return acceptPhone;
    }

    private boolean checkPhoneText(String text) {
        for (int i = 0; i < text.length(); ++i) {
            char temp = text.charAt(i);
            // Если ничего из этого - ошибка либо не номер телефона
            if (!Character.isDigit(temp) && temp != '+' && temp != '-' && temp != ' ') {
                return false;
            }
        }
        return true;
    }
}
