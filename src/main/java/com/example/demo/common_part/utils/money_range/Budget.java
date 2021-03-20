package com.example.demo.common_part.utils.money_range;

public interface Budget {
    String getIdentifier();
    int ordinal();
    Budget[] getAllRanges();
}
