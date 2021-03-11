package com.example.demo.common_part.utils;

public enum Rooms {
    GOSTINKA("Гостинка", "0"), ONE("1", "1"), TWO("2", "2"),
    THREE("3", "3"), FOUR("4", "4");

    private final String text;
    private final String identifier;

    Rooms(final String text, final String identifier) {
        this.text = text;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return text;
    }
}
