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
        return MessageFormat.format(addRentFlat, Emoji.MAIN_MENU);
    }

    @Value("${admin.bot.button.addBuyFlat.text}")
    private String addBuyFlat;
    public String getAddBuyFlatBtnText() {
        return MessageFormat.format(addBuyFlat, Emoji.MAIN_MENU);
    }

    @Value("${admin.bot.button.bulkMessage.text}")
    private String bulkMessage;
    public String getBulkMessageText() {
        return MessageFormat.format(bulkMessage, Emoji.MAIN_MENU);
    }

    @Value("${admin.bot.button.callback.prefix}")
    @Getter
    private String adminBtnCallbackMenuPrefix;

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

    @Value("${admin.bot.button.text.map}")
    @Getter
    private String adminBtnMap;
    @Value("${admin.bot.button.callback.map}")
    @Getter
    private String adminBtnCallbackMap;

    @Value("${admin.bot.button.text.contact}")
    @Getter
    private String adminBtnContact;
    @Value("${admin.bot.button.callback.contact}")
    @Getter
    private String adminBtnCallbackContact;

    @Value("${admin.bot.button.text.cancel}")
    private String adminBtnCancel;
    public String getAdminBtnCancel() {
        return MessageFormat.format(adminBtnCancel, Emoji.CANCEL);
    }
    @Value("${admin.bot.button.callback.cancel}")
    @Getter
    private String adminBtnCallbackCancel;

    @Value("${admin.bot.button.text.publish}")
    private String adminBtnPublish;
    public String getAdminBtnPublish() {
        return MessageFormat.format(adminBtnPublish, Emoji.PUBLISH);
    }
    @Value("${admin.bot.button.callback.publish}")
    @Getter
    private String adminBtnCallbackPublish;

    @Value("${admin.bot.button.text.confirmMessageYes}")
    private String adminBtnConfirmMessageYes;
    public String getAdminBtnConfirmMessageYes() {
        return MessageFormat.format(adminBtnConfirmMessageYes, Emoji.YES);
    }

    @Value("${admin.bot.button.text.confirmMessageNo}")
    private String adminBtnConfirmMessageNo;
    public String getAdminBtnConfirmMessageNo() {
        return MessageFormat.format(adminBtnConfirmMessageNo, Emoji.NO);
    }

    @Value("${admin.bot.button.callback.confirmMessagePrefix}")
    @Getter
    private String adminBtnCallbackConfirmPrefix;

    @Value("${admin.bot.button.callback.confirmMessageYes}")
    @Getter
    private String adminBtnCallbackConfirmMessageYes;

    @Value("${admin.bot.button.callback.confirmMessageNo}")
    @Getter
    private String adminBtnCallbackConfirmMessageNo;

    @Value("${admin.bot.button.text.submenu.googleMaps}")
    private String adminBtnSubmenuGoogleMaps;
    public String getAdminBtnSubmenuGoogleMaps() {
        return MessageFormat.format(adminBtnSubmenuGoogleMaps, Emoji.EARTH);
    }
    @Value("${admin.bot.button.link.submenu.googleMapsLink}")
    @Getter
    private String adminBtnSubmenuGoogleMapsLink;
    @Value("${admin.bot.button.callback.submenu.googleMaps}")
    @Getter
    private String adminBtnCallbackSubmenuGoogleMaps;

    @Value("${admin.bot.button.text.submenu.contact}")
    private String adminBtnSubmenuContact;
    public String getAdminBtnSubmenuContact() {
        return MessageFormat.format(adminBtnSubmenuContact, Emoji.CONTACT);
    }
    @Value("${admin.bot.button.callback.submenu.contactPrefix}")
    @Getter
    private String adminBtnCallbackSubmenuContactPrefix;
    @Value("${admin.bot.button.callback.submenu.contact}")
    @Getter
    private String adminBtnCallbackSubmenuMyContact;

    @Value("${admin.bot.button.callback.submenu.roomsPrefix}")
    @Getter
    private String adminBtnCallbackSubmenuRoomsPrefix;

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

    @Value("${admin.bot.button.text.submenu.cancel}")
    private String adminBtnSubmenuCancel;
    public String getAdminBtnSubmenuCancel() {
        return MessageFormat.format(adminBtnSubmenuCancel, Emoji.CANCEL);
    }

    @Value("${admin.bot.button.callback.submenu.cancelPrefix}")
    @Getter
    private String adminBtnCallbackSubmenuCancelPrefix;

    @Value("${admin.bot.button.callback.submenu.cancel}")
    @Getter
    private String adminBtnCallbackSubmenuCancel;

    @Value("${admin.menu.addBuyFlat.text}")
    private String adminAddBuyFlatText;
    public String getAdminAddBuyFlatText() {
        return MessageFormat.format(adminAddBuyFlatText, Emoji.MAIN_MENU);
    }

    @Value("${admin.menu.addRentFlat.text}")
    private String adminAddRentFlatText;
    public String getAdminAddRentFlatText() {
        return MessageFormat.format(adminAddRentFlatText, Emoji.MAIN_MENU);
    }

    @Value("${admin.menu.bulkMessage.text}")
    private String adminBulkMessageText;
    public String getAdminBulkMessageText() {
        return MessageFormat.format(adminBulkMessageText, Emoji.MAIN_MENU);
    }

    @Value("${admin.bot.button.callback.submenu.districtPrefix}")
    @Getter
    private String adminBtnCallbackSubmenuDistrictPrefix;
    public String getCallbackSubmenuDistrict(String buttonText) {
        return adminBtnCallbackSubmenuDistrictPrefix + buttonText;
    }

    @Value("${admin.bot.button.callback.submenu.rangePrefix}")
    @Getter
    private String adminBtnCallbackSubmenuRangePrefix;
    public String getCallbackSubmenuRange(String buttonText) {
        return adminBtnCallbackSubmenuRangePrefix + buttonText;
    }

    @Value("${admin.bot.button.text.submenu.publishConfirm.yes}")
    private String adminBtnPublishConfirmYes;
    public String getAdminBtnPublishConfirmYes() {
        return MessageFormat.format(adminBtnConfirmMessageYes, Emoji.YES);
    }
    @Value("${admin.bot.button.callback.submenu.publishConfirm.yes}")
    @Getter
    private String adminBtnCallbackSubmenuConfirmYes;

    @Value("${admin.bot.button.callback.submenu.publishConfirmPrefix}")
    @Getter
    private String adminBtnCallbackPublishPrefix;

    @Value("${admin.bot.button.text.submenu.publishConfirm.no}")
    private String adminBtnPublishConfirmNo;
    public String getAdminBtnPublishConfirmNo() {
        return MessageFormat.format(adminBtnConfirmMessageNo, Emoji.NO);
    }
    @Value("${admin.bot.button.callback.submenu.publishConfirm.no}")
    @Getter
    private String adminBtnCallbackSubmenuConfirmNo;

    @Value("${newFlat.button.map.text}")
    private String newFlatBtnMap;
    public String getNewFlatBtnMap() {
        return MessageFormat.format(newFlatBtnMap, Emoji.EARTH);
    }

    @Value("${newFlat.button.contact.text}")
    private String newFlatBtnContact;
    public String getNewFlatBtnContact() {
        return MessageFormat.format(newFlatBtnContact, Emoji.CONTACT);
    }

}
