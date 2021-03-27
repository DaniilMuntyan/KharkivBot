package com.example.demo.user_bot.service;

import com.example.demo.common_part.constants.MessagesVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public final class ErrorMessageService {
    private final MessagesVariables messagesVariables;

    @Autowired
    public ErrorMessageService(MessagesVariables messagesVariables) {
        this.messagesVariables = messagesVariables;
    }

    public SendMessage getUserError(Long chatId) {
        SendMessage error = new SendMessage();
        error.setText(messagesVariables.getUserErrorText());
        error.setChatId(chatId.toString());
        return error;
    }

}
