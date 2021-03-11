package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum RentalRange {
    UAH_4500_6000("4500 - 6000", "1"), UAH_6000_8000("6000 - 8000", "2"),
    UAH_8000_10000("8000 - 10000", "3"), UAH_10000_12000("10000 - 12000", "4"),
    UAH_12000_16000("12000 - 16000", "5"), UAH_16000_PLUS("16000+", "6");

    private final String range;
    private final String identifier;

    RentalRange(final String range, final String identifier) {
        this.range = range + " грн";
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static RentalRange valueOfRange(String range) {
        for(RentalRange temp: RentalRange.values()) {
            if (temp.toString().equals(range)) {
                return temp;
            }
        }
        return null;
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
