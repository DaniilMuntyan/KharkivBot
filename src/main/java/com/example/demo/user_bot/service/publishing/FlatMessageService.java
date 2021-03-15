package com.example.demo.user_bot.service.publishing;

import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.service.handler.callback.SeeOthersCallbackHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public final class FlatMessageService {
    private static final Logger LOGGER = Logger.getLogger(FlatMessageService.class);

    private final UserMenuVariables userMenuVariables;

    @Autowired
    public FlatMessageService(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    private InlineKeyboardMarkup getNewFlatKeyboard(RentFlat rentFlat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (rentFlat.getMapLink() != null) {
            InlineKeyboardButton buttonMap = InlineKeyboardButton.builder()
                    .text(userMenuVariables.getUserBotFlatMsgMapText())
                    .url(rentFlat.getHtmlMapLink())
                    .build();
            rows.add(List.of(buttonMap));
        }

        if (rentFlat.getContact() != null) {
            InlineKeyboardButton buttonContact = InlineKeyboardButton.builder()
                    .text(userMenuVariables.getUserBotFlatMsgContactText())
                    .url(rentFlat.getContact())
                    .build();
            rows.add(List.of(buttonContact));
        }


        if (rows.size() == 0) {
            return null;
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public SendMessage getMessageFromRentFlat(String chatId, RentFlat rentFlat) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(rentFlat.getHtmlMessage());
        InlineKeyboardMarkup keyboard = this.getNewFlatKeyboard(rentFlat);
        if (keyboard != null) {
            sendMessage.setReplyMarkup(keyboard);
        }
        LOGGER.info(rentFlat.getHtmlMessage());
        LOGGER.info(sendMessage);
        return sendMessage;
    }
}

