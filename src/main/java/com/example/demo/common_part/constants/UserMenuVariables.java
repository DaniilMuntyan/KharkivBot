package com.example.demo.common_part.constants;

import com.example.demo.common_part.utils.Emoji;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@PropertySource(value= "classpath:user-menu.properties", encoding = "UTF-8")
public class UserMenuVariables {

    @Value("${user.bot.menuInit.category.button.categoryPrefix.callback}")
    @Getter
    private String menuInitBtnCategoryCallbackPrefix;

    @Value("${user.bot.menuInit.category.button.rental.text}")
    @Getter
    private String menuInitBtnCategoryRentalText;
    @Value("${user.bot.menuInit.category.button.rental.callback}")
    @Getter
    private String menuInitCategoryBtnRentalCallback;

    @Value("${user.bot.menuInit.category.button.buy.text}")
    @Getter
    private String menuInitBtnCategoryBuyText;
    @Value("${user.bot.menuInit.category.button.buy.callback}")
    @Getter
    private String menuInitCategoryBtnBuyCallback;

    @Value("${user.bot.menuInit.button.categoryNext.callback}")
    @Getter
    private String menuInitBtnCategoryNextCallback;

    @Value("${user.bot.menuInit.rooms.button.callbackPrefix}")
    @Getter
    private String menuInitRoomsBtnCallbackPrefix;

    @Value("${user.bot.menuInit.rooms.button.room1.text}")
    private String menuInitRoomsBtnRoom1Text;
    public String getMenuInitRoomsBtnRoom1Text() {
        return MessageFormat.format(menuInitRoomsBtnRoom1Text, Emoji.ONE);
    }
    @Value("${user.bot.menuInit.rooms.button.room1.callback}")
    @Getter
    private String menuInitRoomsBtnRoom1Callback;

    @Value("${user.bot.menuInit.rooms.button.room2.text}")
    private String menuInitRoomsBtnRoom2Text;
    public String getMenuInitRoomsBtnRoom2Text() {
        return MessageFormat.format(menuInitRoomsBtnRoom1Text, Emoji.TWO);
    }
    @Value("${user.bot.menuInit.rooms.button.room2.callback}")
    @Getter
    private String menuInitRoomsBtnRoom2Callback;

    @Value("${user.bot.menuInit.rooms.button.room3.text}")
    private String menuInitRoomsBtnRoom3Text;
    public String getMenuInitRoomsBtnRoom3Text() {
        return MessageFormat.format(menuInitRoomsBtnRoom3Text, Emoji.THREE);
    }
    @Value("${user.bot.menuInit.rooms.button.room3.callback}")
    @Getter
    private String menuInitRoomsBtnRoom3Callback;

    @Value("${user.bot.menuInit.rooms.button.room4.text}")
    private String menuInitRoomsBtnRoom4Text;
    public String getMenuInitRoomsBtnRoom4Text() {
        return MessageFormat.format(menuInitRoomsBtnRoom4Text, Emoji.FOUR);
    }
    @Value("${user.bot.menuInit.rooms.button.room4.callback}")
    @Getter
    private String menuInitRoomsBtnRoom4Callback;

    @Value("${user.bot.menuInit.rooms.button.room0.text}")
    @Getter
    private String menuInitRoomsBtnRoom0Text;
    @Value("${user.bot.menuInit.rooms.button.room0.callback}")
    @Getter
    private String menuInitRoomsBtnRoom0Callback;

    @Value("${user.bot.menuInit.button.roomNext.callback}")
    @Getter
    private String menuInitBtnRoomNextCallback;

    @Value("${user.bot.menuInit.button.next.text}")
    private String menuInitBtnNextText;
    public String getMenuInitBtnNextText() {
        return MessageFormat.format(menuInitBtnNextText, Emoji.NEXT);
    }

    @Value("${user.bot.menuInit.button.callbackPrefix}")
    @Getter
    private String menuInitBtnCallbackPrefix;

    @Value("${user.bot.menuInit.districts.button.districtPrefix.callback}")
    @Getter
    private String menuInitDistrictsBtnPrefixCallback;

    @Value("${user.bot.menuInit.districts.button.selectAll.text}")
    @Getter
    private String menuInitDistrictBtnSelectAllText;
    @Value("${user.bot.menuInit.districts.button.selectAll.callback}")
    @Getter
    private String menuInitDistrictBtnSelectAllCallback;

    @Value("${user.bot.menuInit.button.districtNext.callback}")
    @Getter
    private String menuInitDistrictNextCallback;

    @Value("${user.bot.menuInit.budget.button.rangePrefix.callback}")
    @Getter
    private String menuInitBudgetBtnRangePrefixCallback;

    @Value("${user.bot.menuInit.button.budgetNext.callback}")
    @Getter
    private String menuInitBudgetNextCallback;

    @Value("${user.bot.menu1.button.choice.text}")
    @Getter
    private String menu1BtnChoiceText;

    @Value("${user.bot.menu1.button.settings.text}")
    @Getter
    private String menu1BtnSettingsText;

    @Value("${user.bot.menu2.button.category.text}")
    @Getter
    private String menu2BtnCategoryText;
    @Value("${user.bot.menu2.button.category.callback}")
    @Getter
    private String menu2BtnCategoryCallback;

    @Value("${user.bot.menu2.button.rooms.text}")
    @Getter
    private String menu2BtnRoomsText;
    @Value("${user.bot.menu2.button.rooms.callback}")
    @Getter
    private String menu2BtnRoomsCallback;

