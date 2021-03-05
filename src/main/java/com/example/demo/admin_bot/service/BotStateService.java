package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.admin_bot.utils.State;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class BotStateService {
    private static final Logger LOGGER = Logger.getLogger(BotStateService.class);

    private final AdminService adminService;
    private final MenuVariables menuVariables;

    @Autowired
    public BotStateService(AdminService adminService, MenuVariables menuVariables) {
        this.adminService = adminService;
        this.menuVariables = menuVariables;
    }

    public BotApiMethod<?> processInput(Message message, User admin) {
        BotApiMethod<?> answer = null;
        //boolean editMessage = admin.getAdminChoice().getMenuMessageId() != null;

        if(admin.getBotState() == State.ADD_BUY_FLAT) {
            NewFlatMenu newFlatMenu = adminService.getAddBuyFlatMenu();
            AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
            admin.setAdminChoice(newAdminChoice);
            User newAdmin = adminService.saveAdmin(admin);
            LOGGER.info("newAdminChoice: " + newAdminChoice + ".\nFor admin: " + newAdmin.getId());

            answer = new SendMessage();
            ((SendMessage) answer).setChatId(message.getChatId().toString());
            ((SendMessage) answer).setText(menuVariables.getAdminAddBuyFlatText());
            ((SendMessage) answer).setReplyMarkup(newFlatMenu.getKeyboard());
        }
        if(admin.getBotState() == State.ADD_RENT_FLAT) {
            NewFlatMenu newFlatMenu = adminService.getAddRentFlatMenu();
            AdminChoice newAdminChoice = adminService.saveChoice(newFlatMenu);
            admin.setAdminChoice(newAdminChoice);
            User newAdmin = adminService.saveAdmin(admin);
            LOGGER.info("newAdminChoice: " + newAdminChoice + ".\nFor admin: " + newAdmin.getId());

            answer = new SendMessage();
            ((SendMessage) answer).setChatId(message.getChatId().toString());
            ((SendMessage) answer).setText(menuVariables.getAdminAddRentFlatText());
            ((SendMessage) answer).setReplyMarkup(newFlatMenu.getKeyboard());
        }

        return answer;
    }
}
