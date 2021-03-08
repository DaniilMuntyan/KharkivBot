package com.example.demo.common_part.model;

import com.example.demo.common_part.utils.District;
import com.example.demo.user_bot.utils.Emoji;
import com.example.demo.common_part.utils.RentalRange;
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
    private String telegraph;

    @Column(name="info")
    @Getter
    private String info;

    public String getMarkdownMessage() {
        return (Emoji.RECORD + ((telegraph != null) ?
                (" [№ " + id.toString() + "](" + telegraph + ")" + "\n") : (" № " + id.toString() + "\n"))) +
                ((rooms != null) ? (Emoji.ROOMS + " " + rooms + "к\n") : "") +
                ((square != null && square != 0.0) ? (Emoji.SQUARE + " " + square + "м²\n") : "") +
                ((floor != null && allFloors != null) ? (Emoji.FLOOR + " Этаж: " + floor + "/" + allFloors + "\n") : "") +
                ((metro != null && !metro.isEmpty()) ? (Emoji.METRO + " " + metro + "\n") : "") +
                ((address != null && !address.isEmpty()) ? (Emoji.ADDRESS + " " + address + "\n") : "") +
                ((money != null && !money.isEmpty()) ? (Emoji.MONEY + " " + money + "\n") : "") +
                ((info != null && !info.isEmpty()) ? (Emoji.INFO + " " + info + "\n") : "");
    }

    public String getHtmlMessage() {
        return (Emoji.RECORD  + ((telegraph != null) ? " № <a href=\"" + telegraph + "\">" + id.toString() + "</a>" + "\n" : " № " + id.toString()) + "\n") +
                ((rooms != null) ? (Emoji.ROOMS + " " + rooms + (rooms != Rooms.GOSTINKA ? "к" : "") + "\n") : "") +
                (((square != null && square != 0.0) ? (Emoji.SQUARE + " " + square + "м²\n") : "")) +
                (((floor != null && allFloors != null) ? (Emoji.FLOOR + " Этаж: " + floor + "/" + allFloors + "\n") : "")) +
                ((metro != null && !metro.isEmpty()) ? (Emoji.METRO + " " + metro + "\n") : "") +
                ((address != null && !address.isEmpty()) ? (Emoji.ADDRESS + " " + address + "\n") : "") +
                ((money != null && !money.isEmpty()) ? (Emoji.MONEY + " " + money + "\n") : "") +
                ((info != null && !info.isEmpty()) ? (Emoji.INFO + " " + info + "\n") : "");
    }

    public RentFlat(AdminChoice adminChoice) {
        this.address = adminChoice.getAddress();
        this.allFloors = adminChoice.getAllFloors();
        this.contact = adminChoice.getContact();
        this.floor = adminChoice.getFloor();
        this.district = adminChoice.getDistrict();
        this.info = adminChoice.getInfo();
        this.mapLink = adminChoice.getMapLink();
        this.metro = adminChoice.getMetro();
        this.money = adminChoice.getMoney();
        this.rentalRange = RentalRange.valueOfRange(adminChoice.getMoneyRange());
        this.rooms = adminChoice.getRooms();
        this.square = adminChoice.getSquare();
        this.telegraph = adminChoice.getTelegraph();
    }
}
