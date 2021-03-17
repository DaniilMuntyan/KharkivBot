package com.example.demo.user_bot.service.publishing;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

@Service
public final class SendFoundFlatsService {
    private static final Logger LOGGER = Logger.getLogger(SendFoundFlatsService.class);

    private final DataCache dataCache;
    private final ProgramVariables programVariables;
    private final UserBotSendingQueue userBotSendingQueue;
    private final FlatMessageService flatMessageService;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;

    @Autowired
    public SendFoundFlatsService(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, FlatMessageService flatMessageService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry) {
        this.dataCache = dataCache;
        this.programVariables = programVariables;
        this.userBotSendingQueue = userBotSendingQueue;
        this.flatMessageService = flatMessageService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
    }

    public void sendNotSentRentFlats(UserCache user) {
        LOGGER.info("user getUserChoicesRent " + user.getUserChoice().getUserChoicesRent());

        List<RentFlat> notSentRentFlats = dataCache.getNotSentRentFlatsMap().get(user.getChatId());
        if (notSentRentFlats == null) { // Если список еще не был создан ранее - создаем
            notSentRentFlats = new ArrayList<>();
        }

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        if (notSentRentFlats.size() <= programVariables.getFlatsNumberPerChat()) { // Если неотправленных квартир меньше, чем за один раз можно отправить
            this.sendAllNotSentRent(notSentRentFlats, user); // Отправляю все
            user.setBotUserState(UserState.INIT); // Отправили все нужные квартиры
        } else { // Если неотправленных больше квартир, чем можем отправить за раз - отправляем фиксированное число
            this.sendNotAllNotSentRent(notSentRentFlats, user);
            user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние - отправили не все квартиры
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
    }
    public void sendNotSentBuyFlats(UserCache user) {
        List<BuyFlat> notSentBuyFlats = dataCache.getNotSentBuyFlatsMap().get(user.getChatId());
        if (notSentBuyFlats == null) { // Если список еще не был создан ранее - создаем
            notSentBuyFlats = new ArrayList<>();
        }

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        if (notSentBuyFlats.size() <= programVariables.getFlatsNumberPerChat()) { // Если неотправленных квартир меньше, чем за один раз можно отправить
            this.sendAllNotSentBuy(notSentBuyFlats, user); // Отправляю все
            user.setBotUserState(UserState.INIT); // Отправили все нужные квартиры
        } else { // Если неотправленных больше квартир, чем можем отправить за раз - отправляем фиксированное число
            this.sendNotAllNotSentBuy(notSentBuyFlats, user);
            user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние - отправили не все квартиры
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
    }

    private void sendAllNotSentRent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        for (RentFlat temp : userNotSentRentFlats) { // Отправляю все
            SendMessage sendMessage = this.flatMessageService.getMessageFromFlat(user.getChatId().toString(), temp);
            userBotSendingQueue.addMessageToQueue(sendMessage);
        }
        this.sentAllMessage(user.getChatId().toString()); // Закончили подбор
        userNotSentRentFlats.clear(); // Чищу со списка - т.к. уже отправили неотправленные квартиры
    }
    private void sendAllNotSentBuy(List<BuyFlat> userNotSentBuyFlats, UserCache user) {
        for (BuyFlat temp : userNotSentBuyFlats) { // Отправляю все
            SendMessage sendMessage = this.flatMessageService.getMessageFromFlat(user.getChatId().toString(), temp);
            userBotSendingQueue.addMessageToQueue(sendMessage);
        }
        this.sentAllMessage(user.getChatId().toString()); // Закончили подбор
        userNotSentBuyFlats.clear(); // Чищу со списка - т.к. уже отправили неотправленные квартиры
    }

    private void sendNotAllNotSentRent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        int c = 0;
        int notSent = 0; // Количество неотправленных квартир
        // Вспомогательный лист, чтобы не возникал java.util.ConcurrentModificationException
        List<RentFlat> listOfSentFlats = new ArrayList<>();
        for (RentFlat temp : userNotSentRentFlats) {
            if (c <= programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                SendMessage sendMessage = this.flatMessageService.getMessageFromFlat(user.getChatId().toString(), temp);
                userBotSendingQueue.addMessageToQueue(sendMessage);
                listOfSentFlats.add(temp); // Добавляю в список отправленных квартир
            } else {
                notSent++; // Количество неотправленных квартир
            }
            c++;
        }
        // Удаляю все отправленные квартиры со списка неотправленных для текущего пользователя
        for (RentFlat temp: listOfSentFlats) {
            dataCache.removeNotSentFlat(user, temp);
        }
        sentNotAllMessage(user.getChatId().toString(), notSent); // Отправляю сообщение "Показать еще"
    }
    private void sendNotAllNotSentBuy(List<BuyFlat> userNotSentBuyFlats, UserCache user) {
        int c = 0;
        int notSent = 0; // Количество неотправленных квартир
        // Вспомогательный лист, чтобы не возникал java.util.ConcurrentModificationException
        List<BuyFlat> listOfSentFlats = new ArrayList<>();
        for (BuyFlat temp : userNotSentBuyFlats) {
            if (c <= programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                SendMessage sendMessage = this.flatMessageService.getMessageFromFlat(user.getChatId().toString(), temp);
                userBotSendingQueue.addMessageToQueue(sendMessage);
                listOfSentFlats.add(temp); // Добавляю в список отправленных квартир
            } else {
                notSent++; // Количество неотправленных квартир
            }
            c++;
        }
        // Удаляю все отправленные квартиры со списка неотправленных для текущего пользователя
        for (BuyFlat temp: listOfSentFlats) {
            dataCache.removeNotSentFlat(user, temp);
        }
        sentNotAllMessage(user.getChatId().toString(), notSent); // Отправляю сообщение "Показать еще"
    }

    private void sentNotAllMessage(String chatId, int notSent) {
        SendMessage sentNotAll = new SendMessage();
        sentNotAll.setChatId(chatId);
        sentNotAll.setText(messagesVariables.getUserSentNotAllFLatsText(notSent + ""));
        sentNotAll.setReplyMarkup(keyboardsRegistry.getNotAllRentFlatsKeyboard().getKeyboard());
        userBotSendingQueue.addMessageToQueue(sentNotAll);
    }

    private void sentAllMessage(String chatId) {
        SendMessage sentAll = new SendMessage();
        sentAll.setChatId(chatId);
        sentAll.setText(messagesVariables.getUserSentAllFlats());
        userBotSendingQueue.addMessageToQueue(sentAll);
    }
}
