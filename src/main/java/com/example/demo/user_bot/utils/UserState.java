package com.example.demo.user_bot.utils;

public enum UserState {
    INIT, // Начальное состояние бота, после /start
    FIRST_INIT, // Юзер зашел первый раз
    FIRST_INIT_CATEGORY, // Первый раз, указывает категорию (аренда/покупка)
    FIRST_INIT_ROOMS, // Первый раз, выбирает кол-во комнат
    FIRST_INIT_DISTRICTS, // Первый раз, выбирает районы
    FIRST_INIT_BUDGET; // Первый раз, выбирает сумму
}
