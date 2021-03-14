package com.example.demo.user_bot.cache;

import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.service.state_handler.UserBotStateService;
import com.example.demo.user_bot.utils.UserState;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Builder
public final class UserCache { // Что храним в кэше

    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
    private UserState botUserState;
    private String phone;
    private UserChoice userChoice;
    private Date lastAction;
    private long lastMessage; // Когда было последнее сообщение - для антиспама
    private boolean saved; // Позывает, сохранен ли уже кэш в базе данных
    private boolean spam; // Показывает, пользователь в спаме или нет

    public boolean getSaved() {
        return this.saved;
    }

    public long lastMessage() {
        return this.lastMessage;
    }

    public boolean getSpam() {
        return this.spam;
    }

    public String getName(boolean withUsername) {
        String name = "";
        if (this.firstName != null) {
            name += this.firstName;
        }
        if (this.lastName != null) {
            name += " " + this.lastName;
        }
        if (withUsername && this.username != null) {
            name += " (@" + this.username + ")";
        }
        return name;
    }
}
