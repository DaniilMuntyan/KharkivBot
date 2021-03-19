package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.botapi.AdminTelegramBot;
import com.example.demo.admin_bot.cache.AdminCache;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.admin_bot.keyboards.delete.ConfirmKeyboard;
import com.example.demo.admin_bot.keyboards.delete.EnterIdKeyboard;
import com.example.demo.admin_bot.keyboards.submenu.BulkMessageConfirmKeyboard;
import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.user_bot.service.entities.BuyFlatService;
import com.example.demo.user_bot.service.entities.RentalFlatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminBotStateService {
    private static final Logger LOGGER = Logger.getLogger(AdminBotStateService.class);

    private final AdminService adminService;
    private final AdminMenuVariables adminMenuVariables;
    private final MessagesVariables messagesVariables;

    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;

    private final AdminCache adminCache;

    // В некоторых методах возможен Exception, поэтому не всегда надо возвращаться в предыдущее состояние
    private boolean notBackToAdminMenu;

    @Autowired
    public AdminBotStateService(AdminService adminService, AdminMenuVariables adminMenuVariables, MessagesVariables messagesVariables, RentalFlatService rentalFlatService, BuyFlatService buyFlatService, AdminCache adminCache) {
        this.adminService = adminService;
        this.adminMenuVariables = adminMenuVariables;
        this.messagesVariables = messagesVariables;
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.adminCache = adminCache;
    }

    public List<BotApiMethod<?>> processAdminInput(Message message, User admin) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        notBackToAdminMenu = true;

        // Если прислали /start - удалить текущее меню и прислать сообщение
        if (admin.getBotAdminState() == AdminState.ADMIN_INIT) {
            this.processInit(answer, message, admin);
        }

        // Если прислали /exit - выйти из режима админа
        if (admin.getBotAdminState() == AdminState.ADMIN_EXIT) {
            this.processExit(answer, message, admin);
        }

        // Если нажали на кнопку "Добавить квартиру (продажа)"
        if (admin.getBotAdminState() == AdminState.ADMIN_ADD_BUY_FLAT) {
            processAddBuyFlat(answer, message, admin);
        }

        // Если нажали на кнопку "Добавить квартиру (аренда)"
        if (admin.getBotAdminState() == AdminState.ADMIN_ADD_RENT_FLAT) {
            processAddRentFlat(answer, message, admin);
        }

        // После команды "Удалить квартиру (аренда)"
        if (admin.getBotAdminState() == AdminState.ADMIN_DELETE_RENT_FLAT) {
            processDeleteRent(answer, admin);
        }

        // После команды "Удалить квартиру (продажа)"
        if (admin.getBotAdminState() == AdminState.ADMIN_DELETE_BUY_FLAT) {
            processDeleteBuy(answer, admin);
        }

        // Ввели айдишник квартиры (аренда)
        if (admin.getBotAdminState() == AdminState.ADMIN_DELETE_WAIT_RENT_ID) {
            processRentId(answer, message, admin);
        }

        // Ввели айдишник квартиры (продажа)
        if (admin.getBotAdminState() == AdminState.ADMIN_DELETE_WAIT_BUY_ID) {
            processBuyId(answer, message, admin);
        }

        // Порядок обработки ADMIN_WRITE_MESSAGE и ADMIN_WAIT_MESSAGE - важен.
        // Потому что внутри processNewMessage мы меняем состояние на ADMIN_WAIT_MESSAGE
        // Если прирслали сообщение для рассылки
        if (admin.getBotAdminState() == AdminState.ADMIN_WAIT_MESSAGE) {
            processAdminMessage(answer, message, admin);
        }

        // Если нажали на кнопку "Написать сообщение"
        if (admin.getBotAdminState() == AdminState.ADMIN_WRITE_MESSAGE) {
            processNewMessage(answer, message, admin);
        }

        // Ввели площадь
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_SQUARE) {
            processSquare(answer, message, admin);
        }

        // Ввели номер этажа квартиры
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_FLOOR) {
            processFloor(answer, message, admin);
        }

        // Ввели количество этажей в доме
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_ALL_FLOORS) {
            processAllFloor(answer, message, admin);
        }

        // Ввели станцию метро
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_METRO) {
            processMetro(answer, message, admin);
        }

        // Ввели адрес
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_ADDRESS) {
            processAddress(answer, message, admin);
        }

        // Ввели цену
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_PRICE) {
            processPrice(answer, message, admin);
        }

        // Ввели ссылку на карту
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_MAP) {
            processMap(answer, message, admin);
        }

        // Ввели контакт
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_CONTACT) {
            processContact(answer, message, admin);
        }

        // Ввели ссылку на фото
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_PHOTO) {
            processPhoto(answer, message, admin);
        }

        // Ввели доп. информацию
        if (admin.getBotAdminState() == AdminState.ADMIN_SUBMENU_INFO) {
            processInfo(answer, message, admin);
        }

        // Возвращаемся
        if (admin.getAdminChoice().getIsRentFlat() != null && !notBackToAdminMenu) {
            admin.setBotAdminState(admin.getAdminChoice().getIsRentFlat() ? AdminState.ADMIN_ADD_RENT_FLAT : AdminState.ADMIN_ADD_BUY_FLAT);
        }


        adminService.saveAdmin(admin); // Сохраняем измененные параметры администратора

        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private EditMessageText getEditKeyboard(Long chatId, Integer messageId, User admin) {
        NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(newFlatMenu.getKeyboard());
        editMessageText.setText(admin.getAdminChoice().getIsRentFlat() ?
                adminMenuVariables.getAdminAddRentFlatText() : adminMenuVariables.getAdminAddBuyFlatText());
        this.notBackToAdminMenu = false; // Поменяли клавиатуру - значит можем вернуться в предыдущее состояние поубликации квартиры
        return editMessageText;
    }

    private void processInit(List<BotApiMethod<?>> answer, Message message, User admin) {
        if (admin.getAdminChoice().getMenuMessageId() != null) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(admin.getAdminChoice().getMenuMessageId());
            deleteMessage.setChatId(message.getChatId().toString());
            admin.getAdminChoice().setMenuMessageId(null); // Удалили меню
            answer.add(deleteMessage);
        }

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(message.getChatId().toString());
        textResponse.setReplyMarkup(adminService.getMainMenu());
        textResponse.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.WAVE, admin.getName(false)));
        answer.add(textResponse);

        adminService.setAdminChoice(admin, new AdminChoice());
    }

    private void processAddBuyFlat(List<BotApiMethod<?>> answer, Message message, User admin) {
        NewFlatMenu newFlatMenu = adminService.getAddBuyFlatMenu();
        AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
        adminService.setAdminChoice(admin, newAdminChoice);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(adminMenuVariables.getAdminAddBuyFlatText());
        sendMessage.setReplyMarkup(newFlatMenu.getKeyboard());
        answer.add(sendMessage);
    }

    private void processAddRentFlat(List<BotApiMethod<?>> answer, Message message, User admin) {
        NewFlatMenu newFlatMenu = adminService.getAddRentFlatMenu();
        AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
        adminService.setAdminChoice(admin, newAdminChoice);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(adminMenuVariables.getAdminAddRentFlatText());
        sendMessage.setReplyMarkup(newFlatMenu.getKeyboard());
        answer.add(sendMessage);
    }

    private void processSquare(List<BotApiMethod<?>> answer, Message message, User admin) {
        try {
            Float square = Float.valueOf(message.getText().trim());
            if (square.equals(Float.POSITIVE_INFINITY) || square.equals(Float.NEGATIVE_INFINITY)) {
                throw new NumberFormatException("Square cannot be INFINITY!");
            }
            admin.getAdminChoice().setSquare(square);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        } catch (NumberFormatException ex) {
            answer.add(deleteApiMethod(message));
        }
    }

    private void processFloor(List<BotApiMethod<?>> answer, Message message, User admin) {
        try {
            Short floor = Short.valueOf(message.getText().trim());
            admin.getAdminChoice().setFloor(floor);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        } catch (NumberFormatException ex) {
            LOGGER.error(ex);
        }
    }

    private void processAllFloor(List<BotApiMethod<?>> answer, Message message, User admin) {
        try {
            Short allFloor = Short.valueOf(message.getText().trim());
            admin.getAdminChoice().setAllFloors(allFloor);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        } catch (NumberFormatException ex) {
            LOGGER.error(ex);
        }
    }

    private void processMetro(List<BotApiMethod<?>> answer, Message message, User admin) {
        String metro = message.getText().trim();
        if (!metro.isEmpty()) {
            admin.getAdminChoice().setMetro(metro);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processAddress(List<BotApiMethod<?>> answer, Message message, User admin) {
        String address = message.getText().trim();
        if (!address.isEmpty()) {
            admin.getAdminChoice().setAddress(address);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processPrice(List<BotApiMethod<?>> answer, Message message, User admin) {
        String price = message.getText().trim();
        if (!price.isEmpty()) {
            admin.getAdminChoice().setMoney(price);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processPhoto(List<BotApiMethod<?>> answer, Message message, User admin) {
        String telegraph = message.getText().trim();
        if (!telegraph.isEmpty()) {
            admin.getAdminChoice().setTelegraph(telegraph);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processInfo(List<BotApiMethod<?>> answer, Message message, User admin) {
        String info = message.getText().trim();
        if (!info.isEmpty()) {
            admin.getAdminChoice().setInfo(info);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processContact(List<BotApiMethod<?>> answer, Message message, User admin) {
        String contact = message.getText().trim();
        if (checkContact(contact)) {
            admin.getAdminChoice().setContact("https://t.me/" + contact.replace("@", ""));
            adminService.saveChoice(admin.getAdminChoice());
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }
    private boolean checkContact(String text) {
        String[] arr = text.split(" ");
        LOGGER.info("CHECK CONTACT: " + Arrays.toString(arr));
        return arr.length == 1 && arr[0].startsWith("@");
    }

    private void processMap(List<BotApiMethod<?>> answer, Message message, User admin) {
        String mapLink = message.getText().trim();
        if (!mapLink.isEmpty()) {
            admin.getAdminChoice().setMapLink(mapLink);
            answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
        }
    }

    private void processNewMessage(List<BotApiMethod<?>> answer, Message message, User admin) {
        // Удалить меню, если оно открыто
        if (admin.getAdminChoice().getMenuMessageId() != null) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId().toString());
            deleteMessage.setMessageId(message.getMessageId());
            admin.getAdminChoice().setMenuMessageId(null);
            adminService.saveChoice(admin.getAdminChoice());
        }

        // Меняем состояние, чтобы ждать сообщения
        admin.setBotAdminState(AdminState.ADMIN_WAIT_MESSAGE);
        adminService.saveAdminState(admin);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(adminMenuVariables.getAdminBulkMessageText());
        answer.add(sendMessage);
    }

    private void processAdminMessage(List<BotApiMethod<?>> answer, Message message, User admin) {
        String newMessage = message.getText().trim();

        if (!newMessage.isEmpty()) {
            BulkMessageConfirmKeyboard bulkMessageConfirmKeyboard = new BulkMessageConfirmKeyboard();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(admin.getChatId().toString());
            sendMessage.setText(newMessage);
            sendMessage.setEntities(message.getEntities());
            sendMessage.setReplyMarkup(bulkMessageConfirmKeyboard.getKeyboard());
            answer.add(sendMessage);
        }
    }

    private void processExit(List<BotApiMethod<?>> answer, Message message, User admin) {
        if (admin.getAdminChoice().getMenuMessageId() != null) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(admin.getAdminChoice().getMenuMessageId());
            deleteMessage.setChatId(message.getChatId().toString());
            admin.getAdminChoice().setMenuMessageId(null); // Удалили меню
            answer.add(deleteMessage);
        }

        admin.setAdminMode(false);
        admin.setBotAdminState(AdminState.ADMIN_INIT);

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(message.getChatId().toString());
        textResponse.setReplyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build());
        textResponse.setText(MessageFormat.format(messagesVariables.getAdminBye(), Emoji.WAVE));
        answer.add(textResponse);
    }

    private void processDeleteRent(List<BotApiMethod<?>> answer, User admin) {
        EnterIdKeyboard enterIdKeyboard = new EnterIdKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(admin.getChatId().toString());
        sendMessage.setText(messagesVariables.getAdminDeleteEnterRentId());
        sendMessage.setReplyMarkup(enterIdKeyboard.getKeyboard());
        //answer.add(sendMessage);

        AdminTelegramBot bot = BeanUtil.getBean(AdminTelegramBot.class);
        try {
            Message menu = bot.execute(sendMessage);
            admin.getAdminChoice().setMenuMessageId(menu.getMessageId()); // Запоминаю айди меню
        } catch (TelegramApiException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }

        admin.setBotAdminState(AdminState.ADMIN_DELETE_WAIT_RENT_ID); // Ждем айдишник квартиры для аренды
        adminService.saveAdmin(admin);
    }

    private void processDeleteBuy(List<BotApiMethod<?>> answer, User admin) {
        EnterIdKeyboard enterIdKeyboard = new EnterIdKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(admin.getChatId().toString());
        sendMessage.setText(messagesVariables.getAdminDeleteEnterBuyId());
        sendMessage.setReplyMarkup(enterIdKeyboard.getKeyboard());
        //answer.add(sendMessage);

        AdminTelegramBot bot = BeanUtil.getBean(AdminTelegramBot.class);
        try {
            Message menu = bot.execute(sendMessage);
            admin.getAdminChoice().setMenuMessageId(menu.getMessageId());
        } catch (TelegramApiException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }

        admin.setBotAdminState(AdminState.ADMIN_DELETE_WAIT_BUY_ID); // Ждем айдишник квартиры для продажи
        adminService.saveAdmin(admin);
    }

    private void processRentId(List<BotApiMethod<?>> answer, Message message, User admin) {
        String rentId = message.getText().trim();
        try {
            Long flatId = Long.valueOf(rentId);
            Optional<RentFlat> rentFlat = this.rentalFlatService.findById(flatId);
            if (rentFlat.isPresent()) { // Если удаляемая квартира существует
                ConfirmKeyboard confirmKeyboard = new ConfirmKeyboard();

                // Устанавливаю новый adminChoice - это rentFlat (для удаления)
                adminService.setAdminChoice(admin,
                        adminService.getAdminChoiceFromFlat(rentFlat.get(), admin.getAdminChoice().getMenuMessageId()));

                adminCache.addFlatToDelete(admin.getChatId(), flatId); // Сохраняю айдишник квартиры на удаление в кэше

                EditMessageText editMessageText = new EditMessageText(); // Сообщение с найденной квартирой
                editMessageText.setChatId(admin.getChatId().toString());
                editMessageText.setMessageId(admin.getAdminChoice().getMenuMessageId()); // Изменяю меню
                editMessageText.setReplyMarkup(confirmKeyboard.getKeyboard());
                editMessageText.enableHtml(true);
                editMessageText.setDisableWebPagePreview(true); // Не показывать превью телеграфа для удобства
                editMessageText.setText(admin.getAdminChoice().getHtmlMessage()); // Показываю adminChoice (что именно мы удаляем)

                answer.add(editMessageText);

                LOGGER.info("ADMIN FLAT ID: " + admin.getAdminChoice().getFlatId());

                admin.setBotAdminState(AdminState.ADMIN_DELETE_RENT_CONFIRM); // Ждем подтверждения
            } else {
                EditMessageText editMessageText = new EditMessageText(); // Такого ID не существует
                editMessageText.setChatId(admin.getChatId().toString());
                editMessageText.setMessageId(admin.getAdminChoice().getMenuMessageId());
                editMessageText.setText(messagesVariables.getAdminDeleteWrongId());

                answer.add(editMessageText);

                admin.getAdminChoice().setMenuMessageId(null); // Удалил меню
                admin.setBotAdminState(AdminState.ADMIN_INIT); // Возвращаемся в исходное состояние
            }
            adminService.saveAdmin(admin); // Сохраняю измененное состояние
        } catch (NumberFormatException ex) {
            LOGGER.error(ex);
            // Ничего не делаем (ждем пока введет правильно)
        }
    }
    private void processBuyId(List<BotApiMethod<?>> answer, Message message, User admin) {
        String buyId = message.getText().trim();
        try {
            Long flatId = Long.valueOf(buyId);
            Optional<BuyFlat> buyFlat = this.buyFlatService.findById(flatId);
            if (buyFlat.isPresent()) { // Если удаляемая квартира существует
                ConfirmKeyboard confirmKeyboard = new ConfirmKeyboard();

                // Устанавливаю новый adminChoice - это buyFlat (для удаления)
                adminService.setAdminChoice(admin, adminService.getAdminChoiceFromFlat(buyFlat.get(),
                        admin.getAdminChoice().getMenuMessageId()));

                adminCache.addFlatToDelete(admin.getChatId(), flatId); // Сохраняю айдишник квартиры на удаление в кэше

                EditMessageText editMessageText = new EditMessageText(); // Сообщение с найденной квартирой
                editMessageText.setChatId(admin.getChatId().toString());
                editMessageText.setMessageId(admin.getAdminChoice().getMenuMessageId()); // Изменяю меню
                editMessageText.setReplyMarkup(confirmKeyboard.getKeyboard());
                editMessageText.enableHtml(true);
                editMessageText.setDisableWebPagePreview(true); // Не показывать превью телеграфа для удобства
                editMessageText.setText(admin.getAdminChoice().getHtmlMessage()); // Показываю adminChoice (что именно мы удаляем)

                answer.add(editMessageText);

                admin.setBotAdminState(AdminState.ADMIN_DELETE_BUY_CONFIRM); // Ждем подтверждения
            } else {
                EditMessageText editMessageText = new EditMessageText(); // Такого ID не существует
                editMessageText.setChatId(admin.getChatId().toString());
                editMessageText.setMessageId(admin.getAdminChoice().getMenuMessageId());
                editMessageText.setText(messagesVariables.getAdminDeleteWrongId());

                answer.add(editMessageText);

                admin.getAdminChoice().setMenuMessageId(null); // Удалил меню
                admin.setBotAdminState(AdminState.ADMIN_INIT); // Возвращаемся в исходное состояние
            }
            adminService.saveAdmin(admin); // Сохраняю измененное состояние
        } catch (NumberFormatException ex) {
            LOGGER.error(ex);
            // Ничего не делаем (ждем пока введет правильно)
        }
    }
}