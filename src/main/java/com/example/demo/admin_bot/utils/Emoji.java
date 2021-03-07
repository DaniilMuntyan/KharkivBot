package com.example.demo.admin_bot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HI(":wave:"), MAIN_MENU(":pencil2:"), KEY(":closed_lock_with_key:"),
    ONE(":one:"), TWO(":two:"), THREE(":three:"), FOUR(":four:"),
    SUBMENU(":pencil2:"), RIGHT(":point_right:"), SELECTED(":white_check_mark:"),
    CANCEL(":x:"), EARTH(":earth_africa:"), CONTACT(":iphone:"), YES(":white_check_mark:"),
    NO(":x:"), WARNING(":warning:"), FAIL(":no_entry:"), SUCCESS(":white_check_mark:"),
    PUBLISH(":memo:");

    private final String emojiName;

    Emoji(String name) {
        this.emojiName = EmojiParser.parseToUnicode(name);
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
