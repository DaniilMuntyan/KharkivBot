package com.example.demo.admin_bot.constants;

import com.example.demo.common_part.utils.Emoji;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@PropertySource(value="classpath:messages.properties", encoding = "UTF-8")
public class MessagesVariables {
    @Value("${admin.hello}")
    @Getter
    private String helloAdmin;

    @Value("${admin.hi}")
    @Getter
    private String adminHi;

    @Value("${admin.bye}")
    @Getter
    private String adminBye;

    @Value("${admin.error.sendMessage}")
    private String adminErrorSendMessage;
    public String getAdminErrorSendMessage() {
        return MessageFormat.format(adminErrorSendMessage, Emoji.ERROR);
    }

    @Value("${admin.menu.forbidden}")
    @Getter
    private String adminMenuForbidden;

    @Value("${admin.bot.menu.rooms.messageText}")
    private String roomsMessageText;
    public String getRoomsMessageText() {
        return MessageFormat.format(roomsMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.square.messageText}")
    private String squareMessageText;
    public String getSquareMessageText() {
        return MessageFormat.format(squareMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.floor.messageText}")
    private String floorMessageText;
    public String getFloorMessageText() {
        return MessageFormat.format(floorMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.allFloors.messageText}")
    private String allFloorsMessageText;
    public String getAllFloorsMessageText() {
        return MessageFormat.format(allFloorsMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.metro.messageText}")
    private String metroMessageText;
    public String getMetroMessageText() {
        return MessageFormat.format(metroMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.address.messageText}")
    private String addressMessageText;
    public String getAddressMessageText() {
        return MessageFormat.format(addressMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.district.messageText}")
    private String districtMessageText;
    public String getDistrictMessageText() {
        return MessageFormat.format(districtMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.price.messageText}")
    private String priceMessageText;
    public String getPriceMessageText() {
        return MessageFormat.format(priceMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.budget.messageText}")
    private String budgetMessageText;
    public String getBudgetMessageText() {
        return MessageFormat.format(budgetMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.photo.messageText}")
    private String telegraphMessageText;
    public String getTelegraphMessageText() {
        return MessageFormat.format(telegraphMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.info.messageText}")
    private String infoMessageText;
    public String getInfoMessageText() {
        return MessageFormat.format(infoMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.map.messageText}")
    private String mapMessageText;
    public String getMapMessageText() {
        return MessageFormat.format(mapMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.contact.messageText}")
    private String contactMessageText;
    public String getContactMessageText() {
        return MessageFormat.format(contactMessageText, Emoji.SUBMENU);
    }

    @Value("${admin.menu.confirmMessage.messageText}")
    private String confirmMessageMessageText;
    public String getConfirmMessageMessageText() {
        return MessageFormat.format(confirmMessageMessageText, Emoji.WARNING);
    }

    @Value("${admin.menu.confirmMessage.cancel}")
    private String adminConfirmMessageCancel;
    public String getAdminConfirmMessageCancel() {
        return MessageFormat.format(adminConfirmMessageCancel, Emoji.FAIL);
    }

    @Value("${admin.menu.confirmMessage.success}")
    private String adminConfirmMessageSuccess;
    public String getAdminConfirmMessageSuccess() {
        return MessageFormat.format(adminConfirmMessageSuccess, Emoji.SUCCESS);
    }

    @Value("${admin.menu.confirmPublish.cancel}")
    private String adminConfirmPublishCancel;
    public String getAdminConfirmPublishCancel() {
        return MessageFormat.format(adminConfirmPublishCancel, Emoji.FAIL);
    }

    @Value("${admin.menu.confirmPublish.success}")
    private String adminConfirmPublishSuccess;
    public String getAdminConfirmPublishSuccess(String flatNumber) {
        return MessageFormat.format(adminConfirmPublishSuccess, Emoji.SUCCESS, flatNumber, Emoji.UP);
    }

    @Value("${admin.menu.delete.category}")
    private String adminDeleteCategory;
    public String getAdminDeleteCategory() {
        return MessageFormat.format(adminDeleteCategory, Emoji.ARROW_DOWN);
    }

    @Value("${admin.menu.delete.enterRentId}")
    private String adminDeleteEnterRentId;
    public String getAdminDeleteEnterRentId() {
        return MessageFormat.format(adminDeleteEnterRentId, Emoji.ARROW_DOWN);
    }
    @Value("${admin.menu.delete.enterBuyId}")
    private String adminDeleteEnterBuyId;
    public String getAdminDeleteEnterBuyId() {
        return MessageFormat.format(adminDeleteEnterBuyId, Emoji.ARROW_DOWN);
    }

    @Value("${admin.menu.delete.success}")
    private String adminDeleteSuccess;
    public String getAdminDeleteSuccess(String flatId) {
        return MessageFormat.format(adminDeleteSuccess, Emoji.BOX_WITH_CHECK, flatId);
    }

    @Value("${admin.menu.delete.canceled}")
    private String adminDeleteCanceled;
    public String getAdminDeleteCanceled() {
        return MessageFormat.format(adminDeleteCanceled, Emoji.NO);
    }

    @Value("${admin.menu.delete.wrongId}")
    private String adminDeleteWrongId;
    public String getAdminDeleteWrongId() {
        return MessageFormat.format(adminDeleteWrongId, Emoji.SAD);
    }

    @Value("${admin.foundNewFlat.text}")
    private String adminFoundNewFlat;
    public String getAdminFoundNewFlat() {
        return MessageFormat.format(adminFoundNewFlat, Emoji.WAVE, Emoji.SMILE);
    }

    @Value("${user.hi}")
    private String userHi;
    public String getUserHi(String name) {
        return MessageFormat.format(userHi, Emoji.WAVE, name);
    }

    @Value("${user.firstHi}")
    private String userFirstHi;
    public String getUserFirstHi(String name) {
        return MessageFormat.format(userFirstHi, Emoji.WAVE, name, Emoji.SPEED, Emoji.ARROW_DOWN);
    }

    @Value("${user.init.category.text}")
    private String userInitCategoryText;
    public String getUserInitCategoryText() {
        return MessageFormat.format(userInitCategoryText, Emoji.DOWN);
    }

    @Value("${user.init.rooms.text}")
    private String userInitRoomsText;
    public String getUserInitRoomsText() {
        return MessageFormat.format(userInitRoomsText, Emoji.DOWN);
    }

    @Value("${user.init.districts.text}")
    private String userInitDistrictsText;
    public String getUserInitDistrictsText() {
        return MessageFormat.format(userInitDistrictsText, Emoji.DOWN);
    }

    @Value("${user.init.budget.text}")
    private String userInitBudgetText;
    public String getUserInitBudgetText() {
        return MessageFormat.format(userInitBudgetText, Emoji.DOWN);
    }

    @Value("${user.init.end.text}")
    private String userInitEndText;
    public String getUserInitEndText() {
        return MessageFormat.format(userInitEndText, Emoji.OK, Emoji.WINK);
    }

    @Value("${user.dontUnderstand.text}")
    private String userDontUnderstandText;
    public String getUserDontUnderstandText() {
        return MessageFormat.format(userDontUnderstandText, Emoji.SAD);
    }

    @Value("${user.antiSpam.text}")
    private String userAntiSpamText;
    public String getUserAntiSpamText() {
        return MessageFormat.format(userAntiSpamText, Emoji.WARNING, Emoji.SAD);
    }

    @Value("${user.sentAllFlats.text}")
    private String userSentAllFlats;
    public String getUserSentAllFlats() {
        return MessageFormat.format(userSentAllFlats, Emoji.UP);
    }

    @Value("${user.sentNoFlats.text}")
    private String userSentNoFlatsText;
    public String getUserSentNoFlatsText() {
        return MessageFormat.format(userSentNoFlatsText, Emoji.EYES);
    }

    @Value("${user.sentNotAllFlats.text}")
    private String userSentNotAllFLatsText;
    public String getUserSentNotAllFLatsText(String size) {
        return MessageFormat.format(userSentNotAllFLatsText, Emoji.POINT_UP, size, Emoji.ARROW_DOWN);
    }

    @Value("${user.enough.text}")
    private String userEnoughText;
    public String getUserEnoughText() {
        return MessageFormat.format(userEnoughText, Emoji.OK, Emoji.SMILE);
    }

    @Value("${user.menu1.text}")
    private String userMenu1Text;
    public String getUserMenu1Text() {
        return MessageFormat.format(userMenu1Text, Emoji.CLIPBOARD, Emoji.RAISED_HANDS, Emoji.ORANGE_DIAMOND);
    }

    @Value("${user.menu2.text}")
    private String userMenu2Text;
    public String getUserMenu2Text() {
        return MessageFormat.format(userMenu2Text, Emoji.CLIPBOARD, Emoji.RAISED_HANDS,
                Emoji.POINT_RIGHT, Emoji.SMILE);
    }

    @Value("${user.menu2.1.text}")
    private String userMenu21Text;
    public String getUserMenu21Text() {
        return MessageFormat.format(userMenu21Text, Emoji.POINT_RIGHT);
    }

    @Value("${user.menu2.2.text}")
    private String userMenu22Text;
    public String getUserMenu22Text() {
        return MessageFormat.format(userMenu22Text, Emoji.POINT_RIGHT);
    }

    @Value("${user.menu2.3.text}")
    private String userMenu23Text;
    public String getUserMenu23Text() {
        return MessageFormat.format(userMenu23Text, Emoji.POINT_RIGHT);
    }

    @Value("${user.menu2.4.text}")
    private String userMenu24Text;
    public String getUserMenu24Text() {
        return MessageFormat.format(userMenu24Text, Emoji.POINT_RIGHT);
    }

    @Value("${user.menu3.text}")
    private String userMenu3Text;
    public String getUserMenu3Text() {
        return MessageFormat.format(userMenu3Text, Emoji.CLIPBOARD, Emoji.RAISED_HANDS, Emoji.ORANGE_DIAMOND);
    }

    @Value("${user.menu3.1.stopMailing.text}")
    private String userMenu31StopMailingText;
    public String getUserMenu31StopMailingText() {
        return MessageFormat.format(userMenu31StopMailingText, Emoji.OK, Emoji.SMILE);
    }

    @Value("${user.menu3.1.startMailing.text}")
    private String userMenu31StartMailingText;
    public String getUserMenu31StartMailingText() {
        return MessageFormat.format(userMenu31StartMailingText, Emoji.STARS_EYES);
    }

    @Value("${user.menu3.2.text}")
    private String userMenu32Text;
    public String getUserMenu32Text() {
        return MessageFormat.format(userMenu32Text, Emoji.TELEPHONE);
    }

    @Value("${user.menu3.2.acceptPhone.text}")
    private String userMenu32AcceptPhoneText;
    public String getUserMenu32AcceptPhoneText() {
        return MessageFormat.format(userMenu32AcceptPhoneText, Emoji.LIKE, Emoji.RAISED_HANDS, Emoji.ORANGE_DIAMOND,
                Emoji.ANGEL, Emoji.WINK);
    }

    @Value("${user.menu3.2.wrongPhone.text}")
    private String userMenu32WrongPhoneText;
    public String getUserMenu32WrongPhoneText() {
        return MessageFormat.format(userMenu32WrongPhoneText, Emoji.GREY_EXCLAMATION);
    }

}
