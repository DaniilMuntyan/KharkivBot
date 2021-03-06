package com.example.demo.admin_bot.keyboards;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
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

    // В зависимости от параметров объекта AdminChoice пишу текста на кнопках
    private List<String> getTexts() {
        List<String> texts = new ArrayList<>();
        String no = "нет";
        String selected = Emoji.SELECTED.toString();
        texts.add(adminChoice.getRooms() == null ? menuVariables.getAdminBtnRooms() + " " + no:
                selected + " " + menuVariables.getAdminBtnRooms() + " " +
                        adminChoice.getRooms().toString().toLowerCase());
        texts.add(adminChoice.getSquare() == null ? menuVariables.getAdminBtnSquare() + " " + no:
                selected + " " + menuVariables.getAdminBtnSquare() + " " + adminChoice.getSquare());
        texts.add(adminChoice.getFloor() == null ? menuVariables.getAdminBtnFloor() + " " + no:
                selected + " " + menuVariables.getAdminBtnFloor() + " " + adminChoice.getFloor());
        texts.add(adminChoice.getAllFloors() == null ? menuVariables.getAdminBtnAllFloor() + " " + no:
                selected + " " + menuVariables.getAdminBtnAllFloor() + " " + adminChoice.getAllFloors());
        texts.add(adminChoice.getMetro() == null ? menuVariables.getAdminBtnMetro() + " " + no:
                selected + " " + menuVariables.getAdminBtnMetro() + " " + adminChoice.getMetro());
        texts.add(adminChoice.getAddress() == null ? menuVariables.getAdminBtnAddress() + " " + no:
                selected + " " + menuVariables.getAdminBtnAddress() + " " + adminChoice.getAddress());
        texts.add(adminChoice.getDistrict() == null ? menuVariables.getAdminBtnDistrict() + " " + no:
                selected + " " + menuVariables.getAdminBtnDistrict() + " " + adminChoice.getDistrict());
        texts.add(adminChoice.getMoney() == null ? menuVariables.getAdminBtnMoney() + " " + no:
                selected + " " + menuVariables.getAdminBtnMoney() + " " + adminChoice.getMoney());
        texts.add(adminChoice.getMoneyRange() == null ? menuVariables.getAdminBtnMoneyRange() + " " + no:
                selected + " " + menuVariables.getAdminBtnMoneyRange() + " " + adminChoice.getMoneyRange());
        texts.add(adminChoice.getTelegraph() == null ? menuVariables.getAdminBtnTelegraph() + " " + no:
                selected + " " + menuVariables.getAdminBtnTelegraph() + " " + adminChoice.getTelegraph());
        texts.add(adminChoice.getInfo() == null ? menuVariables.getAdminBtnInfo() + " " + no:
                selected + " " + menuVariables.getAdminBtnInfo() + " " + adminChoice.getInfo());
        texts.add(menuVariables.getAdminBtnCancel());
        return texts;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return newFlatMenu;
    }
}
