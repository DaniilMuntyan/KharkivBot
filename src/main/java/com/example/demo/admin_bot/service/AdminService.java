package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.constants.Commands;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.constants.ProgramVariables;
import com.example.demo.model.AdminChoice;
import com.example.demo.model.User;
import com.example.demo.repo.AdminChoiceRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final ProgramVariables programVariables;
    private final MenuVariables menuVariables;
    private final AdminChoiceRepository adminChoiceRepository;
    private final UserRepository userRepository;

    private ReplyKeyboardMarkup mainMenu;
    private AnswerCallbackQuery answerCallbackQuery;

    @Autowired
    public AdminService(ProgramVariables programVariables, MenuVariables menuVariables, AdminChoiceRepository adminChoiceRepository, UserRepository userRepository) {
        this.programVariables = programVariables;
        this.menuVariables = menuVariables;
        this.adminChoiceRepository = adminChoiceRepository;
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
        row1.add(new KeyboardButton(menuVariables.getAddRentFlatBtnText()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(menuVariables.getAddBuyFlatBtnText()));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        this.mainMenu = replyKeyboardMarkup;

        return this.mainMenu;
    }

    public AnswerCallbackQuery getAnswerCallback(CallbackQuery callbackQuery) {
        if(this.answerCallbackQuery != null) {
            return this.answerCallbackQuery;
        }

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        this.answerCallbackQuery = answerCallbackQuery;

        return this.answerCallbackQuery;
    }

    public NewFlatMenu getAddBuyFlatMenu() {
        return new NewFlatMenu(false);
    }

    public NewFlatMenu getAddBuyFlatMenu(AdminChoice adminChoice) {
        return new NewFlatMenu(adminChoice);
    }

    public NewFlatMenu getAddRentFlatMenu() {
        return new NewFlatMenu(true);
    }

    public NewFlatMenu getAddRentFlatMenu(AdminChoice adminChoice) {
        return new NewFlatMenu(adminChoice);
    }

    public AdminChoice saveChoice(NewFlatMenu newFlatMenu) {
        return adminChoiceRepository.save(newFlatMenu.getAdminChoice());
    }

    public User saveAdmin(User admin) {
        return userRepository.save(admin);
    }
}
