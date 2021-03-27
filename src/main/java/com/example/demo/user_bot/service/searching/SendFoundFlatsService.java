package com.example.demo.user_bot.service.searching;

import com.example.demo.common_part.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.service.FlatMessageService;
import com.example.demo.user_bot.utils.MenuSendMessage;
import com.example.demo.user_bot.utils.UserState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public final class SendFoundFlatsService {
    private static final Logger LOGGER = Logger.getLogger(SendFoundFlatsService.class);

    private final DataCache dataCache;
    private final ProgramVariables programVariables;
    private final UserBotSendingQueue userBotSendingQueue;
    private final FlatMessageService flatMessageService;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final FindFlatsService findFlatsService;

    @Autowired
    public SendFoundFlatsService(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, FlatMessageService flatMessageService, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FindFlatsService findFlatsService) {
        this.dataCache = dataCache;
        this.programVariables = programVariables;
        this.userBotSendingQueue = userBotSendingQueue;
        this.flatMessageService = flatMessageService;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.findFlatsService = findFlatsService;
    }

    // Главный метод по поиску и отправке квартир пользователю
    public void sendFoundFlats(UserCache user, List<BotApiMethod<?>> response) {
        if (user.getUserChoice().getIsRentFlat()) { // Если пользователь ищет квартиру под аренду
            List<RentFlat> notSentRentFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<RentFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            this.findFlatsService.findRentFlatsForUser(user, notSentRentFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceRentFlats(userChoiceFlats, user);

            if (notSentRentFlats.size() == 0) { // Если не нашлось квартир
                response.add(this.flatsNotFoundMessage(user));
            } else { // Если подходящие квартиры есть
                this.sendFoundRentFlats(user);
            }
        } else { // Если пользователь ищет квартиру для покупки
            List<BuyFlat> notSentBuyFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<BuyFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            this.findFlatsService.findBuyFlatsForUser(user, notSentBuyFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceBuyFlats(userChoiceFlats, user);

            if (userChoiceFlats.size() == 0) { // Если не нашлось квартир
                response.add(this.flatsNotFoundMessage(user));
            } else { // Если подходящие квартиры есть
                this.sendFoundSentBuyFlats(user);
            }
        }

        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }

    public void sendFoundRentFlats(UserCache user) { // Отправка найденных квартир юзеру
        List<RentFlat> notSentRentFlats = dataCache.getNotSentRentFlatsMap().get(user.getChatId());
        if (notSentRentFlats == null) { // Если список еще не был создан ранее - создаем
            notSentRentFlats = new ArrayList<>();
        }

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        if (notSentRentFlats.size() <= programVariables.getFlatsNumberPerChat()) { // Если неотправленных квартир меньше, чем за один раз можно отправить
            this.sendAllNotSentRent(notSentRentFlats, user); // Отправляю все
            user.setBotUserState(UserState.MENU1); // Отправили все нужные квартиры - переходим в главное меню
            user.getUserChoice().setMenuMessageId(null); // Удаляю меню
        } else { // Если неотправленных квартир больше, чем можем отправить за раз - отправляем фиксированное число
            this.sendNotAllNotSentRent(notSentRentFlats, user); // Отправить не все квартиры (фиксированное число)
            user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние - отправили не все квартиры
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
    }
    public void sendFoundSentBuyFlats(UserCache user) { // Отправка найденных квартир юзеру
        List<BuyFlat> notSentBuyFlats = dataCache.getNotSentBuyFlatsMap().get(user.getChatId());
        if (notSentBuyFlats == null) { // Если список еще не был создан ранее - создаем
            notSentBuyFlats = new ArrayList<>();
        }

        user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        if (notSentBuyFlats.size() <= programVariables.getFlatsNumberPerChat()) { // Если неотправленных квартир меньше, чем за один раз можно отправить
            this.sendAllNotSentBuy(notSentBuyFlats, user); // Отправляю все
            user.setBotUserState(UserState.MENU1); // Отправили все нужные квартиры - переходим в главное меню
            user.getUserChoice().setMenuMessageId(null); // Удаляю меню
        } else { // Если неотправленных квартир больше, чем можем отправить за раз - отправляем фиксированное число
            this.sendNotAllNotSentBuy(notSentBuyFlats, user); // Отправить не все (факсированное число)
            user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние - отправили не все квартиры
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
    }

    private void sendAllNotSentRent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        for (RentFlat temp : userNotSentRentFlats) { // Отправляю все
            SendMessage sendMessage = this.flatMessageService
                    .getMessageFromFlat(user.getChatId().toString(), temp, true);
            userBotSendingQueue.addMessageToQueue(sendMessage);
        }
        this.sentAllMessage(user.getChatId().toString()); // Отправляю сообщение "Вот все что мне удалось найти"
        userNotSentRentFlats.clear(); // Чищу со списка - т.к. уже отправили неотправленные квартиры
    }
    private void sendAllNotSentBuy(List<BuyFlat> userNotSentBuyFlats, UserCache user) {
        for (BuyFlat temp : userNotSentBuyFlats) { // Отправляю все
            SendMessage sendMessage = this.flatMessageService
                    .getMessageFromFlat(user.getChatId().toString(), temp, true);
            userBotSendingQueue.addMessageToQueue(sendMessage);
        }
        this.sentAllMessage(user.getChatId().toString()); // Отправляю сообщение "Вот все что мне удалось найти"
        userNotSentBuyFlats.clear(); // Чищу со списка - т.к. уже отправили неотправленные квартиры
    }

    private void sendNotAllNotSentRent(List<RentFlat> userNotSentRentFlats, UserCache user) {
        int c = 0;
        int notSent = 0; // Количество неотправленных квартир
        // Вспомогательный лист, чтобы не возникал java.util.ConcurrentModificationException
        List<RentFlat> listOfSentFlats = new ArrayList<>();
        for (RentFlat temp : userNotSentRentFlats) {
            if (c < programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                SendMessage sendMessage = this.flatMessageService
                        .getMessageFromFlat(user.getChatId().toString(), temp, true);
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
            if (c < programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                SendMessage sendMessage = this.flatMessageService
                        .getMessageFromFlat(user.getChatId().toString(), temp, true);
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

    // Сообщение "Есть еще квартиры. Если хочешь увидеть - жми кнопку"
    private void sentNotAllMessage(String chatId, int notSent) {
        MenuSendMessage sentNotAll = new MenuSendMessage();
        sentNotAll.setChatId(chatId);
        sentNotAll.setText(messagesVariables.getUserSentNotAllFLatsText(notSent + ""));
        sentNotAll.setReplyMarkup(keyboardsRegistry.getNotAllRentFlatsKeyboard().getKeyboard());
        sentNotAll.setChangeMenuMessageId(true); // Сохраняю айдишник меню
        userBotSendingQueue.addMessageToQueue(sentNotAll);
    }

    // Сообщение "Это все что есть"
    private void sentAllMessage(String chatId) {
        SendMessage sentAll = new SendMessage();
        sentAll.setChatId(chatId);
        sentAll.setText(messagesVariables.getUserSentAllFlats());
        sentAll.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard());
        userBotSendingQueue.addMessageToQueue(sentAll);
    }

    // Сообщение
    private SendMessage flatsNotFoundMessage(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserSentNoFlatsText());
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard()); // Меню 1

        user.setBotUserState(UserState.MENU1); // Перешли в главное меню
        this.dataCache.saveUserCache(user); // Сохраняю в базу измененное состояние

        return sendMessage;
    }
}
