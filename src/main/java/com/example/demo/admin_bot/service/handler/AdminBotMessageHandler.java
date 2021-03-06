package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.admin_bot.service.AdminBotStateService;
import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.admin_bot.utils.AdminCommands;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.common_part.service.RefreshUserDataService;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.DeleteMessageService;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.*;

@Service
public class AdminBotMessageHandler {
    private static final Logger LOGGER = Logger.getLogger(AdminBotMessageHandler.class);

    private final DeleteMessageService deleteMessageService;
    private final AdminService adminService;
    private final MessagesVariables messagesVariables;
    private final AdminMenuVariables adminMenuVariables;
    private final AdminBotStateService adminBotStateService;
    private final UserService userService;
    private final RefreshUserDataService refreshUserDataService;

    private final DataCache dataCache;

    @Autowired
    public AdminBotMessageHandler(DeleteMessageService deleteMessageService, AdminService adminService, MessagesVariables messagesVariables, AdminMenuVariables adminMenuVariables, AdminBotStateService adminBotStateService, UserService userService, RefreshUserDataService refreshUserDataService, DataCache dataCache) {
        this.deleteMessageService = deleteMessageService;
        this.adminService = adminService;
        this.messagesVariables = messagesVariables;
        this.adminMenuVariables = adminMenuVariables;
        this.adminBotStateService = adminBotStateService;
        this.userService = userService;
        this.refreshUserDataService = refreshUserDataService;
        this.dataCache = dataCache;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText().trim();

        // Достаю админа из базы или из кэша
        Optional<UserCache> user = userService.findUserInCacheOrDb(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(chatId.toString());

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            response.addAll(handleAdminMessage(message, user.get()));
        } else {
            if (adminService.isEnterAdminCommand(text)) { // Если ввел команду с паролем
                if (user.isPresent()) { // Если админ уже есть в кэше
                    UserCache admin = user.get();
                    admin.setAdmin(true);
                    if (admin.getAdminChoice() == null) { // Если админ в базе есть, но AdminChoice еще не имеет
                        admin.setAdminChoice(new AdminChoice());
                    }
                    this.dataCache.saveUserCache(admin);
                } else { // Если админа нет в кэше, он первый раз заходит в режим админа
                    User newUser = new User(message);
                    newUser.setAdminMode(true);
                    newUser.setAdminChoice(new AdminChoice());
                    newUser.setBotAdminState(AdminState.ADMIN_INIT);
                    userService.saveNewUser(newUser); // Сохраняю в базу и кэш
                }
                response.add(helloAdmin(textResponse));
            }
        }
        return response;
    }

    // Обработка сообщения от админа
    private List<BotApiMethod<?>> handleAdminMessage(Message message, UserCache admin) {
        String text = message.getText();
        Long chatId = message.getChatId();

        List<BotApiMethod<?>> response = new ArrayList<>();

        boolean adminCommand = false;

        this.refreshUserDataService.refreshUserName(message); // Обновляю firstname, lastname и username

        if (text.equals(adminMenuVariables.getAddRentFlatBtnText())) { // Если выбрали "опубликовать квартиру для аренды"
            if (admin.getAdminChoice().getMenuMessageId() != null) { // Если процесс публикации не завершен
                response.add(this.deleteMessageService.deleteApiMethod(message));
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_ADD_RENT_FLAT);
        }
        if (text.equals(adminMenuVariables.getAddBuyFlatBtnText())) { // Если выбрали "опубликовать квартиру для покупки"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(this.deleteMessageService.deleteApiMethod(message));
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_ADD_BUY_FLAT);
        }
        if (text.equals(adminMenuVariables.getDeleteRentFlat())) { // Если выбрали "Удалить квартиру (аренда)"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(this.deleteMessageService.deleteApiMethod(message));
                return response;
            }
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_DELETE_RENT_FLAT);
        }
        if (text.equals(adminMenuVariables.getDeleteBuyFlat())) { // Если выбрали "Удалить квартиру (продажа)"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(this.deleteMessageService.deleteApiMethod(message));
                return response;
            }
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_DELETE_BUY_FLAT);
        }
        if(text.equals(adminMenuVariables.getBulkMessageText())) { // Если выбрали "Написать сообщение"
            if(admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(this.deleteMessageService.deleteApiMethod(message));
                return response;
            }
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_WRITE_MESSAGE);
        }
        if (text.equals(AdminCommands.START)) {
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_INIT);
        }
        if (text.equals(AdminCommands.EXIT)) {
            adminCommand = true;
            admin.setBotAdminState(AdminState.ADMIN_EXIT);
        }
        if (admin.getBotAdminState() == AdminState.ADMIN_DELETE_WAIT_RENT_ID ||
                admin.getBotAdminState() == AdminState.ADMIN_DELETE_WAIT_BUY_ID) { // Если ждем ID
            adminCommand = true;
        }
        if (checkMenuBotState(admin.getBotAdminState())) {
            adminCommand = true;
        }

        if (adminCommand) { // Если пришла команда от администратора - обработать, в соответствии с состоянием
            response.addAll(adminBotStateService.processAdminInput(message, admin));
        } else { // Если пришло любое текстовое сообщение
            if (admin.getBotAdminState() == AdminState.ADMIN_INIT) {
                SendMessage textResponse = new SendMessage();
                textResponse.setChatId(chatId.toString());
                textResponse.setReplyMarkup(adminService.getMainMenu());
                textResponse.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.WAVE, admin.getName(false)));
                response.add(textResponse);
            }
        }

        // Если не ждем сообщение от админа - то удаляем ненужное
        // И если не находимся в начальном состоянии (идет какой-то процесс)
        if(admin.getBotAdminState() != AdminState.ADMIN_WAIT_MESSAGE_TO_BULK && admin.getBotAdminState() != AdminState.ADMIN_INIT) {
            response.add(0, this.deleteMessageService.deleteApiMethod(message));
        }
        
        this.dataCache.saveUserCache(admin);

        return response;
    }

    // Зашли в режим админа
    private BotApiMethod<?> helloAdmin(SendMessage response) {
        response.setText(MessageFormat.format(messagesVariables.getHelloAdmin(), Emoji.KEY));
        response.setReplyMarkup(adminService.getMainMenu());
        return response;
    }

    private boolean checkMenuBotState(AdminState botAdminState) {
        return botAdminState == AdminState.ADMIN_SUBMENU_SQUARE || botAdminState == AdminState.ADMIN_SUBMENU_FLOOR ||
                botAdminState == AdminState.ADMIN_SUBMENU_ALL_FLOORS || botAdminState == AdminState.ADMIN_SUBMENU_METRO ||
                botAdminState == AdminState.ADMIN_SUBMENU_ADDRESS || botAdminState == AdminState.ADMIN_SUBMENU_PRICE ||
                botAdminState == AdminState.ADMIN_SUBMENU_PHOTO || botAdminState == AdminState.ADMIN_SUBMENU_INFO ||
                botAdminState == AdminState.ADMIN_SUBMENU_CONTACT || botAdminState == AdminState.ADMIN_SUBMENU_MAP ||
                botAdminState == AdminState.ADMIN_WAIT_MESSAGE_TO_BULK ;
    }
}
