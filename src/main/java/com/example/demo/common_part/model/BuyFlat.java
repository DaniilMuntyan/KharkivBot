package com.example.demo.common_part.model;

import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.utils.money_range.BuyRange;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.Rooms;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="buy", schema="public")
@Entity
public final class BuyFlat {
    @Id
    @SequenceGenerator(name = "BUY_SEQUENCE", sequenceName = "buy_buy_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BUY_SEQUENCE")
    @Column(name="buy_id")
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
    private BuyRange buyRange;
    public BuyRange getRange() {
        return buyRange;
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

    private String htmlDecode(String text) { // Вынести в сервис
        text = text.replace("\"&amp;\"", "&");
        text = text.replace("&quot;", "\"");
        text = text.replace("&gt;", ">");
        text = text.replace("&lt;", "<");

        return text;
    }

    public BuyFlat(AdminChoice adminChoice) {
        this.address = adminChoice.getAddress();
        this.allFloors = adminChoice.getAllFloors();
        this.contact = adminChoice.getContact();
        this.floor = adminChoice.getFloor();
        this.district = adminChoice.getDistrict();
        this.info = adminChoice.getInfo();
        this.mapLink = adminChoice.getMapLink();
        this.metro = adminChoice.getMetro();
        this.money = adminChoice.getMoney();
        this.buyRange = BuyRange.valueOfRange(adminChoice.getMoneyRange());
        this.rooms = adminChoice.getRooms();
        this.square = adminChoice.getSquare();
        this.telegraph = adminChoice.getTelegraph();
    }

    /*@Override
    public boolean equals(Object obj) {
        return this.getId().equals(((BuyFlat) obj).getId());
    }
*/
    public String getHtmlMapLink() {
        return htmlEncode(mapLink);
    }
}
