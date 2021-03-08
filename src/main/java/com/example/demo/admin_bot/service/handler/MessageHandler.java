package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.constants.Commands;
import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.BotStateService;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.common_part.model.AdminChoice;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.*;

@Service
public class MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class);

    private final UserRepository userRepository;
    private final AdminService adminService;
    private final MessagesVariables messagesVariables;
    private final MenuVariables menuVariables;
    private final BotStateService botStateService;

    @Autowired
    public MessageHandler(UserRepository userRepository, AdminService adminService, MessagesVariables messagesVariables, MenuVariables menuVariables, BotStateService botStateService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.messagesVariables = messagesVariables;
        this.menuVariables = menuVariables;
        this.botStateService = botStateService;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText().trim();
        String username = message.getFrom().getUserName(); // Обновляю дый раз, когда получаю новое сообщение
        Optional<User> user = userRepository.findByChatId(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(chatId.toString());

        boolean isAdmin = false;

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            isAdmin = true;
            response.addAll(handleAdminMessage(message, user.get(), username));
        } else {
            if (adminService.isEnterAdminCommand(text)) { // Если ввел команду с паролем
                isAdmin = true;
                if (user.isPresent()) { // Если админ уже есть в базе
                    User admin = user.get();
                    admin.setAdminMode(true);
                    admin = userRepository.save(admin);
                    LOGGER.info("Admin entered: " + admin.getName(true));
                } else { // Если админа нет в базе, он первый раз заходит в режим админа
                    User newUser = new User(message);
                    newUser.setAdminMode(true);
                    newUser = userRepository.save(newUser);
                    LOGGER.info("New admin: " + newUser.getName(true));
                }
                response.add(helloAdmin(textResponse));
            }
        }

        return response;
    }

    // Обработка сообщения от админа
    private List<BotApiMethod<?>> handleAdminMessage(Message message, User admin, String username) {
        String text = message.getText();
        Long chatId = message.getChatId();

        List<BotApiMethod<?>> response = new ArrayList<>();

        boolean adminCommand = false;

        if(!admin.getUsername().equals(username)) { // Если админ поменял свой юзернейм
            admin.setUsername(username);
        }

        if (text.equals(menuVariables.getAddRentFlatBtnText())) { // Если выбрали "опубликовать квартиру для аренды"
            if (admin.getAdminChoice().getMenuMessageId() != null) { // Если процесс публикации не завершен
                response.add(deleteApiMethod(message));
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddRentFlatButton");
            admin.setBotState(State.ADD_RENT_FLAT);
        }
        if (text.equals(menuVariables.getAddBuyFlatBtnText())) { // Если выбрали "опубликовать квартиру для покупки"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(deleteApiMethod(message));
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddBuyFlatButton");
            admin.setBotState(State.ADD_BUY_FLAT);
        }
        if(text.equals(menuVariables.getBulkMessageText())) { // Если выбрали "Написать сообщение"
            if(admin.getAdminChoice().getMenuMessageId() != null) {
                response.add(deleteApiMethod(message));
                return response;
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. BulkMessage");
            admin.setBotState(State.WRITE_MESSAGE);
        }
        if (text.equals(Commands.START)) {
            adminCommand = true;
            admin.setBotState(State.INIT);
        }
        if (text.equals(Commands.EXIT)) {
            adminCommand = true;
            admin.setBotState(State.EXIT);
        }
        if (checkMenuBotState(admin.getBotState())) {
            adminCommand = true;
        }

        if (adminCommand) { // Если пришла команда от администратора - обработать, в соответствии с состоянием
            response.addAll(botStateService.processInput(message, admin));
        } else { // Если пришло любое текстовое сообщение
            if (admin.getBotState() == State.INIT) {
                LOGGER.info("handleAdminMessage. Any text message");
                SendMessage textResponse = new SendMessage();
                textResponse.setChatId(chatId.toString());
                textResponse.setReplyMarkup(adminService.getMainMenu());
                textResponse.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
                response.add(textResponse);
            }
        }

        // Если не ждем сообщение от админа - то удаляем ненужное
        // И если не находимся в начальном состоянии (идет какой-то процесс)
        if(admin.getBotState() != State.WAIT_MESSAGE && admin.getBotState() != State.INIT) {
            response.add(0, deleteApiMethod(message));
        }

        return response;
    }

    // Зашли в режим админа
    private BotApiMethod<?> helloAdmin(SendMessage response) {
        response.setText(MessageFormat.format(messagesVariables.getHelloAdmin(), Emoji.KEY));
        response.setReplyMarkup(adminService.getMainMenu());
        return response;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private boolean checkMenuBotState(State botState) {
        return botState == State.SUBMENU_SQUARE || botState == State.SUBMENU_FLOOR ||
                botState == State.SUBMENU_ALL_FLOORS || botState == State.SUBMENU_METRO ||
                botState == State.SUBMENU_ADDRESS || botState == State.SUBMENU_PRICE ||
                botState == State.SUBMENU_PHOTO || botState == State.SUBMENU_INFO ||
                botState == State.SUBMENU_CONTACT || botState == State.SUBMENU_MAP ||
                botState == State.WAIT_MESSAGE;
    }
}
