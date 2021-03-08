package com.example.demo.admin_bot.constants;

import com.example.demo.admin_bot.utils.Emoji;
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
        return MessageFormat.format(adminConfirmPublishSuccess, Emoji.SUCCESS, flatNumber);
    }
}
