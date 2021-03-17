package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.service.entities.UserService;
import com.example.demo.user_bot.service.publishing.SendFoundFlatsService;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

@Service
public final class InitMenuEndHandler {
    private static final Logger LOGGER = Logger.getLogger(InitMenuEndHandler.class);
    private final DataCache dataCache;
    private final MessagesVariables messagesVariables;
    private final SendFoundFlatsService sendFoundFlatsService;
    private final UserService userService;

    @Autowired
    public InitMenuEndHandler(DataCache dataCache, MessagesVariables messagesVariables, SendFoundFlatsService sendFoundFlatsService, UserService userService) {
        this.dataCache = dataCache;
        this.messagesVariables = messagesVariables;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.userService = userService;
    }

    public void handleInitMenuEnd(List<BotApiMethod<?>> response, UserCache user) {
        LOGGER.info("handleInitMenuEnd!");
        if (user.getUserChoice().getIsRentFlat()) { // Если выбрали аренду
            this.processRentFlats(user, response);
        } else { // Если выбрали покупку
            this.processBuyFlats(user, response);
        }
        this.dataCache.markNotSaved(user.getChatId()); // Помечаю юзера несохраненным - чтобы обновить в базе
    }

    private void processRentFlats(UserCache user, List<BotApiMethod<?>> response) {
        UserChoice userChoice = user.getUserChoice();

        Map<Long, RentFlat> rentFlatsCacheMap = dataCache.getRentFlatsCacheMap(); // Достаю все квартиры под аренду из кэша

        List<RentFlat> notSentRentFlats = new ArrayList<>(); // Список неотправленных юзеру квартир

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        Set<RentFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

        // Сравниваю параметры всех квартир с предпочтениями пользователя
        for (Map.Entry<Long, RentFlat> entry : rentFlatsCacheMap.entrySet()) {
            RentFlat rentFlat = entry.getValue(); // Очередная квартира
            if (this.userService.checkFlatWithUserChoice(rentFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(rentFlat); // Добавляю квартиру к выбору пользователя
                if (user.getSaved()) { // У юзера изменилось поле UserChoice и его нужно будет сохранить в базу
                    user.setSaved(false);
                }
                notSentRentFlats.add(rentFlat); // Пополняю список неотправленных юзеру квартир
            }
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
        // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
        this.dataCache.setUserChoiceRentFlats(userChoiceFlats, user);

        if (notSentRentFlats.size() == 0) { // Если не нашлось квартир
            response.add(this.flatsNotFoundMessage(user.getChatId().toString()));
        } else { // Если подходящие квартиры есть
            sendFoundFlatsService.sendNotSentRentFlats(user);
        }

        this.dataCache.saveUser(user); // Сохраняю изменения юзера
    }
    private void processBuyFlats(UserCache user, List<BotApiMethod<?>> response) {
        UserChoice userChoice = user.getUserChoice();
        Map<Long, BuyFlat> buyFlatsCacheMap = dataCache.getBuyFlatsCacheMap(); // Достаю все квартиры под продажу из кэша

        List<BuyFlat> notSentBuyFlats = new ArrayList<>(); // Список неотправленных юзеру квартир

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        Set<BuyFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

        // Сравниваю параметры всех квартир с предпочтениями пользователя
        for (Map.Entry<Long, BuyFlat> entry : buyFlatsCacheMap.entrySet()) {
            BuyFlat buyFlat = entry.getValue(); // Очередная квартира
            if (this.userService.checkFlatWithUserChoice(buyFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(buyFlat); // Добавляю квартиру к выбору пользователя
                if (user.getSaved()) { // У юзера изменилось поле UserChoice и его нужно будет сохранить в базу
                    user.setSaved(false);
                }
                notSentBuyFlats.add(buyFlat); // Пополняю список неотправленных юзеру квартир
            }
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
        // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
        this.dataCache.setUserChoiceBuyFlats(userChoiceFlats, user);

        if (userChoiceFlats.size() == 0) { // Если не нашлось квартир
            response.add(this.flatsNotFoundMessage(user.getChatId().toString()));
        } else { // Если подходящие квартиры есть
            sendFoundFlatsService.sendNotSentBuyFlats(user);
        }

        this.dataCache.saveUser(user); // Сохраняю изменения юзера
    }

    private SendMessage flatsNotFoundMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messagesVariables.getUserSentNoFlatsText());
        return sendMessage;
    }

    /*private void sendSentNotAllMessage(String chatId, int notSent) {
        SendMessage sentNotAll = new SendMessage();
        sentNotAll.setChatId(chatId);
        sentNotAll.setText(messagesVariables.getUserSentNotAllFLatsText(notSent + ""));
        sentNotAll.setReplyMarkup(keyboardsRegistry.getNotAllRentFlatsKeyboard().getKeyboard());
        userBotSendingQueue.addBulkMessageToQueue(sentNotAll);
    }*/
}
