package com.example.demo.user_bot.cache;

import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.service.state_handler.UserBotStateService;
import com.example.demo.user_bot.utils.UserState;
import lombok.Builder;
import lombok.Data;
import org.apache.log4j.Logger;

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
    private Boolean saved; // Позывает, сохранен ли уже кэш в базе данных

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
