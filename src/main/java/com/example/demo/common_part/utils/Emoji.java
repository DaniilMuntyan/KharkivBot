package com.example.demo.common_part.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HI(":wave:"), MAIN_MENU(":pencil2:"), KEY(":closed_lock_with_key:"),
    ONE(":one:"), TWO(":two:"), THREE(":three:"), FOUR(":four:"),
    SUBMENU(":pencil2:"), RIGHT(":point_right:"), SELECTED(":white_check_mark:"),
    CANCEL(":x:"), EARTH(":earth_africa:"), CONTACT(":iphone:"), YES(":white_check_mark:"),
    NO(":x:"), WARNING(":warning:"), FAIL(":no_entry:"), SUCCESS(":white_check_mark:"),
    PUBLISH(":memo:"), ERROR(":pensive:"), BACK(":arrow_left:"), SAVE(":white_check_mark:"),
    PHONE("::calling"),
    RECORD(":memo:"),
    ROOMS(":bed:"),
    SQUARE(":triangular_ruler:"),
    FLOOR(":building_construction:"),
    ADDRESS(":round_pushpin:"),
    METRO(":part_alternation_mark:"),
    MONEY(":dollar:"),
    INFO(":information_source:"),
    NEXT(":arrow_right:"), DOWN(":point_down:"), SAD(":pensive:"), WINK(":wink:"),
    OK(":ok_hand:"),
    BOX_WITH_CHECK(":ballot_box_with_check:");

    private final String emojiName;

    Emoji(String name) {
        this.emojiName = EmojiParser.parseToUnicode(name);
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
