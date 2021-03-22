package com.example.demo.common_part.model;

import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.common_part.utils.money_range.BuyRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="buy", schema="public")
@Entity
public final class BuyFlat implements Flat {
    @Id
    @SequenceGenerator(name = "BUY_SEQUENCE", sequenceName = "buy_buy_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BUY_SEQUENCE")
    @Column(name="buy_id")
    private Long id;

    @Column(name="district")
    @Enumerated(EnumType.STRING)
    private District district;

    @Column(name="metro")
    private String metro;

    @Column(name="address")
    private String address;

    @Column(name="rooms")
    @Enumerated(EnumType.ORDINAL)
    private Rooms rooms;

    @Column(name="square")
    private Float square;

    @Column(name="floor")
    private Short floor;

    @Column(name="all_floors")
    private Short allFloors;

    @Column(name="money")
    private String money;

    @Column(name="money_range")
    @Enumerated(EnumType.STRING)
    private BuyRange buyRange;

    @Column(name="map")
    @Setter
    private String mapLink;

    @Column(name="contact")
    @Setter
    private String contact;

    @Column(name="telegraph")
    private String telegraph;

    @Column(name="info")
    private String info;

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

    private String htmlEncode(String text) { // Вынести в сервис
        text = text.replace("&", "&amp;");
        text = text.replace("\"", "&quot;");
        text = text.replace(">", "&gt;");
        text = text.replace("<", "&lt;");
        return text;
    }

    @Override
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

    @Override
    public String getHtmlMapLink() {
        return htmlEncode(mapLink);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public District getDistrict() {
        return this.district;
    }

    @Override
    public String getMetro() {
        return this.metro;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public Rooms getRooms() {
        return this.rooms;
    }

    @Override
    public Float getSquare() {
        return this.square;
    }

    @Override
    public Short getFloor() {
        return this.floor;
    }

    @Override
    public Short getAllFloors() {
        return this.allFloors;
    }

    @Override
    public String getMoney() {
        return this.money;
    }

    @Override
    public BuyRange getRange() {
        return buyRange;
    }

    @Override
    public String getMapLink() {
        return this.mapLink;
    }

    @Override
    public String getContact() {
        return this.contact;
    }

    @Override
    public String getTelegraph() {
        return this.telegraph;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    /*@Override
    public boolean equals(Object obj) {
        return this.getId().equals(((BuyFlat) obj).getId());
    }
*/
}