    @Value("${user.bot.menu2.button.districts.text}")
    @Getter
    private String menu2BtnDistrictsText;
    @Value("${user.bot.menu2.button.districts.callback}")
    @Getter
    private String menu2BtnDistrictsCallback;

    @Value("${user.bot.menu2.button.budget.text}")
    @Getter
    private String menu2BtnBudgetText;
    @Value("${user.bot.menu2.button.budget.callback}")
    @Getter
    private String menu2BtnBudgetCallback;

    @Value("${user.bot.menu2.button.back.text}")
    private String menu2BtnBackText;
    public String getMenu2BtnBackText() {
        return MessageFormat.format(menu2BtnBackText, Emoji.BACK);
    }
    @Value("${user.bot.menu2.button.back.callback}")
    @Getter
    private String menu2BtnBackCallback;

    @Value("${user.bot.menu2.button.save.text}")
    private String menu2BtnSaveText;
    public String getMenu2BtnSaveText() {
        return MessageFormat.format(menu2BtnSaveText, Emoji.SAVE);
    }
    @Value("${user.bot.menu2.button.save.callback}")
    @Getter
    private String menu2BtnSaveCallback;

    @Value("${user.bot.menu3.button.notMailing.text}")
    @Getter
    private String menu3BtnNotMailingText;

    @Value("${user.bot.menu3.button.enterPhone.text}")
    @Getter
    private String menu3BtnEnterPhoneText;

    @Value("${user.bot.menu3.button.back.text}")
    private String menu3BtnBackText;
    public String getMenu3BtnBackText() {
        return MessageFormat.format(menu3BtnBackText, Emoji.BACK);
    }

    @Value("${user.bot.menu2.1.button.rental.text}")
    @Getter
    private String menu21BtnRentalText;
    @Value("${user.bot.menu2.1.button.rental.callback}")
    @Getter
    private String menu21BtnRentalCallback;

    @Value("${user.bot.menu2.1.button.buy.text}")
    @Getter
    private String menu21BtnBuyText;
    @Value("${user.bot.menu2.1.button.buy.callback}")
    @Getter
    private String menu21BtnBuyCallback;

    @Value("${user.bot.menu2.1.button.back.text}")
    private String menu21BtnBackText;
    public String getMenu21BtnBackText() {
        return MessageFormat.format(menu21BtnBackText, Emoji.BACK);
    }
    @Value("${user.bot.menu2.1.button.back.callback}")
    @Getter
    private String menu21BtnBackCallback;

    @Value("${user.bot.menu2.2.button.room1.text}")
    private String menu22BtnRoom1Text;
    public String getMenu22BtnRoom1Text() {
        return MessageFormat.format(menu22BtnRoom1Text, Emoji.ONE);
    }
    @Value("${user.bot.menu2.2.button.room1.callback}")
    @Getter
    private String menu22BtnRoom1Callback;

    @Value("${user.bot.menu2.2.button.room2.text}")
    private String menu22BtnRoom2Text;
    public String getMenu22BtnRoom2Text() {
        return MessageFormat.format(menu22BtnRoom2Text, Emoji.TWO);
    }
    @Value("${user.bot.menu2.2.button.room2.callback}")
    @Getter
    private String menu22BtnRoom2Callback;

    @Value("${user.bot.menu2.2.button.room3.text}")
    private String menu22BtnRoom3Text;
    public String getMenu22BtnRoom3Text() {
        return MessageFormat.format(menu22BtnRoom3Text, Emoji.THREE);
    }
    @Value("${user.bot.menu2.2.button.room3.callback}")
    @Getter
    private String menu22BtnRoom3Callback;

    @Value("${user.bot.menu2.2.button.room4.text}")
    private String menu22BtnRoom4Text;
    public String getMenu22BtnRoom4Text() {
        return MessageFormat.format(menu22BtnRoom4Text, Emoji.FOUR);
    }
    @Value("${user.bot.menu2.2.button.room4.callback}")
    @Getter
    private String menu22BtnRoom4Callback;

    @Value("${user.bot.menu2.2.button.room0.text}")
    @Getter
    private String menu22BtnRoom0Text;
    @Value("${user.bot.menu2.2.button.room0.callback}")
    @Getter
    private String menu22BtnRoom0Callback;

    @Value("${user.bot.menu2.3.button.back.text}")
    private String menu23BtnBackText;
    public String getMenu23BtnBackText() {
        return MessageFormat.format(menu23BtnBackText, Emoji.BACK);
    }
    @Value("${user.bot.menu2.3.button.back.callback}")
    @Getter
    private String menu23BtnBackCallback;

    @Value("${user.bot.menu2.3.button.districtPrefix.callback}")
    @Getter
    private String menu23BtnDistrictPrefixCallback;

    @Value("${user.bot.menu2.4.button.back.text}")
    private String menu24BtnBackText;
    public String getMenu24BtnBackText() {
        return MessageFormat.format(menu24BtnBackText, Emoji.BACK);
    }
    @Value("${user.bot.menu2.4.button.back.callback}")
    @Getter
    private String menu24BtnBackCallback;

    @Value("${user.bot.menu2.4.button.rangePrefix.callback}")
    @Getter
    private String menu24BtnRangePrefixCallback;

    @Value("${user.bot.menu3.2.button.sendMyPhone.text}")
    private String menu32BtnSendMyPhoneText;
    public String getMenu32BtnSendMyPhoneText() {
        return MessageFormat.format(menu32BtnSendMyPhoneText, Emoji.PHONE);
    }

    @Value("${user.bot.menu3.2.button.back.text}")
    private String menu32BtnBackText;
    public String getMenu32BtnBackText() {
        return MessageFormat.format(menu32BtnBackText, Emoji.BACK);
    }

}
