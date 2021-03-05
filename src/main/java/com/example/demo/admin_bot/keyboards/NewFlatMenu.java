package com.example.demo.admin_bot.keyboards;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.model.AdminChoice;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class NewFlatMenu {
    private final MenuVariables menuVariables;
    @Setter
    @Getter
    private AdminChoice adminChoice;
    private InlineKeyboardMarkup newFlatMenu;

    public NewFlatMenu(boolean isRentFlat) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.adminChoice = new AdminChoice(isRentFlat);
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    public NewFlatMenu(AdminChoice adminChoice) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.adminChoice = adminChoice;
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    public void refreshKeyboard() {
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    private InlineKeyboardMarkup getNewFlatKeyboardMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<String> texts;
        texts = getTexts();

        InlineKeyboardButton buttonRooms = InlineKeyboardButton.builder()
                .text(texts.get(0))
                .callbackData(menuVariables.getAdminBtnCallbackRooms())
                .build();
        InlineKeyboardButton buttonSquare = InlineKeyboardButton.builder()
                .text(texts.get(1))
                .callbackData(menuVariables.getAdminBtnCallbackSquare())
                .build();
        InlineKeyboardButton buttonFloor = InlineKeyboardButton.builder()
                .text(texts.get(2))
                .callbackData(menuVariables.getAdminBtnCallbackFloor())
                .build();
        InlineKeyboardButton buttonAllFloor = InlineKeyboardButton.builder()
                .text(texts.get(3))
                .callbackData(menuVariables.getAdminBtnCallbackAllFloor())
                .build();
        InlineKeyboardButton buttonMetro = InlineKeyboardButton.builder()
                .text(texts.get(4))
                .callbackData(menuVariables.getAdminBtnCallbackMetro())
                .build();
        InlineKeyboardButton buttonAddress = InlineKeyboardButton.builder()
                .text(texts.get(5))
                .callbackData(menuVariables.getAdminBtnCallbackAddress())
                .build();
        InlineKeyboardButton buttonDistrict = InlineKeyboardButton.builder()
                .text(texts.get(6))
                .callbackData(menuVariables.getAdminBtnCallbackDistrict())
                .build();
        InlineKeyboardButton buttonMoney = InlineKeyboardButton.builder()
                .text(texts.get(7))
                .callbackData(menuVariables.getAdminBtnCallbackMoney())
                .build();
        InlineKeyboardButton buttonMoneyRange = InlineKeyboardButton.builder()
                .text(texts.get(8))
                .callbackData(menuVariables.getAdminBtnCallbackMoneyRange())
                .build();
        InlineKeyboardButton buttonTelegraph = InlineKeyboardButton.builder()
                .text(texts.get(9))
                .callbackData(menuVariables.getAdminBtnCallbackTelegraph())
                .build();
        InlineKeyboardButton buttonInfo = InlineKeyboardButton.builder()
                .text(texts.get(10))
                .callbackData(menuVariables.getAdminBtnCallbackInfo())
                .build();
        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(texts.get(11))
                .callbackData(menuVariables.getAdminBtnCallbackCancel())
                .build();

        List<InlineKeyboardButton> row1 = List.of(buttonRooms, buttonSquare);
        List<InlineKeyboardButton> row2 = List.of(buttonFloor, buttonAllFloor);
        List<InlineKeyboardButton> row3 = List.of(buttonMetro, buttonAddress);
        List<InlineKeyboardButton> row4 = List.of(buttonDistrict, buttonMoney);
        List<InlineKeyboardButton> row5 = List.of(buttonMoneyRange, buttonTelegraph);
        List<InlineKeyboardButton> row6 = List.of(buttonInfo);
        List<InlineKeyboardButton> row7 = List.of(buttonCancel);
        List<List<InlineKeyboardButton>> rowList = List.of(row1, row2, row3, row4, row5, row6, row7);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private List<String> getTexts() {
        List<String> texts = new ArrayList<>();
        String no = "нет";
        texts.add(menuVariables.getAdminBtnRooms() + " " +
                (adminChoice.getRooms() == null ? no : adminChoice.getRooms().toString().toLowerCase()));
        texts.add(menuVariables.getAdminBtnSquare() + " " +
                (adminChoice.getSquare() == null ? no : adminChoice.getSquare()));
        texts.add(menuVariables.getAdminBtnFloor() + " " +
                (adminChoice.getFloor() == null ? no : adminChoice.getFloor()));
        texts.add(menuVariables.getAdminBtnAllFloor() + " " +
                (adminChoice.getAllFloors() == null ? no : adminChoice.getAllFloors()));
        texts.add(menuVariables.getAdminBtnMetro() + " " +
                (adminChoice.getMetro() == null ? no : adminChoice.getMetro()));
        texts.add(menuVariables.getAdminBtnAddress() + " " +
                (adminChoice.getAddress() == null ? no : adminChoice.getAddress()));
        texts.add(menuVariables.getAdminBtnDistrict() + " " +
                (adminChoice.getDistrict() == null ? no : adminChoice.getDistrict()));
        texts.add(menuVariables.getAdminBtnMoney() + " " +
                (adminChoice.getMoney() == null ? no : adminChoice.getMoney()));
        texts.add(menuVariables.getAdminBtnMoneyRange() + " " +
                (adminChoice.getMoneyRange() == null ? no : adminChoice.getMoneyRange()));
        texts.add(menuVariables.getAdminBtnTelegraph() + " " +
                (adminChoice.getTelegraph() == null ? no : adminChoice.getTelegraph()));
        texts.add(menuVariables.getAdminBtnInfo() + " " +
                (adminChoice.getInfo() == null ? no : adminChoice.getInfo()));
        texts.add(menuVariables.getAdminBtnCancel());
        return texts;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return newFlatMenu;
    }
}
