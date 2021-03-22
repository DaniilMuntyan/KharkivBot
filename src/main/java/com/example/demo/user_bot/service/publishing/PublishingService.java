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

    private final DataCache dataCache;

    @Autowired
    public PublishingService(RentalFlatService rentalFlatService, BuyFlatService buyFlatService, ProgramVariables programVariables, UserService userService, UserBotSendingQueue userBotSendingQueue, MessagesVariables messagesVariables, DataCache dataCache) {
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.programVariables = programVariables;
        this.userService = userService;
        this.userBotSendingQueue = userBotSendingQueue;
        this.messagesVariables = messagesVariables;
        this.dataCache = dataCache;
    }

    public String publish(UserCache admin, List<BotApiMethod<?>> response) {
        List<User> allUsers = userService.findAllUsers();
        String result;

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        String foundNewFlatForYou = messagesVariables.getAdminFoundNewFlat() + "\n";
        String withoutNewFlatForYou = "."; // Квартира без надписи "Нашел новую квартиру для тебя"
        if (admin.getAdminChoice().getIsRentFlat()) {
            RentFlat rentFlat = rentalFlatService.save(new RentFlat(admin.getAdminChoice()));
            PublishedFlatKeyboard publishedFlatKeyboard = new PublishedFlatKeyboard(admin.getAdminChoice(), rentFlat);
            admin.getAdminChoice().setFlatId(rentFlat.getId()); // Когда уже есть айди добавленной квартиры
            sendMessage.setText(foundNewFlatForYou + rentFlat.getHtmlMessage());
            sendMessage.setReplyMarkup(publishedFlatKeyboard.getKeyboard()); // Установаливаю клавиатуру для это квартиры под аренду
            withoutNewFlatForYou = rentFlat.getHtmlMessage(); // Без "Нашел новую квартиру для тебя"
            result = rentFlat.getId().toString();
            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (user.getWantsUpdates() && this.userService.checkFlatWithUserChoice(rentFlat, user.getUserChoice())) { // Предпочтения совпали
                    user.getUserChoice().addRentChoice(rentFlat); // Добавляю в предпочтения юзера
                    this.dataCache.newRentFlat(rentFlat); // Сохраняю квартиру в кэш
                    this.dataCache.saveUserCache(user); // Сохраняю в кэш изменения UserChoice

                    userBotSendingQueue.addBulkMessageToQueue(SendMessage.builder()
                            .text(sendMessage.getText())
                            .chatId(user.getChatId().toString())
                            .parseMode(sendMessage.getParseMode())
                            .build());
                }
            }
        } else {
            BuyFlat buyFlat = buyFlatService.save(new BuyFlat(admin.getAdminChoice()));
            PublishedFlatKeyboard publishedFlatKeyboard = new PublishedFlatKeyboard(admin.getAdminChoice(), buyFlat);
            admin.getAdminChoice().setFlatId(buyFlat.getId()); // Если уже есть айди добавленной квартиры
            sendMessage.setText(foundNewFlatForYou + buyFlat.getHtmlMessage());
            sendMessage.setReplyMarkup(publishedFlatKeyboard.getKeyboard());
            withoutNewFlatForYou = buyFlat.getHtmlMessage(); // Без "Нашел новую квартиру для тебя"
            result = buyFlat.getId().toString();

            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (user.getWantsUpdates() && this.userService.checkFlatWithUserChoice(buyFlat, user.getUserChoice())) {
                    user.getUserChoice().addBuyChoice(buyFlat); // Добавляю в предпочтения юзера
                    this.dataCache.newBuyFlat(buyFlat); // Сохраняю квартиру в кэш
                    this.dataCache.saveUserCache(user); // Сохраняю в кэш изменения UserChoice
                    //this.dataCache.markNotSaved(user.getChatId()); // Сохранить изменения в базу

                    userBotSendingQueue.addBulkMessageToQueue(SendMessage.builder()
                            .text(sendMessage.getText())
                            .chatId(user.getChatId().toString())
                            .parseMode(sendMessage.getParseMode())
                            .build());
                }
            }
        }

        // Публикую в канал, если квартира под аренду и если нет ошибки
        if (admin.getAdminChoice().getIsRentFlat() && !result.equals("ERROR")) {
            response.add(SendMessage.builder()
                    .chatId(programVariables.getTelegramChannel())
                    .text(withoutNewFlatForYou)
                    .parseMode(sendMessage.getParseMode())
                    .build());
        }

        return result;
    }
}
