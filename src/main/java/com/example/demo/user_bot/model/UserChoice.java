package com.example.demo.user_bot.model;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_choice", schema="public")
@Entity
public final class UserChoice {
    @Id
    @SequenceGenerator(name = "USER_CHOICE_SEQUENCE", sequenceName = "user_choice_choice_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_CHOICE_SEQUENCE")
    @Column(name="choice_id")
    @Getter
    private Long id;

    @Column(name="menu_message_id")
    @Getter
    @Setter
    private Integer menuMessageId;

    @Column(name="is_rent_flat")
    @Getter
    @Setter
    private Boolean isRentFlat;

    @Column(name="rooms")
    @Getter
    @Setter
    private String rooms;

    @Column(name="districts")
    @Getter
    @Setter
    private String districts;

    @Column(name="budget")
    @Getter
    @Setter
    private String budget;
}
