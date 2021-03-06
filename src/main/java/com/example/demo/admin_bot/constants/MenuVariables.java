package com.example.demo.admin_bot.constants;

import com.example.demo.admin_bot.utils.Emoji;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@PropertySource(value="classpath:menu.properties", encoding = "UTF-8")
public class MenuVariables {
    @Value("${admin.bot.button.addRentFlat.text}")
    private String addRentFlat;
    public String getAddRentFlatBtnText() {
        return MessageFormat.format(addRentFlat, Emoji.ADD);
    }

    @Value("${admin.bot.button.addBuyFlat.text}")
    private String addBuyFlat;
    public String getAddBuyFlatBtnText() {
        return MessageFormat.format(addBuyFlat, Emoji.ADD);
    }

    @Value("${admin.bot.button.text.rooms}")
    @Getter
    private String adminBtnRooms;
    @Value("${admin.bot.button.callback.rooms}")
    @Getter
    private String adminBtnCallbackRooms;

    @Value("${admin.bot.button.text.square}")
    @Getter
    private String adminBtnSquare;
    @Value("${admin.bot.button.callback.square}")
    @Getter
    private String adminBtnCallbackSquare;

    @Value("${admin.bot.button.text.floor}")
    @Getter
    private String adminBtnFloor;
    @Value("${admin.bot.button.callback.floor}")
    @Getter
    private String adminBtnCallbackFloor;

    @Value("${admin.bot.button.text.allFloor}")
    @Getter
    private String adminBtnAllFloor;
    @Value("${admin.bot.button.callback.allFloor}")
    @Getter
    private String adminBtnCallbackAllFloor;

    @Value("${admin.bot.button.text.metro}")
    @Getter
    private String adminBtnMetro;
    @Value("${admin.bot.button.callback.metro}")
    @Getter
    private String adminBtnCallbackMetro;

    @Value("${admin.bot.button.text.address}")
    @Getter
    private String adminBtnAddress;
    @Value("${admin.bot.button.callback.address}")
    @Getter
    private String adminBtnCallbackAddress;

    @Value("${admin.bot.button.text.district}")
    @Getter
    private String adminBtnDistrict;
    @Value("${admin.bot.button.callback.district}")
    @Getter
    private String adminBtnCallbackDistrict;

    @Value("${admin.bot.button.text.money}")
    @Getter
    private String adminBtnMoney;
    @Value("${admin.bot.button.callback.money}")
    @Getter
    private String adminBtnCallbackMoney;

    @Value("${admin.bot.button.text.moneyRange}")
    @Getter
    private String adminBtnMoneyRange;
    @Value("${admin.bot.button.callback.moneyRange}")
    @Getter
    private String adminBtnCallbackMoneyRange;

    @Value("${admin.bot.button.text.telegraph}")
    @Getter
    private String adminBtnTelegraph;
    @Value("${admin.bot.button.callback.telegraph}")
    @Getter
    private String adminBtnCallbackTelegraph;

    @Value("${admin.bot.button.text.info}")
    @Getter
    private String adminBtnInfo;
    @Value("${admin.bot.button.callback.info}")
    @Getter
    private String adminBtnCallbackInfo;

    @Value("${admin.bot.button.text.cancel}")
    private String adminBtnCancel;
    public String getAdminBtnCancel() {
        return MessageFormat.format(adminBtnCancel, Emoji.CANCEL);
    }
    @Value("${admin.bot.button.callback.cancel}")
    @Getter
    private String adminBtnCallbackCancel;

    @Value("${admin.bot.button.callback.roomsZero}")
    @Getter
    private String adminBtnCallbackRoomsZero;

    @Value("${admin.bot.button.callback.roomsOne}")
    @Getter
    private String adminBtnCallbackRoomsOne;

    @Value("${admin.bot.button.callback.roomsTwo}")
    @Getter
    private String adminBtnCallbackRoomsTwo;

    @Value("${admin.bot.button.callback.roomsThree}")
    @Getter
    private String adminBtnCallbackRoomsThree;

    @Value("${admin.bot.button.callback.roomsFour}")
    @Getter
    private String adminBtnCallbackRoomsFour;

    @Value("${admin.bot.button.text.roomsZero}")
    @Getter
    private String adminBtnRoomsZero;

    @Value("${admin.bot.menu.text.selectRoom}")
    private String adminMenuTextSelectRoom;
    public String getAdminMenuTextSelectRoom() {
        return MessageFormat.format(adminMenuTextSelectRoom, Emoji.DOWN);
    }

    @Value("${admin.bot.button.text.submenu.cancel}")
    private String adminBtnSubmenuCancel;
    public String getAdminBtnSubmenuCancel() {
        return MessageFormat.format(adminBtnSubmenuCancel, Emoji.CANCEL);
    }

    @Value("${admin.bot.button.callback.submenu.cancel}")
    @Getter
    private String adminBtnCallbackSubmenuCancel;

    @Value("${admin.menu.addBuyFlat.text}")
    private String adminAddBuyFlatText;
    public String getAdminAddBuyFlatText() {
        return MessageFormat.format(adminAddBuyFlatText, Emoji.ADD);
    }

    @Value("${admin.menu.addRentFlat.text}")
    private String adminAddRentFlatText;
    public String getAdminAddRentFlatText() {
        return MessageFormat.format(adminAddRentFlatText, Emoji.ADD);
    }

}
