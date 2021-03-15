package com.example.demo.common_part.model;

import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.model.UserChoice;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    /*@ManyToMany(mappedBy = "user_choice_rental")
    @Getter
    private Set<UserChoice> userChoices;*/


    public String getHtmlMessage() {
        return (Emoji.RECORD  + ((telegraph != null) ? " <a href=\"" + htmlEncode(telegraph) + "\">№ " + id.toString() + "</a>" : " № " + id.toString())) + "\n" +
                ((rooms != null) ? (Emoji.ROOMS + " " + rooms + (rooms != Rooms.GOSTINKA ? "к" : "") + "\n") : "") +
                (((square != null && square != 0.0) ? (Emoji.SQUARE + " " + square + "м²\n") : "")) +
                (((floor != null && allFloors != null) ? (Emoji.FLOOR + " Этаж: " + floor + "/" + allFloors + "\n") : "")) +
                ((metro != null && !metro.isEmpty()) ? (Emoji.METRO + " " + metro + "\n") : "") +
                ((address != null && !address.isEmpty()) ? (Emoji.ADDRESS + " " + address + "\n") : "") +
                ((money != null && !money.isEmpty()) ? (Emoji.MONEY + " " + money + "\n") : "") +
                ((info != null && !info.isEmpty()) ? (Emoji.INFO + " " + htmlEncode(info) + "\n") : "");
    }

    private String htmlEncode(String text) { // Вынести в сервис
        text = text.replace("&", "&amp;");
        text = text.replace("\"", "&quot;");
        text = text.replace(">", "&gt;");
        text = text.replace("<", "&lt;");
        return text;
    }

    private String htmlDecode(String url) { // Вынести в сервис
        url = url.replace("\"&amp;\"", "&");
        url = url.replace("&quot;", "\"");
        url = url.replace("&gt;", ">");
        url = url.replace("&lt;", "<");
        return url;
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

    public String getHtmlMapLink() {
        return htmlEncode(mapLink);
    }
}
