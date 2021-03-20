package com.example.demo.user_bot.cache;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.money_range.Budget;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.utils.UserState;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public final class UserCache { // Что храним в кэше юзера
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
    private boolean wantsUpdates; // Хочет ли пользователь получать уведомления
    //private List<RentFlat> sentRentFlats; // Хранит уже отправленные пользователю квартиры под аренду

    public UserCache(User user, boolean saved) {
        this.chatId = user.getChatId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.botUserState = user.getBotUserState();
        this.phone = user.getPhone();
        this.userChoice = user.getUserChoice();
        this.lastAction = user.getLastAction();
        this.saved = saved;
        this.wantsUpdates = user.getWantsUpdates();
        this.spam = false;
        //this.sentRentFlats = new ArrayList<>();
    }

    public boolean getIsWantsUpdates() {
        return this.wantsUpdates;
    }
    public boolean getSaved() {
        return this.saved;
    }

    public long lastMessage() {
        return this.lastMessage;
    }

    public boolean getSpam() {
        return this.spam;
    }

    public void addSentRentFlat(RentFlat rentFlat) {
        //sentRentFlats.add(rentFlat);
    }

    public void addBudget(Budget range) throws Exception {
        String budget = range.getIdentifier();
        String userBudget = this.userChoice.getBudget();
        if (!userBudget.contains(budget)) { // Если такого бюджета еще нет у юзера
            this.userChoice.setBudget(userBudget + budget); // Добавляю новый бюджет
        } else { // Если уже существует такой бюджет - исключение
            throw new Exception("Attempt to add existed budget!\nUser budget choice: " +
                    this.getUserChoice().getBudget() + "\nRange we tried to add: " + range.getIdentifier());
        }
    }

    public void removeBudget(Budget range) throws Exception {
        String budget = range.getIdentifier();
        String userBudget = this.userChoice.getBudget();
        if (userBudget.contains(budget)) { // Если такой бюджет есть у юзера - удаляю
            this.userChoice.setBudget(userBudget.replace(budget, ""));
        } else { // Если такого бюджета нет у юзера - исключение
            throw new Exception("Attempt to remove not existed budget!\nUser budget choice: " +
                    this.getUserChoice().getBudget() + "\nRange we tried to remove: " + range.getIdentifier());
        }
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
