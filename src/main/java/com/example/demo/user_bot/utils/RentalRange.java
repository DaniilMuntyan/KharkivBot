package com.example.demo.user_bot.utils;

public enum RentalRange {
    UAH_4500_6000("4500 - 6000"), UAH_6000_8000("6000 - 8000"),
    UAH_8000_10000("8000 - 10000"), UAH_10000_12000("10000 - 12000"),
    UAH_12000_16000("12000_16000"), UAH_16000_PLUS("16000+");

    private final String range;

    RentalRange(String range) {
        this.range = range + " грн";
    }

    public String getRentalRange() {
        return range;
    }
}
