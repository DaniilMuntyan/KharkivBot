package com.example.demo.common_part.model;

import com.example.demo.common_part.utils.District;
import com.example.demo.user_bot.utils.Emoji;
import com.example.demo.user_bot.utils.Rooms;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="admin_choice", schema="public")
@Entity
public final class AdminChoice {

    @Id
    @SequenceGenerator(name = "CHOICE_SEQUENCE", sequenceName = "admin_choice_choice_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "CHOICE_SEQUENCE")
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

    @Column(name="district")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private District district;

    @Column(name="metro")
    @Getter
    @Setter
    private String metro;

    @Column(name="address")
    @Getter
    @Setter
    private String address;

    @Column(name="rooms")
    @Enumerated(EnumType.ORDINAL)
    @Getter
    @Setter
    private Rooms rooms;

    @Column(name="square")
    @Getter
    @Setter
    private Float square;

    @Column(name="floor")
    @Getter
    @Setter
    private Short floor;

    @Column(name="all_floors")
    @Getter
    @Setter
    private Short allFloors;

    @Column(name="money")
    @Getter
    @Setter
    private String money;

    @Column(name="money_range")
    @Getter
    @Setter
    private String moneyRange;

    @Column(name="map")
    @Getter
    @Setter
    private String mapLink;

    @Column(name="contact")
    @Getter
    @Setter
    private String contact;

    @Column(name="telegraph")
    @Getter
    @Setter
    private String telegraph;

    @Column(name="info")
    @Getter
    @Setter
    private String info;

    public AdminChoice(boolean isRentFlat) {
        this.isRentFlat = isRentFlat;
    }

    public String getHtmlMessage() {
        return ((telegraph != null) ? Emoji.RECORD + " <a href=\"" + telegraph + "\">№ XXXXXX</a>" + "\n" : "") +
                ((rooms != null) ? (Emoji.ROOMS + " " + rooms + (rooms != Rooms.GOSTINKA ? "к" : "") + "\n") : "") +
                (((square != null && square != 0.0) ? (Emoji.SQUARE + " " + square + "м²\n") : "")) +
                (((floor != null && allFloors != null) ? (Emoji.FLOOR + " Этаж: " + floor + "/" + allFloors + "\n") : "")) +
                ((metro != null && !metro.isEmpty()) ? (Emoji.METRO + " " + metro + "\n") : "") +
                ((address != null && !address.isEmpty()) ? (Emoji.ADDRESS + " " + address + "\n") : "") +
                ((money != null && !money.isEmpty()) ? (Emoji.MONEY + " " + money + "\n") : "") +
                ((info != null && !info.isEmpty()) ? (Emoji.INFO + " " + info + "\n") : "") +
                "\nОстальные пункты:\n" +
                ((moneyRange != null) ? "Бюджет: " + moneyRange + "\n" : "") +
                ((mapLink != null) ? "На карте: " + "<a href=\"" + mapLink + "\">ссылка</a>\n" : "") +
                ((contact != null) ? "Контакт для связи: @" + contact.substring(contact.lastIndexOf('/') + 1) : "");
    }
}
