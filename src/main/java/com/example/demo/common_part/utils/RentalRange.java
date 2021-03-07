package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum RentalRange {
    UAH_4500_6000("4500 - 6000"), UAH_6000_8000("6000 - 8000"),
    UAH_8000_10000("8000 - 10000"), UAH_10000_12000("10000 - 12000"),
    UAH_12000_16000("12000_16000"), UAH_16000_PLUS("16000+");

    private final String range;

    RentalRange(String range) {
        this.range = range + " грн";
    }

    public static List<String> getAllNames() {
        List<String> s = new ArrayList<>();
        for (RentalRange temp: RentalRange.values()) {
            s.add(temp.toString());
        }
        return s;
    }

    @Override
    public String toString() {
        return this.range;
    }
}
