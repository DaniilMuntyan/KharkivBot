package com.example.demo.user_bot.keyboards;

import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.common_part.constants.UserMenuVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.admin_bot.model.AdminChoice;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class PublishedFlatKeyboard {
    @Getter
    private final InlineKeyboardMarkup keyboard;
    private final AdminMenuVariables adminMenuVariables;
    private final UserMenuVariables userMenuVariables;
    private final AdminChoice adminChoice;

    public PublishedFlatKeyboard(AdminChoice adminChoice, Object flat) {
        this.adminMenuVariables = BeanUtil.getBean(AdminMenuVariables.class);
        this.userMenuVariables = BeanUtil.getBean(UserMenuVariables.class);
        this.adminChoice = adminChoice;
        this.keyboard = getNewFlatMenu(flat);
    }

    private InlineKeyboardMarkup getNewFlatMenu(Object flat) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (this.adminChoice.getMapLink() != null) {
            InlineKeyboardButton buttonMap = InlineKeyboardButton.builder()
                    .text(adminMenuVariables.getNewFlatBtnMap())
                    .url(this.adminChoice.getHtmlMapLink())
                    .build();
            rows.add(List.of(buttonMap));
        }

        if (this.adminChoice.getContact() != null) {
            InlineKeyboardButton buttonContact = InlineKeyboardButton.builder()
                    .text(adminMenuVariables.getNewFlatBtnContact())
                    .url(this.adminChoice.getContact())
                    .build();
            rows.add(List.of(buttonContact));
        }

        // В зависимости от того, для какой квартиры callback (аренда/продажа)
        String callbackData = flat instanceof RentFlat ?
                userMenuVariables.getUserBotFlatMsgSeeRentCallbackPrefix() + ((RentFlat) flat).getId() :
                userMenuVariables.getUserBotFlatMsgSeeBuyCallbackPrefix() + ((BuyFlat) flat).getId() ;
        InlineKeyboardButton buttonSee = InlineKeyboardButton.builder() // Кнопка "хочу посмотреть"
                .text(userMenuVariables.getUserBotFlatMsgSeeText())
                .callbackData(callbackData)
                .build();
        rows.add(List.of(buttonSee));

        if (rows.size() == 0) {
            return null;
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
