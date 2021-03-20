package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.admin_bot.constants.MessagesVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.keyboards.KeyboardsRegistry;
import com.example.demo.user_bot.service.publishing.FindFlatsService;
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
    private final KeyboardsRegistry keyboardsRegistry;
    private final FindFlatsService findFlatsService;

    @Autowired
    public InitMenuEndHandler(DataCache dataCache, MessagesVariables messagesVariables, SendFoundFlatsService sendFoundFlatsService, KeyboardsRegistry keyboardsRegistry, FindFlatsService findFlatsService) {
        this.dataCache = dataCache;
        this.messagesVariables = messagesVariables;
        this.sendFoundFlatsService = sendFoundFlatsService;
        this.keyboardsRegistry = keyboardsRegistry;
        this.findFlatsService = findFlatsService;
    }

    public void handleInitMenuEnd(List<BotApiMethod<?>> response, UserCache user) {
        LOGGER.info("handleInitMenuEnd!");
        if (user.getUserChoice().getIsRentFlat()) { // Если выбрали аренду
            this.processRentFlats(user, response);
        } else { // Если выбрали покупку
            this.processBuyFlats(user, response);
        }
        //this.dataCache.markNotSaved(user.getChatId()); // Помечаю юзера несохраненным - чтобы обновить в базе
        this.dataCache.saveUserCache(user);
    }

    private void processRentFlats(UserCache user, List<BotApiMethod<?>> response) {
        List<RentFlat> notSentRentFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
        Set<RentFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

        this.findFlatsService.findRentFlatsForUser(user, notSentRentFlats, userChoiceFlats); // Заполняю списки

        // Устанавливаю список неотправленных квартир юзеру
        this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
        // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
        this.dataCache.setUserChoiceRentFlats(userChoiceFlats, user);

        //user.setBotUserState(UserState.FLATS_MASSAGING); // Состояние - шлём квартиры

        if (notSentRentFlats.size() == 0) { // Если не нашлось квартир
            response.add(this.flatsNotFoundMessage(user));
        } else { // Если подходящие квартиры есть
            sendFoundFlatsService.sendNotSentRentFlats(user);
        }

        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }
    private void processBuyFlats(UserCache user, List<BotApiMethod<?>> response) {
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
            sendFoundFlatsService.sendNotSentBuyFlats(user);
        }

        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }

    private SendMessage flatsNotFoundMessage(UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(messagesVariables.getUserSentNoFlatsText());
        sendMessage.setReplyMarkup(keyboardsRegistry.getMenu1().getKeyboard()); // Меню 1

        user.setBotUserState(UserState.MENU1); // Перешли в главное меню
        //this.dataCache.markNotSaved(user.getChatId()); // Сохраняю в базу измененное состояние
        this.dataCache.saveUserCache(user);

        return sendMessage;
    }
}
