package com.example.demo.user_bot.model;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.common_part.utils.Rooms;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Set;

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
    @Setter
    private String rooms; // Каждый символ соответствует идентификатору комнаты

    @Column(name="districts")
    @Setter
    private String districts; // Каждый символ соответствует идентификатору района

    @Column(name="budget")
    @Setter
    private String budget;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="user_choice_rental",
            joinColumns = @JoinColumn(name = "user_choice_id"),
            inverseJoinColumns = @JoinColumn(name = "flat_id"))
    @Getter
    private Set<RentFlat> userChoicesRent;


    public String getRooms() {
        return rooms != null ? rooms : "";
    }

    public String getDistricts() {
        return districts != null ? districts : "";
    }

    public String getBudget() {
        return budget != null ? budget : "";
    }

    public void addBudget(Enum<?> enumRange) {
        if (this.budget != null) {
            this.budget += enumRange.ordinal() + " ";
        } else {
            this.budget = enumRange.ordinal() + " ";
        }
    }

}