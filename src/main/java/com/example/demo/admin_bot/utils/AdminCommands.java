package com.example.demo.admin_bot.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCommands {
    public final static String START = "/start";
    public final static String ADMIN = "/admin";
    public final static String EXIT = "/exit";

    public static List<String> getAllCommands() {
        return new ArrayList<>(Arrays.asList(START, ADMIN, EXIT));
    }
}
