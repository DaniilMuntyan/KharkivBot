package com.example.demo.admin_bot.utils;

public enum AdminState {
    ADMIN_INIT, // Начальное состояние бота, когда процесс публикации квартиры не идет
    ADMIN_EXIT, // Состояние бота, выход из режима админа
    ADMIN_ADD_BUY_FLAT, // Состояние бота после нажатия на кнопку "Добавить квартиру (продажа)"
    ADMIN_ADD_RENT_FLAT, // Состояние бота после нажатия на кнопку "Добавить квартиру (аренда)"
    ADMIN_WRITE_MESSAGE, // Состояние бота после нажатия на кнопку "Написать сообщение"
    ADMIN_WAIT_MESSAGE, // Состояние бота после ADMIN_WRITE_MESSAGE - ждем, пока пришлют сообщение
    ADMIN_SUBMENU_SQUARE, // Состояние бота после нажатия на кнопку подпункта "Площадь"
    ADMIN_SUBMENU_FLOOR, // Состояние бота после нажатия на кнопку подпункта "Этаж"
    ADMIN_SUBMENU_ALL_FLOORS, // Состояние бота после нажатия на кнопку подпункта "Всего этажей"
    ADMIN_SUBMENU_METRO, // Состояние бота после нажатия на кнопку подпункта "Метро"
    ADMIN_SUBMENU_ADDRESS, // Состояние бота после нажатия на кнопку подпункта "Адрес"
    ADMIN_SUBMENU_PRICE, // Состояние бота после нажатия на кнопку подпункта "Цена"
    ADMIN_SUBMENU_PHOTO, // Состояние бота после нажатия на кнопку подпункта "Фото"
    ADMIN_SUBMENU_INFO, // Состояние бота после нажатия на кнопку подпункта "Доп. информация"
    ADMIN_SUBMENU_MAP, // Состояние бота после нажатия на кнопку подпункта "На карте"
    ADMIN_SUBMENU_CONTACT, // Состояние бота после нажатия на кнопку подпункта "Контакт"
    ADMIN_SUBMENU_PUBLISHING, // Состояние бота после нажатия на кнопку подпункта "Опубликовать"

    /*USER_INIT; // Начальное состояние бота, после /start

    public static AdminState[] getAdminStates() {
        return new AdminState[] {ADMIN_INIT, ADMIN_EXIT, ADMIN_ADD_BUY_FLAT, ADMIN_ADD_RENT_FLAT, ADMIN_WRITE_MESSAGE,
                ADMIN_WAIT_MESSAGE, ADMIN_SUBMENU_SQUARE, ADMIN_SUBMENU_FLOOR, ADMIN_SUBMENU_ALL_FLOORS,
                ADMIN_SUBMENU_METRO, ADMIN_SUBMENU_ADDRESS, ADMIN_SUBMENU_PRICE, ADMIN_SUBMENU_PHOTO,
                ADMIN_SUBMENU_INFO, ADMIN_SUBMENU_MAP, ADMIN_SUBMENU_CONTACT, ADMIN_SUBMENU_PUBLISHING};
    }

    public static AdminState[] getUserStates() {
        return new AdminState[] {USER_INIT};
    }*/
}
