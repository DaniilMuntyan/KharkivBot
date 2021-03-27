package com.example.demo.admin_bot.service.handler.delete;

import com.example.demo.admin_bot.cache.AdminCache;
import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.admin_bot.service.AdminService;
import com.example.demo.admin_bot.utils.AdminState;
import com.example.demo.common_part.constants.AdminMenuVariables;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.BuyFlatService;
import com.example.demo.user_bot.service.entities.RentalFlatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Service
public final class DeleteCallbackHandler {
    private static final Logger LOGGER = Logger.getLogger(DeleteCallbackHandler.class);

    private final AdminMenuVariables adminMenuVariables;
    private final MessagesVariables messagesVariables;
    private final AdminService adminService;
    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;

    private final AdminCache adminCache;
    private final DataCache dataCache;

    @Autowired
    public DeleteCallbackHandler(AdminMenuVariables adminMenuVariables, MessagesVariables messagesVariables, AdminService adminService, RentalFlatService rentalFlatService, BuyFlatService buyFlatService, AdminCache adminCache, DataCache dataCache) {
        this.adminMenuVariables = adminMenuVariables;
        this.messagesVariables = messagesVariables;
        this.adminService = adminService;
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.adminCache = adminCache;
        this.dataCache = dataCache;
    }

    public List<BotApiMethod<?>> handleDeleteCallback(CallbackQuery callbackQuery, UserCache admin) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        List<BotApiMethod<?>> response = new ArrayList<>();

        if (data.equals(adminMenuVariables.getDeleteBtnCallbackConfirm())) { // Подтвердили удаление
            Long flatId = this.adminCache.getFlatIdToDelete(chatId);
            if (admin.getAdminChoice().getIsRentFlat()) { // Если удаляем квартиру под аренду
                this.dataCache.removeRentFlat(flatId); // Удаляю с хэша все связи с текущей квартирой
                this.rentalFlatService.deleteRentFlat(flatId); // Удаляю квартиру в базе
            } else { // Удаляем квартиру на продажу
                this.dataCache.removeBuyFlat(flatId); // Удаляю с хэша все связи с текущей квартирой
                this.buyFlatService.deleteBuyFlat(flatId); // Удаляю квартиру в базе
            }

            this.adminCache.removeFlatToDelete(chatId); // Убираю из кэша админа эту квартиру (уже удаленную)
            admin.getAdminChoice().clear(); // Почистил выбор админа

            admin.setBotAdminState(AdminState.ADMIN_INIT); // В изначальное состояние
            admin.getAdminChoice().clear(); // Почистил выбор админа

            response.addAll(getSuccess(chatId, messageId, flatId));
        }

        if (data.equals(adminMenuVariables.getDeleteBtnCallbackCancel())) { // Нажали "Отмена"
            admin.setBotAdminState(AdminState.ADMIN_INIT); // В изначальное состояние
            admin.getAdminChoice().clear(); // Почистил выбор админа
            response.add(this.getEditCancel(chatId, messageId)); // Меняю на сообщение об отмене удаления
        }

        this.dataCache.saveUserCache(admin); // Сохраняю измененное состояние админа

        return response;
    }

    private List<BotApiMethod<?>> getSuccess(Long chatId, Integer messageId, Long flatId) {
        List<BotApiMethod<?>> response = new ArrayList<>();
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(); // Убираю инлайн клаву
        editMessageReplyMarkup.setChatId(chatId.toString());
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(null);
        response.add(editMessageReplyMarkup);

        SendMessage successMessage = new SendMessage();
        successMessage.setChatId(chatId.toString());
        successMessage.setText(messagesVariables.getAdminDeleteSuccess(flatId.toString()));
        response.add(successMessage);

        return response;
    }

    private EditMessageText getEditCancel(Long chatId, Integer messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setReplyMarkup(null);
        editMessageText.setText(messagesVariables.getAdminDeleteCanceled());
        return editMessageText;
    }
}
