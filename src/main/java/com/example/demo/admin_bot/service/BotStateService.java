package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotStateService {
    private static final Logger LOGGER = Logger.getLogger(BotStateService.class);

    private final AdminService adminService;
    private final MenuVariables menuVariables;
    private final MessagesVariables messagesVariables;

    @Autowired
    public BotStateService(AdminService adminService, MenuVariables menuVariables, MessagesVariables messagesVariables) {
        this.adminService = adminService;
        this.menuVariables = menuVariables;
        this.messagesVariables = messagesVariables;
    }

    public List<BotApiMethod<?>> processInput(Message message, User admin) {
        List<BotApiMethod<?>> answer = new ArrayList<>();
        //boolean editMessage = admin.getAdminChoice().getMenuMessageId() != null;

        // Если прислали /start - удалить текущее меню и прислать сообщение
        if(admin.getBotState() == State.INIT) {
            if (admin.getAdminChoice().getMenuMessageId() != null) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setMessageId(admin.getAdminChoice().getMenuMessageId());
                deleteMessage.setChatId(message.getChatId().toString());
                answer.add(deleteMessage);
            }

            SendMessage textResponse = new SendMessage();
            textResponse.setChatId(message.getChatId().toString());
            textResponse.setReplyMarkup(adminService.getMainMenu());
            textResponse.setText(MessageFormat.format(messagesVariables.getAdminHi(), Emoji.HI, admin.getName(false)));
            answer.add(textResponse);

            admin.setAdminChoice(new AdminChoice());

            adminService.saveAdmin(admin); // Сохраняем измененное состояние администратора
        }

        // Если нажали на кнопку "Добавить квартиру (продажа)"
        if(admin.getBotState() == State.ADD_BUY_FLAT) {
            NewFlatMenu newFlatMenu = adminService.getAddBuyFlatMenu();
            AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
            admin.setAdminChoice(newAdminChoice);
            User newAdmin = adminService.saveAdmin(admin);
            LOGGER.info("newAdminChoice: " + newAdminChoice + ".\nFor admin: " + newAdmin.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(menuVariables.getAdminAddBuyFlatText());
            sendMessage.setReplyMarkup(newFlatMenu.getKeyboard());
            answer.add(sendMessage);

            adminService.saveAdmin(admin); // Сохраняем измененное состояние администратора
        }

        // Если нажали на кнопку "Добавить квартиру (аренда)"
        if(admin.getBotState() == State.ADD_RENT_FLAT) {
            NewFlatMenu newFlatMenu = adminService.getAddRentFlatMenu();
            AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
            admin.setAdminChoice(newAdminChoice);
            User newAdmin = adminService.saveAdmin(admin);
            LOGGER.info("newAdminChoice: " + newAdminChoice + ".\nFor admin: " + newAdmin.getId());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(menuVariables.getAdminAddRentFlatText());
            sendMessage.setReplyMarkup(newFlatMenu.getKeyboard());
            answer.add(sendMessage);

            adminService.saveAdmin(admin); // Сохраняем измененное состояние администратора
        }

        // Ввели площадь
        if(admin.getBotState() == State.SUBMENU_SQUARE) {
            try {
                Float square = Float.valueOf(message.getText().trim());
                if (square.equals(Float.POSITIVE_INFINITY) || square.equals(Float.NEGATIVE_INFINITY)) {
                    throw new NumberFormatException("Square cannot be INFINITY!");
                }
                admin.getAdminChoice().setSquare(square);
                adminService.saveAdmin(admin);
                answer.add(getEditKeyboard(message.getChatId(), admin.getAdminChoice().getMenuMessageId(), admin));
            } catch (NumberFormatException ex) {
                answer.add(deleteApiMethod(message));
            }
        }

        return answer;
    }

    private DeleteMessage deleteApiMethod(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .build();
    }

    private EditMessageReplyMarkup getEditKeyboard(Long chatId, Integer messageId, User admin) {
        NewFlatMenu newFlatMenu = new NewFlatMenu(admin.getAdminChoice());
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setChatId(chatId.toString());
        editMessageReplyMarkup.setReplyMarkup(newFlatMenu.getKeyboard());
        return editMessageReplyMarkup;
    }
}
