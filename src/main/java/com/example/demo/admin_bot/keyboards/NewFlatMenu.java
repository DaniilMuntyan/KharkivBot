package com.example.demo.admin_bot.keyboards;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.admin_bot.utils.AdminChoiceParameter;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.admin_bot.model.AdminChoice;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NewFlatMenu {
    private static final Logger LOGGER = Logger.getLogger(NewFlatMenu.class);

    private final AdminMenuVariables adminMenuVariables;
    @Getter
    private final AdminChoice adminChoice;
    private final InlineKeyboardMarkup newFlatMenu;
    private final HashMap<AdminChoiceParameter, String> adminChoiceMap;
    private final String no = "нет";

    public NewFlatMenu(boolean isRentFlat) {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.adminChoice = new AdminChoice(isRentFlat);
        // Объект AdminChoice в виде словаря, для доступа к значениям выбора
        this.adminChoiceMap = getHashMapFromAdminChoice();
        this.newFlatMenu = getNewFlatKeyboardMenu();
    }

    public NewFlatMenu(AdminChoice adminChoice) {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
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
                    .text(adminMenuVariables.getAdminBtnPublish())
                    .callbackData(adminMenuVariables.getAdminBtnCallbackPublish())
                    .build();
            adminChoiceButtons.add(List.of(buttonPublish));
        }

        InlineKeyboardButton buttonCancel = InlineKeyboardButton.builder()
                .text(adminMenuVariables.getAdminBtnCancel())
                .callbackData(adminMenuVariables.getAdminBtnCallbackCancel())
                .build();
        adminChoiceButtons.add(List.of(buttonCancel));

        inlineKeyboardMarkup.setKeyboard(adminChoiceButtons);

        return inlineKeyboardMarkup;
    }

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
