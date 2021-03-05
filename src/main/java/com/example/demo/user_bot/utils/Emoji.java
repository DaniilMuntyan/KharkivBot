package com.example.demo.user_bot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    RECORD(":memo:"),
    ROOMS(":bed:"),
    SQUARE(":triangular_ruler:"),
    FLOOR(":building_construction:"),
    ADDRESS(":round_pushpin:"),
    METRO(":part_alternation_mark:"),
    MONEY(":dollar:"),
    INFO(":information_source:");


    private final String emojiName;

    Emoji(String name) {
        this.emojiName = EmojiParser.parseToUnicode(name);
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
