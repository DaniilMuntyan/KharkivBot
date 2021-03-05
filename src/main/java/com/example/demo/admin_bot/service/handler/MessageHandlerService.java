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

        DeleteMessage deleteMessage = deleteApiMethod(message);

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());

        BotApiMethod<?> answer = null; // Если пришел любой другой текст - ничего не отправляем

        boolean isAdmin = false;

        if(user.isPresent() && adminService.isAdmin(user.get())) { // Если админ
            isAdmin = true;
            //response = handleAdminMessage(text, response, user.get());

            answer = handleAdminMessage(message, user.get());
        } else {
            if (adminService.isEnterAdminCommand(text)) { // Если ввел команду с паролем
                isAdmin = true;
                User newUser = new User(message);
                newUser.setAdminMode(true);
                newUser = userRepository.save(newUser);
                LOGGER.info("New admin: " + newUser.getName(true));
                answer = helloAdmin(response);
            }
        }
        if (!isAdmin) {
            return Collections.singletonList(answer);
        } else {
            return Arrays.asList(deleteMessage, answer);
        }
    }

    // Обработка сообщения от админа
    private BotApiMethod<?> handleAdminMessage(Message message, User admin) {
        String text = message.getText();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();

        boolean adminCommand = false;
        BotApiMethod<?> answer = null;

        /*
        // Если еще есть незаконченный процесс публикации квартиры - выходим.
        // Только одно меню может работать в данный момент времени.
        if (admin.getAdminChoice().getMenuMessageId() != null) {
            return null;
        }*/

        if (text.equals(menuVariables.getAddRentFlatBtnText())) { // Если выбрали "опубликовать квартиру для аренды"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                return null;
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddRentFlatButton");
            admin.setBotState(State.ADD_RENT_FLAT);
            userRepository.save(admin);
        }
        if (text.equals(menuVariables.getAddBuyFlatBtnText())) { // Если выбрали "опубликовать квартиру для покупки"
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                return null;
            }
            adminCommand = true;
            LOGGER.info("handleAdminMessage. AddBuyFlatButton");
            admin.setBotState(State.ADD_BUY_FLAT);
            userRepository.save(admin);
        }
        if(text.equals(Commands.START)) {
            adminCommand = false;
            admin.setBotState(State.INIT);
            admin.setAdminChoice(new AdminChoice());
            userRepository.save(admin);
        }

        if(adminCommand) { // Если пришла команда от администратора - обработать, в соответствии с состоянием
            answer = botStateService.processInput(message, admin);
        } else { // Если пришло любое текстовое сообщение
            if (admin.getBotState() == State.INIT) {
                LOGGER.info("handleAdminMessage. Any text message");
                SendMessage response = new SendMessage();
                response.setChatId(chatId.toString());
                response.setReplyMarkup(adminService.getMainMenu());
                response.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
                answer = response;
            }
        }

        return answer;
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
