package com.example.demo.user_bot.service.publishing;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class FindFlatsService {
    private static final Logger LOGGER = Logger.getLogger(FindFlatsService.class);
    private final DataCache dataCache;
    private final UserService userService;

    @Autowired
    public FindFlatsService(DataCache dataCache, UserService userService) {
        this.dataCache = dataCache;
        this.userService = userService;
    }

    // Ищу квартиры под аренду для пользователя
    public void findRentFlatsForUser(UserCache user, List<RentFlat> notSentRentFlats, Set<RentFlat> userChoiceFlats) {
        UserChoice userChoice = user.getUserChoice();

        Map<Long, RentFlat> rentFlatsCacheMap = dataCache.getRentFlatsCacheMap(); // Достаю все квартиры под аренду из кэша

        // Сравниваю параметры всех квартир с предпочтениями пользователя
        for (Map.Entry<Long, RentFlat> entry : rentFlatsCacheMap.entrySet()) {
            RentFlat rentFlat = entry.getValue(); // Очередная квартира
            if (this.userService.checkFlatWithUserChoice(rentFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(rentFlat); // Добавляю квартиру к выбору пользователя
                if (user.getSaved()) { // У юзера изменилось поле UserChoice и его нужно будет сохранить в базу
                    this.dataCache.saveUserCache(user); // TODO: проверить, можно ли использовать markNotSaved вместо saveUserCache
                    //this.dataCache.markNotSaved(user.getChatId());
                }
                notSentRentFlats.add(rentFlat); // Пополняю список неотправленных юзеру квартир
            }
        }
    }
    // Ищу квартиры под продажу для пользователя
    public void findBuyFlatsForUser(UserCache user, List<BuyFlat> notSentBuyFlats, Set<BuyFlat> userChoiceFlats) {
        UserChoice userChoice = user.getUserChoice();
        Map<Long, BuyFlat> buyFlatsCacheMap = dataCache.getBuyFlatsCacheMap(); // Достаю все квартиры под продажу из кэша

        // Сравниваю параметры всех квартир с предпочтениями пользователя
        for (Map.Entry<Long, BuyFlat> entry : buyFlatsCacheMap.entrySet()) {
            BuyFlat buyFlat = entry.getValue(); // Очередная квартира
            if (this.userService.checkFlatWithUserChoice(buyFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(buyFlat); // Добавляю квартиру к выбору пользователя
                if (user.getSaved()) { // У юзера изменилось поле UserChoice и его нужно будет сохранить в базу
                    this.dataCache.saveUserCache(user);
                    //this.dataCache.markNotSaved(user.getChatId());
                }
                notSentBuyFlats.add(buyFlat); // Пополняю список неотправленных юзеру квартир
            }
        }
    }
}
