package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.keyboards.MainMenuKeyboard;
import com.example.demo.admin_bot.utils.AdminCommands;
import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.cache.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class AdminService {
    private final ProgramVariables programVariables;
    private final UserRepository userRepository;
    private final AdminChoiceService adminChoiceService;

    private final DataCache dataCache;

    private final MainMenuKeyboard mainMenuKeyboard;

    @Autowired
    public AdminService(ProgramVariables programVariables, AdminChoiceService adminChoiceService, UserRepository userRepository, DataCache dataCache, MainMenuKeyboard mainMenuKeyboard) {
        this.programVariables = programVariables;
        this.adminChoiceService = adminChoiceService;
        this.userRepository = userRepository;
        this.dataCache = dataCache;
        this.mainMenuKeyboard = mainMenuKeyboard;
    }

    public boolean isAdmin(User user) {
        return user.isAdminMode();
    }

    public boolean isEnterAdminCommand(String text) {
        String[] arrayString = text.trim().split(" ");
        return arrayString.length == 2 && arrayString[0].equals(AdminCommands.ADMIN) &&
                arrayString[1].equals(programVariables.getAdminPassword());
    }

    public AnswerCallbackQuery getAnswerCallback(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

    public ReplyKeyboardMarkup getMainMenu() {
        return this.mainMenuKeyboard.getMainMenu();
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

    public void saveAdmin(User admin) {
        dataCache.saveUser(admin); // Сохраняю админа в кэше юзеров, чтобы потом кэш не перезаписал его в базе
        userRepository.save(admin);
    }

    public void saveAdminState(User admin) {
        userRepository.editAdminState(admin.getId(), admin.getBotAdminState().ordinal());
        dataCache.saveUser(admin); // Сохраняю админа в кэше (чтобы запись по расписанию не перезаписала изменения)
    }
    
    public AdminChoice getAdminChoiceFromFlat(RentFlat rentFlat, Integer menuMessageId) {
        AdminChoice adminChoice = new AdminChoice();
        adminChoice.setIsRentFlat(true);
        adminChoice.setAddress(rentFlat.getAddress());
        adminChoice.setFlatId(rentFlat.getId());
        adminChoice.setAllFloors(rentFlat.getAllFloors());
        adminChoice.setContact(rentFlat.getContact());
        adminChoice.setFloor(rentFlat.getFloor());
        adminChoice.setDistrict(rentFlat.getDistrict());
        adminChoice.setMapLink(rentFlat.getMapLink());
        adminChoice.setInfo(rentFlat.getInfo());
        adminChoice.setMetro(rentFlat.getMetro());
        adminChoice.setMoney(rentFlat.getMoney());
        adminChoice.setMoneyRange(rentFlat.getRange().toString());
        adminChoice.setRooms(rentFlat.getRooms());
        adminChoice.setSquare(rentFlat.getSquare());
        adminChoice.setTelegraph(rentFlat.getTelegraph());
        adminChoice.setMenuMessageId(menuMessageId);
        return adminChoice;
    }
    public AdminChoice getAdminChoiceFromFlat(BuyFlat buyFlat, Integer menuMessageId) {
        AdminChoice adminChoice = new AdminChoice();
        adminChoice.setIsRentFlat(false);
        adminChoice.setAddress(buyFlat.getAddress());
        adminChoice.setFlatId(buyFlat.getId());
        adminChoice.setAllFloors(buyFlat.getAllFloors());
        adminChoice.setContact(buyFlat.getContact());
        adminChoice.setFloor(buyFlat.getFloor());
        adminChoice.setDistrict(buyFlat.getDistrict());
        adminChoice.setMapLink(buyFlat.getMapLink());
        adminChoice.setInfo(buyFlat.getInfo());
        adminChoice.setMetro(buyFlat.getMetro());
        adminChoice.setMoney(buyFlat.getMoney());
        adminChoice.setMoneyRange(buyFlat.getRange().toString());
        adminChoice.setRooms(buyFlat.getRooms());
        adminChoice.setSquare(buyFlat.getSquare());
        adminChoice.setTelegraph(buyFlat.getTelegraph());
        adminChoice.setMenuMessageId(menuMessageId);
        return adminChoice;
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
