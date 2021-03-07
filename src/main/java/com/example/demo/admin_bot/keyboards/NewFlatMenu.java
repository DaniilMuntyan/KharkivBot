package com.example.demo.admin_bot.keyboards;

import com.example.demo.admin_bot.constants.MenuVariables;
import com.example.demo.admin_bot.utils.AdminChoiceParameter;
import com.example.demo.admin_bot.utils.BeanUtil;
import com.example.demo.admin_bot.utils.Emoji;
import com.example.demo.common_part.model.AdminChoice;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NewFlatMenu {
    private static final Logger LOGGER = Logger.getLogger(NewFlatMenu.class);

    private final MenuVariables menuVariables;
    @Getter
    private final AdminChoice adminChoice;
    private final InlineKeyboardMarkup newFlatMenu;
    private final HashMap<AdminChoiceParameter, String> adminChoiceMap;
    private final String no = "нет";

    public NewFlatMenu(boolean isRentFlat) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.adminChoice = new AdminChoice(isRentFlat);
        // Объект AdminChoice в виде словаря, для доступа к значениям выбора
        this.adminChoiceMap = getHashMapFromAdminChoice();
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    public NewFlatMenu(AdminChoice adminChoice) {
        this.menuVariables = BeanUtil.getBean(MenuVariables.class);
        this.adminChoice = adminChoice;
        // Объект AdminChoice в виде словаря, для доступа к значениям выбора
        this.adminChoiceMap = getHashMapFromAdminChoice();
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    private InlineKeyboardMarkup getNewFlatKeyboardMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<String> texts = new ArrayList<>(), callbacks = new ArrayList<>();
        setTextsAndCallbacks(texts, callbacks);

        List<List<InlineKeyboardButton>> adminChoiceButtons = new ArrayList<>();

        int i;
        if (texts.size() > 1) {
            for (i = 1; i < texts.size(); i += 2) {
                InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                        .text(texts.get(i - 1))
                        .callbackData(callbacks.get(i - 1))
                        .build();
                InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                        .text(texts.get(i))
                        .callbackData(callbacks.get(i))
                        .build();
                adminChoiceButtons.add(List.of(button1, button2));
            }
            if (i == texts.size()) { // Если одна кнопка осталась нечетной
                InlineKeyboardButton buttonEnd = InlineKeyboardButton.builder()
                        .text(texts.get(i - 1))
                        .callbackData(callbacks.get(i - 1))
                        .build();
                adminChoiceButtons.add(List.of(buttonEnd));
            }
        } else {
            InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                    .text(texts.get(0))
                    .callbackData(callbacks.get(0))
                    .build();
            adminChoiceButtons.add(List.of(button1));
        }

        boolean publish = checkForPublish();
        if (publish) {
            InlineKeyboardButton buttonPublish = InlineKeyboardButton.builder()
                    .text(menuVariables.getAdminBtnPublish())
                    .callbackData(menuVariables.getAdminBtnCallbackPublish())
                    .build();
            adminChoiceButtons.add(List.of(buttonPublish));
        }

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(menuVariables.getAdminBtnCancel())
                .callbackData(menuVariables.getAdminBtnCallbackCancel())
                .build();
        adminChoiceButtons.add(List.of(buttonCancel));


        /*InlineKeyboardButton buttonRooms = InlineKeyboardButton.builder()
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
        InlineKeyboardButton buttonMap = InlineKeyboardButton.builder()
                .text(texts.get(11))
                .callbackData(menuVariables.getAdminBtnCallbackMap())
                .build();
        InlineKeyboardButton buttonContact = InlineKeyboardButton.builder()
                .text(texts.get(12))
                .callbackData(menuVariables.getAdminBtnCallbackContact())
                .build();
        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(texts.get(13))
                .callbackData(menuVariables.getAdminBtnCallbackCancel())
                .build();*/

        /*List<InlineKeyboardButton> row1 = List.of(buttonRooms, buttonSquare);
        List<InlineKeyboardButton> row2 = List.of(buttonFloor, buttonAllFloor);
        List<InlineKeyboardButton> row3 = List.of(buttonMetro, buttonAddress);
        List<InlineKeyboardButton> row4 = List.of(buttonDistrict, buttonMoney);
        List<InlineKeyboardButton> row5 = List.of(buttonMoneyRange, buttonTelegraph);
        List<InlineKeyboardButton> row6 = List.of(buttonInfo, buttonMap);
        List<InlineKeyboardButton> row7 = List.of(buttonContact);
        List<InlineKeyboardButton> row8 = List.of(buttonCancel);
        List<List<InlineKeyboardButton>> rowList = List.of(row1, row2, row3, row4, row5, row6, row7, row8);*/

        inlineKeyboardMarkup.setKeyboard(adminChoiceButtons);

        return inlineKeyboardMarkup;
    }


    /*private void initChoice() {
        this.choice = new HashMap<>();
        this.choice.put(AdminChoiceParameter.ADDRESS, this.getAdminChoice().getAddress() != null);
        this.choice.put(AdminChoiceParameter.CONTACT, this.getAdminChoice().getContact() != null);
        this.choice.put(AdminChoiceParameter.ALL_FLOORS, this.getAdminChoice().getAllFloors() != null);
        this.choice.put(AdminChoiceParameter.INFO, this.getAdminChoice().getInfo() != null);
        this.choice.put(AdminChoiceParameter.DISTRICT, this.getAdminChoice().getDistrict() != null);
        this.choice.put(AdminChoiceParameter.FLOOR, this.getAdminChoice().getFloor() != null);
        this.choice.put(AdminChoiceParameter.MAP_LINK, this.getAdminChoice().getMapLink() != null);
        this.choice.put(AdminChoiceParameter.METRO, this.getAdminChoice().getMetro() != null);
        this.choice.put(AdminChoiceParameter.MONEY, this.getAdminChoice().getMoney() != null);
        this.choice.put(AdminChoiceParameter.MONEY_RANGE, this.getAdminChoice().getMoneyRange() != null);
        this.choice.put(AdminChoiceParameter.ROOMS, this.getAdminChoice().getRooms() != null);
        this.choice.put(AdminChoiceParameter.SQUARE, this.getAdminChoice().getSquare() != null);
        this.choice.put(AdminChoiceParameter.TELEGRAPH, this.getAdminChoice().getTelegraph() != null);
    }*/

    // В зависимости от параметров объекта AdminChoice пишу текста на кнопках
    private void setTextsAndCallbacks(List<String> texts, List<String> callbacks) {
        String selected = Emoji.SELECTED.toString();

        for(AdminChoiceParameter temp: AdminChoiceParameter.values()) {
            // Определяю текст кнопки. Если выбор уже сделан - показываю его значение
            texts.add((!this.adminChoiceMap.get(temp).equals(this.no) ? selected + " " : "") +
                    temp.getBtnText() + " " + adminChoiceMap.get(temp));
            callbacks.add(temp.getBtnCallback());
        }
    }

    private HashMap<AdminChoiceParameter, String> getHashMapFromAdminChoice() {
        HashMap<AdminChoiceParameter, String> adminChoiceHashMap = new HashMap<>();
        adminChoiceHashMap.put(AdminChoiceParameter.DISTRICT, this.adminChoice.getDistrict() != null ?
                this.adminChoice.getDistrict().toString() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.ADDRESS, this.adminChoice.getAddress() != null ?
                this.adminChoice.getAddress() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.TELEGRAPH, this.adminChoice.getTelegraph() != null ?
                this.adminChoice.getTelegraph() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.MAP_LINK, this.adminChoice.getMapLink() != null ?
                this.adminChoice.getMapLink() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.SQUARE, this.adminChoice.getSquare() != null ?
                this.adminChoice.getSquare().toString() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.ROOMS, this.adminChoice.getRooms() != null ?
                this.adminChoice.getRooms().toString() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.CONTACT, this.adminChoice.getContact() != null ?
                this.adminChoice.getContact() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.MONEY, this.adminChoice.getMoney() != null ?
                this.adminChoice.getMoney() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.MONEY_RANGE, this.adminChoice.getMoneyRange() != null ?
                this.adminChoice.getMoneyRange() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.METRO, this.adminChoice.getMetro() != null ?
                this.adminChoice.getMetro() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.FLOOR, this.adminChoice.getFloor() != null ?
                this.adminChoice.getFloor().toString() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.ALL_FLOORS, this.adminChoice.getAllFloors() != null ?
                this.adminChoice.getAllFloors().toString() : this.no);
        adminChoiceHashMap.put(AdminChoiceParameter.INFO, this.adminChoice.getInfo() != null ?
                this.adminChoice.getInfo() : this.no);
        return adminChoiceHashMap;
    }

    private boolean checkForPublish() {
        // Прохожусь по всем обязательным полям, если какой-то из них не заполнен - публикация невозможна
        for(AdminChoiceParameter temp: AdminChoiceParameter.getRequiredForPublish()) {
            if (this.adminChoiceMap.get(temp).equals(this.no))
                return false;
        }
        return true;
    }


    public InlineKeyboardMarkup getKeyboard() {
        return newFlatMenu;
    }
}
