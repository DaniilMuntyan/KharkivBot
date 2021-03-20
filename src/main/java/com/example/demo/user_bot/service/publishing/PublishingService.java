package com.example.demo.user_bot.service.publishing;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
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

    public String publish(User admin, List<BotApiMethod<?>> response) {
        List<User> allUsers = userService.findAllUsers();
        String result;

        PublishedFlatKeyboard publishedFlatKeyboard = new PublishedFlatKeyboard(admin.getAdminChoice());

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(publishedFlatKeyboard.getKeyboard());
        String foundNewFlatForYou = messagesVariables.getAdminFoundNewFlat() + "\n";
        String withoutNewFlatForYou = "."; // Квартира без надписи "Нашел новую квартиру для тебя"
        if (admin.getAdminChoice().getIsRentFlat()) {
            RentFlat rentFlat = rentalFlatService.save(new RentFlat(admin.getAdminChoice()));
            admin.getAdminChoice().setFlatId(rentFlat.getId()); // Если уже есть айди добавленной квартиры
            sendMessage.setText(foundNewFlatForYou + rentFlat.getHtmlMessage());
            withoutNewFlatForYou = rentFlat.getHtmlMessage(); // Без "Нашел новую квартиру для тебя"
            result = rentFlat.getId().toString();
            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (this.userService.checkFlatWithUserChoice(rentFlat, user.getUserChoice())) { // Предпочтения совпали
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
            admin.getAdminChoice().setFlatId(buyFlat.getId()); // Если уже есть айди добавленной квартиры
            sendMessage.setText(foundNewFlatForYou + buyFlat.getHtmlMessage());
            withoutNewFlatForYou = buyFlat.getHtmlMessage(); // Без "Нашел новую квартиру для тебя"
            result = buyFlat.getId().toString();

            // Отправляю только тем пользователям, у которых соответствующие предпочтения
            for (User user: allUsers) {
                if (this.userService.checkFlatWithUserChoice(buyFlat, user.getUserChoice())) {
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
