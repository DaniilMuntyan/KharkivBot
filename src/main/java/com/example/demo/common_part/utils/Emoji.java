package com.example.demo.common_part.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    WAVE(":wave:"), MAIN_MENU(":pencil2:"), KEY(":closed_lock_with_key:"),
    ONE(":one:"), TWO(":two:"), THREE(":three:"), FOUR(":four:"),
    SUBMENU(":pencil2:"), POINT_RIGHT(":point_right:"), SELECTED(":white_check_mark:"),
    CANCEL(":x:"), EARTH(":earth_africa:"), CONTACT(":iphone:"), YES(":white_check_mark:"),
    NO(":x:"), WARNING(":warning:"), FAIL(":no_entry:"), SUCCESS(":white_check_mark:"),
    PUBLISH(":memo:"), ERROR(":pensive:"), BACK(":arrow_left:"), SAVE(":white_check_mark:"),
    PHONE(":calling:"),
    RECORD(":memo:"),
    ROOMS(":bed:"),
    SQUARE(":triangular_ruler:"),
    FLOOR(":building_construction:"),
    ADDRESS(":round_pushpin:"),
    METRO(":part_alternation_mark:"),
    MONEY(":dollar:"),
    INFO(":information_source:"),
    NEXT(":arrow_right:"), DOWN(":point_down:"), SAD(":pensive:"), WINK(":wink:"),
    OK(":ok_hand:"), BOX_WITH_CHECK(":ballot_box_with_check:"), UP(":arrow_up:"),
    EYES(":eyes:"), POINT_UP(":point_up:"), ARROW_DOWN(":arrow_down:"),
    SMILE(":blush:"), PLUS(":heavy_plus_sign:"), MINUS(":heavy_minus_sign:"),
    SPEED(":runner:"), SEARCH(":mag:"),
    CLIPBOARD(":clipboard:"), TELEPHONE(":telephone:"), RAISED_HANDS(":raised_hands:"),
    ORANGE_DIAMOND(":small_orange_diamond:"), STARS_EYES(":star_struck:"), LIKE("\uD83D\uDC4D"),
    ANGEL(":innocent:"), GREY_EXCLAMATION(":grey_exclamation:"), MAN_RAISING("\uD83D\uDE4B\u200D"),
    PUNCH(":punch:");

    private final String emojiName;

    Emoji(String name) {
        this.emojiName = EmojiParser.parseToUnicode(name);
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
