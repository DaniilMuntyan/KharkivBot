package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import com.example.demo.user_bot.service.publishing.FlatMessageService;
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
    private final ProgramVariables programVariables;
    private final UserBotSendingQueue userBotSendingQueue;
    private final MessagesVariables messagesVariables;
    private final KeyboardsRegistry keyboardsRegistry;
    private final FlatMessageService flatMessageService;
    private final SendFoundFlatsService sendFoundFlatsService;

    @Autowired
    public InitMenuEndHandler(DataCache dataCache, ProgramVariables programVariables, UserBotSendingQueue userBotSendingQueue, MessagesVariables messagesVariables, KeyboardsRegistry keyboardsRegistry, FlatMessageService flatMessageService, SendFoundFlatsService sendFoundFlatsService) {
        this.dataCache = dataCache;
        this.programVariables = programVariables;
        this.userBotSendingQueue = userBotSendingQueue;
        this.messagesVariables = messagesVariables;
        this.keyboardsRegistry = keyboardsRegistry;
        this.flatMessageService = flatMessageService;
        this.sendFoundFlatsService = sendFoundFlatsService;
    }

    public void handleInitMenuEnd(List<BotApiMethod<?>> response, UserCache user) {
        UserChoice userChoice = user.getUserChoice();
        Map<Long, RentFlat> rentFlatsCacheMap = dataCache.getRentFlatsCacheMap(); // Достаю все квартиры под аренду из кэша
        Map<Long, ArrayList<RentFlat>> notSentRentFlats = dataCache.getNotSentRentFlatsMap(); // Не отправленные юзеру квартиры
        List<RentFlat> flatsToUser = notSentRentFlats.get(user.getChatId()); // Список неотправленных квартир для юзера
        if (flatsToUser != null) {
            flatsToUser.clear(); // Чищу список неотправленных квартир для пользователя, так как будут уже новые варианты
        } else {
            flatsToUser = new ArrayList<>(); // Если ранее не был создан - создаю
        }
        user.setBotUserState(UserState.FLATS_MASSAGING); // Шлём квартиры
        // Сравниваю их параметры всех квартир с предпочтениями пользователя
        for (Map.Entry<Long, RentFlat> entry : rentFlatsCacheMap.entrySet()) {
            RentFlat rentFlat = entry.getValue(); // Очередная квартира
            Set<RentFlat> userChoiceFlats = userChoice.getUserChoicesRent();
            if (userChoiceFlats == null) { // Если этот выбор пользователя еще не имеет своих вариантов квартир
                userChoiceFlats = new HashSet<>();
            }
            if (this.checkFlatWithUserChoice(rentFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(rentFlat); // Добавляю квартиры к выбору пользователя
                if (user.getSaved()) { // У юзера изменилось поле UserChoice и его нужно будет сохранить в базу
                    user.setSaved(false);
                }
                flatsToUser.add(rentFlat);// Добавляю подходящий вариант для пользователя
            }
        }

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentRentFlats((ArrayList<RentFlat>) flatsToUser, user);
        LOGGER.info("flatsToUser: " + flatsToUser);

        if (flatsToUser.size() == 0) { // Если не нашлось квартир
            response.add(this.flatsNotFoundMessage(user.getChatId().toString()));
        }
        else { // Если подходящие квартиры есть
            sendFoundFlatsService.sendNotSentRentFlats(user);
            /*LOGGER.info("flatsToUser.size(): " + flatsToUser.size() + " " + "getFlatsNumberPerChat:" +
                    programVariables.getFlatsNumberPerChat());
            if (flatsToUser.size() <= programVariables.getFlatsNumberPerChat()) { // Если нашлось квартир меньше, чем за один раз можно отправить
                for (RentFlat temp: flatsToUser) { // Отправляю все
                    SendMessage sendMessage = flatMessageService.getMessageFromRentFlat(user.getChatId().toString(), temp);
                    userBotSendingQueue.addBulkMessageToQueue(sendMessage);
                }
                this.sendSentAllMessage(user.getChatId().toString()); // Отправляю сообщение - все квартиры показал
                user.setBotUserState(UserState.INIT); // Отправили все нужные квартиры
            } else { // Если нашли больше квартир, чем можем отправить за раз - отправляем фиксированное число
                int c = 0;
                int notSent = 0; // Количество не отправленных квартир
                for (RentFlat temp: flatsToUser) {
                    if (c <= programVariables.getFlatsNumberPerChat()) { // Отправляю первые N
                        SendMessage sendMessage = flatMessageService.getMessageFromRentFlat(user.getChatId().toString(), temp);
                        LOGGER.info(sendMessage);
                        userBotSendingQueue.addBulkMessageToQueue(sendMessage);
                    } else {
                        dataCache.addNotSentFlat(user, temp);
                        notSent++;
                    }
                    c++;
                }
                this.sendSentNotAllMessage(user.getChatId().toString(), notSent); // Отправляю сообщение "Показать еще"
                user.setBotUserState(UserState.SENT_NOT_ALL); // Меняю состояние
            }*/
        }
    }

    private SendMessage flatsNotFoundMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messagesVariables.getUserSentNoFlatsText());
        return sendMessage;
    }

    private void sendSentNotAllMessage(String chatId, int notSent) {
        SendMessage sentNotAll = new SendMessage();
        sentNotAll.setChatId(chatId);
        sentNotAll.setText(messagesVariables.getUserSentNotAllFLatsText(notSent + ""));
        sentNotAll.setReplyMarkup(keyboardsRegistry.getNotAllRentFlatsKeyboard().getKeyboard());
        userBotSendingQueue.addBulkMessageToQueue(sentNotAll);
    }


    private boolean checkFlatWithUserChoice(RentFlat flat, UserChoice userChoice) {
        return userChoice.getBudget().contains(flat.getRange().getIdentifier()) &&
                userChoice.getRooms().contains(flat.getRooms().getIdentifier()) &&
                userChoice.getDistricts().contains(flat.getDistrict().getIdentifier());
    }
}
