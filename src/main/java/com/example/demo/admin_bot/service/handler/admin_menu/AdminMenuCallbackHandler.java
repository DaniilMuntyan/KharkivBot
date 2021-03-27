package com.example.demo.admin_bot.service.handler.admin_menu;

import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.submenu.*;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMenuCallbackHandler {
    private final AdminMenuVariables adminMenuVariables;
    private final AdminService adminService;
    private final MessagesVariables messagesVariables;

    @Autowired
    public AdminMenuCallbackHandler(AdminMenuVariables adminMenuVariables, AdminService adminService, MessagesVariables messagesVariables) {
        this.adminMenuVariables = adminMenuVariables;
        this.adminService = adminService;
        this.messagesVariables = messagesVariables;
    }

    public List<BotApiMethod<?>> handleAdminMenuCallback(CallbackQuery callbackQuery, UserCache admin) {
        List<BotApiMethod<?>> response = new ArrayList<>();

        // Получаем айди сообщения с главным меню, если он отличается от сохраненного.
        // Сохраняем его, чтобы не давать создавать другое меню.
        // Потому что в один момент времени может обрабатываться только одно меню.
        if (!callbackQuery.getMessage().getMessageId().equals(admin.getAdminChoice().getMenuMessageId())) {
            admin.getAdminChoice().setMenuMessageId(callbackQuery.getMessage().getMessageId());
            adminService.saveChoice(admin.getAdminChoice());
        }

        // Отмена публикации квартиры
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackCancel())) {
            cancelCallback(response, callbackQuery, admin);
        }

        // Количество комнат
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackRooms())) {
            roomsCallback(response, callbackQuery, admin);
        }

        // Площадь
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackSquare())) {
            squareCallback(response, callbackQuery, admin);
        }

        // Этаж
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackFloor())) {
            floorCallback(response, callbackQuery, admin);
        }

        // Всего этажей
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackAllFloor())) {
            allFloorCallback(response, callbackQuery, admin);
        }

        // Метро
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackMetro())) {
            metroCallback(response, callbackQuery, admin);
        }

        // Адрес
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackAddress())) {
            addressCallback(response, callbackQuery, admin);
        }

        // Район
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackDistrict())) {
            districtCallback(response, callbackQuery, admin);
        }

        // Цена
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackMoney())) {
            priceCallback(response, callbackQuery, admin);
        }

        // Бюджет
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackMoneyRange())) {
            moneyRangeCallback(response, callbackQuery, admin);
        }

        // На карте
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackMap())) {
            mapCallback(response, callbackQuery, admin);
        }

        // Контакт
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackContact())) {
            contactCallback(response, callbackQuery, admin);
        }

        // Фото
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackTelegraph())) {
            photoCallback(response, callbackQuery, admin);
        }

        // Доп. информация
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackInfo())) {
            infoCallback(response, callbackQuery, admin);
        }

        // Публикация
        if (callbackQuery.getData().equals(adminMenuVariables.getAdminBtnCallbackPublish())) {
            publishCallback(response, callbackQuery, admin);
        }

        return response;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private void cancelCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        admin.setBotAdminState(AdminState.ADMIN_INIT);
        adminService.clearAdminChoice(admin);

        // Удаляю меню
        response.add(deleteApiMethod(callbackQuery.getMessage()));
        // Посылаю сообщение
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        sendMessage.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.WAVE, admin.getName(false)));
        sendMessage.setReplyMarkup(adminService.getMainMenu());
        response.add(sendMessage);
    }

    private void roomsCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        RoomsKeyboard roomsKeyboard = new RoomsKeyboard();

        editMessageText.setChatId(admin.getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getRoomsMessageText());
        editMessageText.setReplyMarkup(roomsKeyboard.getKeyboard());
        response.add(editMessageText);
    }

    private void squareCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        SquareKeyboard squareKeyboard = new SquareKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getSquareMessageText());
        editMessageText.setReplyMarkup(squareKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_SQUARE);
        adminService.saveAdminState(admin);
    }

    private void floorCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        FloorKeyboard floorKeyboard = new FloorKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getFloorMessageText());
        editMessageText.setReplyMarkup(floorKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_FLOOR);
        adminService.saveAdminState(admin);
    }

    private void allFloorCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        AllFloorsKeyboard allFloorsKeyboard = new AllFloorsKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getAllFloorsMessageText());
        editMessageText.setReplyMarkup(allFloorsKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_ALL_FLOORS);
        adminService.saveAdminState(admin);
    }

    private void metroCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        MetroKeyboard metroKeyboard = new MetroKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getMetroMessageText());
        editMessageText.setReplyMarkup(metroKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_METRO);
        adminService.saveAdminState(admin);
    }

    private void addressCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        AddressKeyboard addressKeyboard = new AddressKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getAddressMessageText());
        editMessageText.setReplyMarkup(addressKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_ADDRESS);
        adminService.saveAdminState(admin);
    }

    private void districtCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        DistrictKeyboard districtKeyboard = new DistrictKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getDistrictMessageText());
        editMessageText.setReplyMarkup(districtKeyboard.getKeyboard());
        response.add(editMessageText);
    }

    private void priceCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        PriceKeyboard priceKeyboard = new PriceKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getPriceMessageText());
        editMessageText.setReplyMarkup(priceKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_PRICE);
        adminService.saveAdminState(admin);
    }

    private void moneyRangeCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        // В зависимости от значения переменной isRentFlat - получаем клавиатуру
        MoneyRangeKeyboard moneyRangeKeyboard = new MoneyRangeKeyboard(admin.getAdminChoice().getIsRentFlat());

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getBudgetMessageText());
        editMessageText.setReplyMarkup(moneyRangeKeyboard.getKeyboard());
        response.add(editMessageText);
    }

    private void photoCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        PhotoKeyboard photoKeyboard = new PhotoKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getTelegraphMessageText());
        editMessageText.setReplyMarkup(photoKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_PHOTO);
        adminService.saveAdminState(admin);
    }

    private void infoCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        InfoKeyboard infoKeyboard = new InfoKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getInfoMessageText());
        editMessageText.setReplyMarkup(infoKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_INFO);
        adminService.saveAdminState(admin);
    }

    private void mapCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        MapKeyboard mapKeyboard = new MapKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getMapMessageText());
        editMessageText.setReplyMarkup(mapKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_MAP);
        adminService.saveAdminState(admin);
    }

    private void contactCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        ContactKeyboard contactKeyboard = new ContactKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messagesVariables.getContactMessageText());
        editMessageText.setReplyMarkup(contactKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_CONTACT);
        adminService.saveAdminState(admin);
    }

    private void publishCallback(List<BotApiMethod<?>> response, CallbackQuery callbackQuery, UserCache admin) {
        EditMessageText editMessageText = new EditMessageText();

        PublishKeyboard publishKeyboard = new PublishKeyboard();

        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.enableHtml(true);
        editMessageText.enableWebPagePreview();
        editMessageText.setText(admin.getAdminChoice().getHtmlMessage());
        editMessageText.setReplyMarkup(publishKeyboard.getKeyboard());
        response.add(editMessageText);

        admin.setBotAdminState(AdminState.ADMIN_SUBMENU_PUBLISHING);
        adminService.saveAdminState(admin);
    }
}