package com.example.demo.user_bot.utils;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public final class MenuSendMessage extends SendMessage {
    // Класс для идентификации нового меню
    // Когда буду отправлять объект этого класса - буду знать, что отправляю новое меню

    // Если changeMenuMessageId - true, то изменяю поле menuMessageId в кэше у пользователя
    // с текущим chatId
    @Getter
    @Setter
    private boolean changeMenuMessageId = false;
}
