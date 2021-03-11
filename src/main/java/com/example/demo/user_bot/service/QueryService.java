package com.example.demo.user_bot.service;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.handler.admin_menu.submenu.CommonMethods;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.botapi.RentalTelegramBot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public final class QueryService {
    /*private static final Logger LOGGER = Logger.getLogger(QueryService.class);

    private final RentalTelegramBot rentalTelegramBot;
    private final CommonMethods commonMethods;
    private final MessagesVariables messagesVariables;

    @Autowired
    public QueryService(RentalTelegramBot rentalTelegramBot, CommonMethods commonMethods, MessagesVariables messagesVariables) {
        this.rentalTelegramBot = rentalTelegramBot;
        this.commonMethods = commonMethods;
        this.messagesVariables = messagesVariables;
    }

    public List<BotApiMethod<?>> execute(BotApiMethod<?> method, User admin) {
        List<BotApiMethod<?>> response = new ArrayList<>();
        try {
            LOGGER.info(this.rentalTelegramBot.execute(method).toString());

            // BotApiMethod<?> response = (BotApiMethod<?>) this.rentalTelegramBot.execute(method);
            // LOGGER.info("RESPONSE " + response.getMethod());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            LOGGER.error(e);
            LOGGER.info("method.class: " + method.getClass());
            boolean isSendMessage = method instanceof SendMessage;
            boolean hostInvalid = e.toString().contains("Bad") &&
                    e.toString().contains("Request") &&
                    e.toString().contains("host") &&
                    e.toString().contains("is invalid");
            boolean urlWrongHttp = e.toString().contains("URL") &&
                    e.toString().contains("wrong") &&
                    e.toString().contains("HTTP") &&
                    e.toString().contains("Bad") &&
                    e.toString().contains("Request");
            if (isSendMessage && (hostInvalid || urlWrongHttp)) { // Ошибка парсинга HTML
                if (admin.getAdminChoice().getMenuMessageId() != null) { // Если есть меню - говорю об ошибке и возвращаю кнопки выбора
                    response.add(commonMethods.getEditNewFlatKeyboard(admin.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
                } else { // Если меню нет, но мы получили такую ошибку - пишу админу сообщение
                    SendMessage error = new SendMessage();
                    error.setChatId(admin.getChatId().toString());
                    error.setText(e.toString());
                    response.add(error);
                }
            }
        }
        return response;
    }*/

}
