package com.example.demo.admin_bot.service.handler;

import com.example.demo.admin_bot.constants.Commands;
import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.service.BotStateService;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.*;

@Service
public class MessageHandlerService {
    private static final Logger LOGGER = Logger.getLogger(MessageHandlerService.class);

    private final UserRepository userRepository;
    private final AdminService adminService;
    private final MessagesVariables messagesVariables;
    private final MenuVariables menuVariables;
    private final BotStateService botStateService;

    @Autowired
    public MessageHandlerService(UserRepository userRepository, AdminService adminService, MessagesVariables messagesVariables, MenuVariables menuVariables, BotStateService botStateService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.messagesVariables = messagesVariables;
        this.menuVariables = menuVariables;
        this.botStateService = botStateService;
    }

    public List<BotApiMethod<?>> handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText().trim();
        Optional<User> user = userRepository.findByChatId(chatId);

        List<BotApiMethod<?>> response = new ArrayList<>();

        DeleteMessage deleteMessage = deleteApiMethod(message);

        SendMessage textResponse = new SendMessage();
        textResponse.setChatId(chatId.toString());

        boolean isAdmin = false;

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            isAdmin = true;
            response.addAll(handleAdminMessage(message, user.get()));
        } else {
            if (adminService.isEnterAdminCommand(text)) { // Если ввел команду с паролем
                isAdmin = true;
                User newUser = new User(message);
                newUser.setAdminMode(true);
                newUser = userRepository.save(newUser);
                LOGGER.info("New admin: " + newUser.getName(true));
                response.add(helloAdmin(textResponse));
            }
        }

        if (isAdmin) {
            response.add(0, deleteMessage);
        }

        return response;
    }

    // Обработка сообщения от админа
    private List<BotApiMethod<?>> handleAdminMessage(Message message, User admin) {
        String text = message.getText();
        Long chatId = message.getChatId();

        List<BotApiMethod<?>> response = new ArrayList<>();

        boolean adminCommand = false;

        if (text.equals(menuVariables.getAddRentFlatBtnText())) { // Если выбрали "опубликовать квартиру для аренды"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddRentFlatButton");
            admin.setBotState(State.ADD_RENT_FLAT);
        }
        if (text.equals(menuVariables.getAddBuyFlatBtnText())) { // Если выбрали "опубликовать квартиру для покупки"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                return response; // Возвращаем пустой
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddBuyFlatButton");
            admin.setBotState(State.ADD_BUY_FLAT);
        }
        if(text.equals(Commands.START)) {
            adminCommand = true;
            admin.setBotState(State.INIT);
        }
        if(admin.getBotState() == State.SUBMENU_SQUARE) { // Если ожидаем площадь
            LOGGER.info("IN MESSAGE_HANDLER IF!!!");
            adminCommand = true;
        }

        if(adminCommand) { // Если пришла команда от администратора - обработать, в соответствии с состоянием
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
}
