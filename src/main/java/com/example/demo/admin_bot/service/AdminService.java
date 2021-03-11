package com.example.demo.admin_bot.service;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.Commands;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final ProgramVariables programVariables;
    private final AdminMenuVariables adminMenuVariables;
    private final UserRepository userRepository;
    private final AdminChoiceService adminChoiceService;

    private ReplyKeyboardMarkup mainMenu;

    @Autowired
    public AdminService(ProgramVariables programVariables, AdminMenuVariables adminMenuVariables, AdminChoiceService adminChoiceService, UserRepository userRepository) {
        this.programVariables = programVariables;
        this.adminMenuVariables = adminMenuVariables;
        this.adminChoiceService = adminChoiceService;
        this.userRepository = userRepository;
    }

    public boolean isAdmin(User user) {
        return user.isAdminMode();
    }

    public boolean isEnterAdminCommand(String text) {
        String[] arrayString = text.trim().split(" ");
        return arrayString.length == 2 && arrayString[0].equals(Commands.ADMIN) &&
                arrayString[1].equals(programVariables.getAdminPassword());
    }

    public ReplyKeyboardMarkup getMainMenu() {
        if (mainMenu != null) {
            return mainMenu;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(adminMenuVariables.getAddRentFlatBtnText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(adminMenuVariables.getAddBuyFlatBtnText()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(adminMenuVariables.getBulkMessageText()));


        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);
        this.mainMenu = replyKeyboardMarkup;

        return this.mainMenu;
    }

    public AnswerCallbackQuery getAnswerCallback(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

    public NewFlatMenu getAddBuyFlatMenu() {
        return new NewFlatMenu(false);
    }

    public NewFlatMenu getAddRentFlatMenu() {
        return new NewFlatMenu(true);
    }

    public AdminChoice saveChoice(NewFlatMenu newFlatMenu) {
        return adminChoiceService.saveChoice(newFlatMenu.getAdminChoice());
    }

    public AdminChoice saveChoice(AdminChoice adminChoice) {
        return adminChoiceService.saveChoice(adminChoice);
    }

    public User saveAdmin(User admin) {
        return userRepository.save(admin);
    }

    public void saveAdminState(User admin) {
        userRepository.editAdminState(admin.getId(), admin.getBotAdminState().ordinal());
    }

    // Чтобы установить новый adminChoice для админа - сохранить новый и удалить предыдущий.
    // Чтобы не хранился в базе
    public void setAdminChoice(User admin, AdminChoice newAdminChoice) {
        AdminChoice prevAdminChoice = admin.getAdminChoice();
        this.adminChoiceService.saveChoice(newAdminChoice);
        admin.setAdminChoice(newAdminChoice);
        this.saveAdmin(admin);
        if (admin.getAdminChoice() != null) {
            this.adminChoiceService.deleteChoice(prevAdminChoice);
        }
    }
}
