package com.example.demo.model;

import com.example.demo.user_bot.utils.*;
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
    private Double square;

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

    @Column(name="telegraph")
    @Getter
    private String telegraph;

    @Column(name="info")
    @Getter
    private String info;
}
