package com.example.demo.common_part.utils;

public enum Rooms {
    GOSTINKA("Гостинка"), ONE("1"), TWO("2"), THREE("3"), FOUR("4");

    private final String text;

    Rooms(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
