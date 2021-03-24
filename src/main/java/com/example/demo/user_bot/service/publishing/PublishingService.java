package com.example.demo.user_bot.service.publishing;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.PublishedFlatKeyboard;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.service.entities.BuyFlatService;
import com.example.demo.user_bot.service.entities.RentalFlatService;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
public final class PublishingService {
    private static final Logger LOGGER = Logger.getLogger(PublishingService.class);
    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;
    private final ProgramVariables programVariables;
    private final UserService userService;
    private final UserBotSendingQueue userBotSendingQueue;
    private final MessagesVariables messagesVariables;
    private final FlatMessageService flatMessageService;

    private final DataCache dataCache;

    @Autowired
    public PublishingService(RentalFlatService rentalFlatService, BuyFlatService buyFlatService, ProgramVariables programVariables, UserService userService, UserBotSendingQueue userBotSendingQueue, MessagesVariables messagesVariables, FlatMessageService flatMessageService, DataCache dataCache) {
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.programVariables = programVariables;
        this.userService = userService;
        this.userBotSendingQueue = userBotSendingQueue;
        this.messagesVariables = messagesVariables;
        this.flatMessageService = flatMessageService;
        this.dataCache = dataCache;
    }

    // Публикую только что добавленную админом квартиру соответствующим пользователям
    public String publish(UserCache admin, List<BotApiMethod<?>> response) {
        List<User> allUsers = userService.findAllUsers();
        String result;

        SendMessage msgToChannel;
        String foundNewFlatForYou = messagesVariables.getAdminFoundNewFlat() + "\n";
        if (admin.getAdminChoice().getIsRentFlat()) {
            RentFlat rentFlat = rentalFlatService.save(new RentFlat(admin.getAdminChoice())); // Сохраняю квартиру в БД
            admin.getAdminChoice().setFlatId(rentFlat.getId()); // Когда уже есть айди добавленной квартиры
            result = messagesVariables.getAdminBotHashTag(rentFlat.getId().toString(), true); // Хэштег 12345_аренда
            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (user.getWantsUpdates() && this.userService.checkFlatWithUserChoice(rentFlat, user.getUserChoice())) { // Предпочтения совпали
                    //TODO закомментил addRentChoice user.getUserChoice().addRentChoice(rentFlat); // Добавляю в предпочтения юзера
                    this.dataCache.newFlat(rentFlat); // Сохраняю квартиру в кэш
                    this.dataCache.saveUserCache(user); // Сохраняю в кэш изменения UserChoice

                    userBotSendingQueue.addBulkMessageToQueue(this.flatMessageService
                            .getMessageFromFlat(user.getChatId().toString(), rentFlat, foundNewFlatForYou, true));
                }
            }
            // Формирую сообщение в канал
            msgToChannel = this.flatMessageService
                    .getMessageFromFlat(programVariables.getTelegramChannel(), rentFlat, false);
        } else {
            BuyFlat buyFlat = buyFlatService.save(new BuyFlat(admin.getAdminChoice())); // Сохраняю квартиру в БД
            PublishedFlatKeyboard publishedFlatKeyboard = new PublishedFlatKeyboard(admin.getAdminChoice(), buyFlat);
            admin.getAdminChoice().setFlatId(buyFlat.getId()); // Если уже есть айди добавленной квартиры
            result = messagesVariables.getAdminBotHashTag(buyFlat.getId().toString(), false); // Хэштег 12345_продажа;
            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (user.getWantsUpdates() && this.userService.checkFlatWithUserChoice(buyFlat, user.getUserChoice())) {
                    //TODO закомментил addBuyChoice user.getUserChoice().addBuyChoice(buyFlat); // Добавляю в предпочтения юзера
                    this.dataCache.newFlat(buyFlat); // Сохраняю квартиру в кэш
                    this.dataCache.saveUserCache(user); // Сохраняю в кэш изменения UserChoice

                    userBotSendingQueue.addBulkMessageToQueue(this.flatMessageService
                            .getMessageFromFlat(user.getChatId().toString(), buyFlat, foundNewFlatForYou, true));
                }
            }
            // Формирую сообщение в канал
            msgToChannel = this.flatMessageService
                    .getMessageFromFlat(programVariables.getTelegramChannel(), buyFlat, false);
        }

        // Публикую в канал, если квартира под аренду и если нет ошибки
        if (admin.getAdminChoice().getIsRentFlat() && !result.equals("ERROR")) {
            response.add(msgToChannel);
            /*response.add(SendMessage.builder()
                    .chatId(programVariables.getTelegramChannel())
                    .text(withoutNewFlatForYou)
                    .parseMode(sendMessage.getParseMode())
                    .build());*/
        }

        return result;
    }
}
