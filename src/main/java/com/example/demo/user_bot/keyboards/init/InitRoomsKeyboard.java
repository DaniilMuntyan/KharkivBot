package com.example.demo.user_bot.keyboards.init;

import com.example.demo.admin_bot.utils.AdminChoiceParameter;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Emoji;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.model.UserChoice;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public final class InitRoomsKeyboard {

    private final UserMenuVariables userMenuVariables;

    @Autowired
    public InitRoomsKeyboard(UserMenuVariables userMenuVariables) {
        this.userMenuVariables = userMenuVariables;
    }

    // Клавиатура для меню инициализации №2 (выбор кол-ва комнат)
    public InlineKeyboardMarkup getKeyboard(UserChoice userChoice) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        String roomChoice = userChoice.getRooms();

        InlineKeyboardButton buttonZero = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.GOSTINKA.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenuInitRoomsBtnRoom0Text())
                .callbackData(userMenuVariables.getMenuInitRoomsBtnRoom0Callback())
                .build();
        InlineKeyboardButton buttonOne = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.ONE.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenuInitRoomsBtnRoom1Text())
                .callbackData(userMenuVariables.getMenuInitRoomsBtnRoom1Callback())
                .build();
        InlineKeyboardButton buttonTwo = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.TWO.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenuInitRoomsBtnRoom2Text())
                .callbackData(userMenuVariables.getMenuInitRoomsBtnRoom2Callback())
                .build();
        InlineKeyboardButton buttonThree = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.THREE.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenuInitRoomsBtnRoom3Text())
                .callbackData(userMenuVariables.getMenuInitRoomsBtnRoom3Callback())
                .build();
        InlineKeyboardButton buttonFour = InlineKeyboardButton.builder()
                .text((roomChoice.contains(Rooms.FOUR.getIdentifier()) ? Emoji.SELECTED + " ": "") +
                        userMenuVariables.getMenuInitRoomsBtnRoom4Text())
                .callbackData(userMenuVariables.getMenuInitRoomsBtnRoom4Callback())
                .build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(buttonOne, buttonTwo));
        rows.add(List.of(buttonThree, buttonFour));
        rows.add(List.of(buttonZero));

        if (!roomChoice.isEmpty()) {
            InlineKeyboardButton buttonNext = InlineKeyboardButton.builder()
                    .text(userMenuVariables.getMenuInitBtnNextText())
                    .callbackData(userMenuVariables.getMenuInitBtnRoomNextCallback())
                    .build();
            rows.add(List.of(buttonNext));
        }

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
