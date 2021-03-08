package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum BuyRange {
    USD_14000_20000("14000 - 20000"), USD_20000_25000("20000 - 25000"), USD_25000_30000("25000 - 30000"),
    USD_30000_35000("30000 - 35000"), USD_35000_40000("35000 - 40000"), USD_40000_45000("40000 - 45000"),
    USD_45000_50000("45000 - 50000"), USD_50000_60000("50000 - 60000"), USD_60000_70000("70000 - 80000"),
    USD_80000_90000("80000 - 90000"), USD_90000_100000("90000 - 100000"), USD_100000_150000("100000 - 150000"),
    USD_150000_200000("150000 - 200000"), USD_200000_PLUS("200000+");

    private final String range;

    public static BuyRange valueOfRange(String range) {
        for(BuyRange temp: BuyRange.values()) {
            if (temp.toString().equals(range)) {
                return temp;
            }
        }
        return null;
    }

    BuyRange(String range) {
        this.range = range + " $";
    }

    public static List<String> getAllNames() {
        List<String> s = new ArrayList<>();
        for (BuyRange temp: BuyRange.values()) {
            s.add(temp.toString());
        }
        return s;
    }

    @Override
    public String toString() {
        return this.range;
    }
}