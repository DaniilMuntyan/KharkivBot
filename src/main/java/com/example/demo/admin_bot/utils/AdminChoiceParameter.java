package com.example.demo.admin_bot.utils;

import com.example.demo.common_part.constants.AdminMenuVariables;
import lombok.Getter;
import lombok.Setter;

public enum AdminChoiceParameter {
    ROOMS, SQUARE, FLOOR, ALL_FLOORS, METRO, ADDRESS,
    DISTRICT, MONEY, MONEY_RANGE, TELEGRAPH, INFO, MAP_LINK,
    CONTACT;

    static {
        AdminMenuVariables adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);

        DISTRICT.btnText = adminMenuVariables.getAdminBtnDistrict();
        METRO.btnText = adminMenuVariables.getAdminBtnMetro();
        ADDRESS.btnText = adminMenuVariables.getAdminBtnAddress();
        ROOMS.btnText = adminMenuVariables.getAdminBtnRooms();
        SQUARE.btnText = adminMenuVariables.getAdminBtnSquare();
        FLOOR.btnText = adminMenuVariables.getAdminBtnFloor();
        ALL_FLOORS.btnText = adminMenuVariables.getAdminBtnAllFloor();
        MONEY.btnText = adminMenuVariables.getAdminBtnMoney();
        MONEY_RANGE.btnText = adminMenuVariables.getAdminBtnMoneyRange();
        MAP_LINK.btnText = adminMenuVariables.getAdminBtnMap();
        CONTACT.btnText = adminMenuVariables.getAdminBtnContact();
        TELEGRAPH.btnText = adminMenuVariables.getAdminBtnTelegraph();
        INFO.btnText = adminMenuVariables.getAdminBtnInfo();

        DISTRICT.btnCallback = adminMenuVariables.getAdminBtnCallbackDistrict();
        METRO.btnCallback = adminMenuVariables.getAdminBtnCallbackMetro();
        ADDRESS.btnCallback = adminMenuVariables.getAdminBtnCallbackAddress();
        ROOMS.btnCallback = adminMenuVariables.getAdminBtnCallbackRooms();
        SQUARE.btnCallback = adminMenuVariables.getAdminBtnCallbackSquare();
        FLOOR.btnCallback = adminMenuVariables.getAdminBtnCallbackFloor();
        ALL_FLOORS.btnCallback = adminMenuVariables.getAdminBtnCallbackAllFloor();
        MONEY.btnCallback = adminMenuVariables.getAdminBtnCallbackMoney();
        MONEY_RANGE.btnCallback = adminMenuVariables.getAdminBtnCallbackMoneyRange();
        MAP_LINK.btnCallback = adminMenuVariables.getAdminBtnCallbackMap();
        CONTACT.btnCallback = adminMenuVariables.getAdminBtnCallbackContact();
        TELEGRAPH.btnCallback = adminMenuVariables.getAdminBtnCallbackTelegraph();
        INFO.btnCallback = adminMenuVariables.getAdminBtnCallbackInfo();

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
