package com.example.demo.common_part.model;

import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.common_part.utils.money_range.Budget;

public interface Flat {
    String getHtmlMessage();
    String getHtmlMapLink();

    Long getId();
    District getDistrict();
    String getMetro();
    String getAddress();
    Rooms getRooms();
    Float getSquare();
    Short getFloor();
    Short getAllFloors();
    String getMoney();
    Budget getRange();
    String getMapLink();
    String getContact();
    String getTelegraph();
    String getInfo();
}
