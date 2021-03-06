package com.example.demo.model;

import com.example.demo.user_bot.utils.District;
import com.example.demo.user_bot.utils.Emoji;
import com.example.demo.user_bot.utils.RentalRange;
import com.example.demo.user_bot.utils.Rooms;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rental", schema="public")
@Entity
public final class RentFlat {
    @Id
    @SequenceGenerator(name = "RENTAL_SEQUENCE", sequenceName = "rental_rental_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "RENTAL_SEQUENCE")
    @Column(name="rental_id")
    @Getter
    private Long id;

    @Column(name="district")
    @Enumerated(EnumType.STRING)
    @Getter
    private District district;

    @Column(name="metro")
    @Getter
    private String metro;

    @Column(name="address")
    @Getter
    private String address;

    @Column(name="rooms")
    @Enumerated(EnumType.ORDINAL)
    @Getter
    private Rooms rooms;

    @Column(name="square")
    @Getter
    private Float square;

    @Column(name="floor")
    @Getter
    private Short floor;

    @Column(name="all_floors")
    @Getter
    private Short allFloors;

    @Column(name="money")
    @Getter
    private String money;

    @Column(name="money_range")
    @Enumerated(EnumType.STRING)
    private RentalRange rentalRange;
    public RentalRange getRange() {
        return rentalRange;
    }

    @Column(name="telegraph")
    @Getter
    private String telegraph;

    @Column(name="info")
    @Getter
    private String info;

    public String getMarkdownMessage() { // Вынести в отдельный сервис
        return (Emoji.RECORD + " [" + id.toString() + "](" + telegraph + ")" + "\n") +
                ((rooms != null) ? (Emoji.ROOMS + " " + rooms + "\n") : "") +
                ((square != null && square != 0.0) ? (Emoji.SQUARE + " " + square + "м²\n") : "") +
                ((floor != null && allFloors != null) ? (Emoji.FLOOR + " Этаж: " + floor + "/" + allFloors + "\n") : "") +
                ((metro != null && !metro.isEmpty()) ? (Emoji.METRO + " " + metro + "\n") : "") +
                ((address != null && !address.isEmpty()) ? (Emoji.ADDRESS + " " + address + "\n") : "") +
                ((money != null && !money.isEmpty()) ? (Emoji.MONEY + " " + money + "\n") : "") +
                ((info != null && !info.isEmpty()) ? (Emoji.INFO + " " + info + "\n") : "");
    }
}
