package com.example.demo.user_bot.utils;

import java.util.List;

public enum UserState {
    INIT, // Начальное состояние бота, после /start
    FIRST_INIT, // Юзер зашел первый раз
    FIRST_INIT_CATEGORY, // Первый раз, указывает категорию (аренда/покупка)
    FIRST_INIT_ROOMS, // Первый раз, выбирает кол-во комнат
    FIRST_INIT_DISTRICTS, // Первый раз, выбирает районы
    FIRST_INIT_BUDGET, // Первый раз, выбирает сумму
    FIRST_INIT_END, // Конец меню инициализации
    FLATS_MASSAGING, // Состояние, в котором мы шлем пользователю подходящие квартиры
    SENT_NOT_ALL, // Состояние после того, как отправили юзеру первые N квартир и сообщение "Показать еще"
    MENU1, // Главное меню (мои предпочтения и настройки)
    MENU2, // Меню "Мои предпочтения"
    MENU21, // Меню выбор категории
    MENU22, // Меню выбор количества комнат
    MENU23, // Меню выбор районов
    MENU24, // Меню выбор бюджета
    MENU3, // Меню "Настройки"
    MENU32; // Меню отправить номер

    public static List<UserState> getFirstStates() {
        return List.of(FIRST_INIT, FIRST_INIT_CATEGORY, FIRST_INIT_ROOMS, FIRST_INIT_DISTRICTS,
                FIRST_INIT_BUDGET, FIRST_INIT_END);
    }
}
