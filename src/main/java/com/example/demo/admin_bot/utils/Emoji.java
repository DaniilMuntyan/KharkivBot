package com.example.demo.admin_bot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HI(":wave:"), ADD(":pencil2:"), KEY(":closed_lock_with_key:"),
    ONE(":one:"), TWO(":two:"), THREE(":three:"), FOUR(":four:"),
    DOWN(":point_down:"), RIGHT(":point_right:"), SELECTED(":white_check_mark:"),
    CANCEL(":x:");

    private final String emojiName;

    Emoji(String name) {
        this.emojiName = EmojiParser.parseToUnicode(name);
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
