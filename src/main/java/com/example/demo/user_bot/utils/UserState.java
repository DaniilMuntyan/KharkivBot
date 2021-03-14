package com.example.demo.user_bot.utils;

import java.util.List;

public enum UserState {
    INIT, // Начальное состояние бота, после /start
    FIRST_INIT, // Юзер зашел первый раз
    FIRST_INIT_CATEGORY, // Первый раз, указывает категорию (аренда/покупка)
    FIRST_INIT_ROOMS, // Первый раз, выбирает кол-во комнат
    FIRST_INIT_DISTRICTS, // Первый раз, выбирает районы
    FIRST_INIT_BUDGET, // Первый раз, выбирает сумму
    FIRST_INIT_END; // Конец меню инициализации

    public static List<UserState> getFirstInit() {
        return List.of(FIRST_INIT, FIRST_INIT_CATEGORY, FIRST_INIT_ROOMS, FIRST_INIT_DISTRICTS, FIRST_INIT_BUDGET);
    }

    public static List<UserState> getNotFirst() {
        return List.of(INIT);
    }
}
