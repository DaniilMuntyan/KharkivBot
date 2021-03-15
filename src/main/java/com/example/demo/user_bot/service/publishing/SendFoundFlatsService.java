package com.example.demo.user_bot.service.publishing;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
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
import java.util.Map;

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
        Map<Long, ArrayList<RentFlat>> notSentRentFlats = dataCache.getNotSentRentFlatsMap();
        List<RentFlat> userNotSentRentFlats = notSentRentFlats.get(user.getChatId());
        if (userNotSentRentFlats == null) { // Если список еще не был создан ранее - создаем
            userNotSentRentFlats = new ArrayList<>();
        }
        user.setBotUserState(UserState.FLATS_MASSAGING); // Посылаем сообщения с квартирами
        LOGGER.info("userNotSentRentFlats.size(): " + userNotSentRentFlats.size());
        if (userNotSentRentFlats.size() <= programVariables.getFlatsNumberPerChat()) { // Если нашлось квартир меньше, чем за один раз можно отправить
            LOGGER.info("SEND ALL NOT SENT");
            this.sendAllNotSent(userNotSentRentFlats, user); // Отправляю все
            user.setBotUserState(UserState.INIT); // Отправили все нужные квартиры
        } else { // Если нашли больше квартир, чем можем отправить за раз - отправляем фиксированное число
            this.sendNotAllNotSent(userNotSentRentFlats, user);
            user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние - отправили не все квартиры
        }
    }

    private void sendAllNotSent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        for (RentFlat temp : userNotSentRentFlats) { // Отправляю все
            SendMessage sendMessage = this.flatMessageService.getMessageFromRentFlat(user.getChatId().toString(), temp);
            userBotSendingQueue.addBulkMessageToQueue(sendMessage);
        }
        userBotSendingQueue.addBulkMessageToQueue(this.sentAllMessage(user.getChatId().toString())); // Закончили подбор
        userNotSentRentFlats.clear(); // Чищу со списка - т.к. уже отправили неотправленные квартиры
    }

    private void sendNotAllNotSent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        int c = 0;
        int notSent = 0; // Количество неотправленных квартир
        // Вспомогательный лист, чтобы не возникал java.util.ConcurrentModificationException
        List<RentFlat> listOfSentFlats = new ArrayList<>();
        for (RentFlat temp : userNotSentRentFlats) {
            if (c <= programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                SendMessage sendMessage = this.flatMessageService.getMessageFromRentFlat(user.getChatId().toString(), temp);
                userBotSendingQueue.addBulkMessageToQueue(sendMessage);
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
        sentNotAllMessage(user.getChatId().toString(), notSent);
    }

    private void sentNotAllMessage(String chatId, int notSent) {
        SendMessage sentNotAll = new SendMessage();
        sentNotAll.setChatId(chatId);
        sentNotAll.setText(messagesVariables.getUserSentNotAllFLatsText(notSent + ""));
        sentNotAll.setReplyMarkup(keyboardsRegistry.getNotAllRentFlatsKeyboard().getKeyboard());
        userBotSendingQueue.addBulkMessageToQueue(sentNotAll);
    }

    private SendMessage sentAllMessage(String chatId) {
        SendMessage sentAll = new SendMessage();
        sentAll.setChatId(chatId);
        sentAll.setText(messagesVariables.getUserSentAllFlats());
        return sentAll;
    }
}
