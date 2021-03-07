package com.example.demo.admin_bot.utils;

import com.example.demo.admin_bot.constants.MenuVariables;
import lombok.Getter;
import lombok.Setter;

public enum AdminChoiceParameter {
    ROOMS, SQUARE, FLOOR, ALL_FLOORS, METRO, ADDRESS,
    DISTRICT, MONEY, MONEY_RANGE, TELEGRAPH, INFO, MAP_LINK,
    CONTACT;

    static {
        MenuVariables menuVariables = BeanUtil.getBean(MenuVariables.class);

        DISTRICT.btnText = menuVariables.getAdminBtnDistrict();
        METRO.btnText = menuVariables.getAdminBtnMetro();
        ADDRESS.btnText = menuVariables.getAdminBtnAddress();
        ROOMS.btnText = menuVariables.getAdminBtnRooms();
        SQUARE.btnText = menuVariables.getAdminBtnSquare();
        FLOOR.btnText = menuVariables.getAdminBtnFloor();
        ALL_FLOORS.btnText = menuVariables.getAdminBtnAllFloor();
        MONEY.btnText = menuVariables.getAdminBtnMoney();
        MONEY_RANGE.btnText = menuVariables.getAdminBtnMoneyRange();
        MAP_LINK.btnText = menuVariables.getAdminBtnMap();
        CONTACT.btnText = menuVariables.getAdminBtnContact();
        TELEGRAPH.btnText = menuVariables.getAdminBtnTelegraph();
        INFO.btnText = menuVariables.getAdminBtnInfo();

        DISTRICT.btnCallback = menuVariables.getAdminBtnCallbackDistrict();
        METRO.btnCallback = menuVariables.getAdminBtnCallbackMetro();
        ADDRESS.btnCallback = menuVariables.getAdminBtnCallbackAddress();
        ROOMS.btnCallback = menuVariables.getAdminBtnCallbackRooms();
        SQUARE.btnCallback = menuVariables.getAdminBtnCallbackSquare();
        FLOOR.btnCallback = menuVariables.getAdminBtnCallbackFloor();
        ALL_FLOORS.btnCallback = menuVariables.getAdminBtnCallbackAllFloor();
        MONEY.btnCallback = menuVariables.getAdminBtnCallbackMoney();
        MONEY_RANGE.btnCallback = menuVariables.getAdminBtnCallbackMoneyRange();
        MAP_LINK.btnCallback = menuVariables.getAdminBtnCallbackMap();
        CONTACT.btnCallback = menuVariables.getAdminBtnCallbackContact();
        TELEGRAPH.btnCallback = menuVariables.getAdminBtnCallbackTelegraph();
        INFO.btnCallback = menuVariables.getAdminBtnCallbackInfo();

        DISTRICT.adminChoiceFieldName = "district";
        METRO.adminChoiceFieldName = "metro";
        ADDRESS.adminChoiceFieldName = "address";
        ROOMS.adminChoiceFieldName = "rooms";
        SQUARE.adminChoiceFieldName = "square";
        FLOOR.adminChoiceFieldName = "floor";
        ALL_FLOORS.adminChoiceFieldName = "allFloors";
        MONEY.adminChoiceFieldName = "money";
        MONEY_RANGE.adminChoiceFieldName = "moneyRange";
        MAP_LINK.adminChoiceFieldName = "map";
        CONTACT.adminChoiceFieldName = "contact";
        TELEGRAPH.adminChoiceFieldName = "telegraph";
        INFO.adminChoiceFieldName = "info";
    }

    public static AdminChoiceParameter[] getRequiredForPublish() {
        return new AdminChoiceParameter[]{ROOMS, SQUARE, FLOOR, ALL_FLOORS,
                ADDRESS, DISTRICT, MONEY, MONEY_RANGE};
    }

    @Getter
    private String btnText;
    @Getter
    private String btnCallback;
    @Getter
    private String adminChoiceFieldName;
    @Getter
    @Setter
    private String choiceValue;
}
