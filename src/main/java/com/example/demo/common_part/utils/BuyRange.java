package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum BuyRange {
    USD_14000_20000("14000 - 20000", "2"), USD_20000_25000("20000 - 25000", "2"), USD_25000_30000("25000 - 30000", "3"),
    USD_30000_35000("30000 - 35000", "4"), USD_35000_40000("35000 - 40000", "5"), USD_40000_45000("40000 - 45000", "6"),
    USD_45000_50000("45000 - 50000", "7"), USD_50000_60000("50000 - 60000", "8"), USD_60000_70000("70000 - 80000", "9"),
    USD_80000_90000("80000 - 90000", "10"), USD_90000_100000("90000 - 100000", "11"), USD_100000_150000("100000 - 150000", "12"),
    USD_150000_200000("150000 - 200000", "13"), USD_200000_PLUS("200000+", "14");

    private final String range;
    private final String identifier;

    public static BuyRange valueOfRange(String range) {
        for(BuyRange temp: BuyRange.values()) {
            if (temp.toString().equals(range)) {
                return temp;
            }
        }
        return null;
    }

    public String getIdentifier() {
        return identifier;
    }

    BuyRange(final String range, final String identifier) {
        this.range = range + " $";
        this.identifier = "&!!" + identifier + "!!#";
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
