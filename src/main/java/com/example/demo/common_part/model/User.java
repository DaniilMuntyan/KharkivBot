package com.example.demo.common_part.model;

import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.utils.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user", schema="public")
@Entity
public final class User {
    @Id
    @SequenceGenerator(name = "USER_SEQUENCE", sequenceName = "user_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQUENCE")
    @Column(name="user_id")
    private Long id;

    @NotNull
    private Long chatId;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="username")
    private String username;

    @Column(name="admin_mode")
    private boolean adminMode;

    @Column(name="admin_state")
    @Enumerated(EnumType.ORDINAL)
    private AdminState botAdminState;

    @Column(name="user_state")
    @Enumerated(EnumType.ORDINAL)
    private UserState botUserState;

    @Column(name="phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_choice", referencedColumnName = "choice_id")
    private AdminChoice adminChoice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_choice", referencedColumnName = "choice_id")
    private UserChoice userChoice;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="last_action")
    @CreationTimestamp
    private Date lastAction;

    /*public User(Long chatId, String firstName, String lastName, String username) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.adminMode = false;
        this.botState = State.ADMIN_INIT;
    }*/

    public User(Message message) {
        this.chatId = message.getChatId();
        this.firstName = message.getFrom().getFirstName();
        this.lastName = message.getFrom().getLastName();
        this.username = message.getFrom().getUserName();
        this.adminMode = false;
        this.botUserState = UserState.USER_INIT;
        this.userChoice = new UserChoice();
    }

    public String getName(boolean withUsername) {
        String name = "";
        if (this.firstName != null) {
            name += this.firstName;
        }
        if (this.lastName != null) {
            name += " " + this.lastName;
        }
        if (this.username != null && withUsername) {
            name += " (@" + this.username + ")";
        }
        return name;
    }
}
