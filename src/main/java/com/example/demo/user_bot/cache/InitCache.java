package com.example.demo.user_bot.cache;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.service.entities.BuyFlatService;
import com.example.demo.user_bot.service.entities.RentalFlatService;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class InitCache {
    private static final Logger LOGGER = Logger.getLogger(InitCache.class);

    private final UserService userService;
    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;
    private final DataCache dataCache;

    @Autowired
    public InitCache(UserService userService, RentalFlatService rentalFlatService, BuyFlatService buyFlatService, DataCache dataCache) {
        this.userService = userService;
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.dataCache = dataCache;

        this.dataCache.setUsersCacheMap(this.initUsersCache()); // Загружаю юзеров из базы в кэш
        this.dataCache.setRentFlatsCacheMap(this.initRentFlatsCache()); // Квартиру под аренду из базы в кэш
        this.dataCache.setBuyFlatsCacheMap(this.initBuyFlatsCache()); // Квартиру под продажу из базы в кэш
    }

    Map<Long, BuyFlat> initBuyFlatsCache() {
        List<BuyFlat> allExistedBuyFlats = buyFlatService.findAllBuyFlats();
        Map<Long, BuyFlat> buyFlatsCache = new ConcurrentHashMap<>();
        for (BuyFlat temp: allExistedBuyFlats) {
            buyFlatsCache.put(temp.getId(), temp);
        }
        LOGGER.info("ALL BuyFlats FROM DATABASE TO CACHE");
        return buyFlatsCache;
    }
    Map<Long, RentFlat> initRentFlatsCache() {
        List<RentFlat> allExistedRentFlats = rentalFlatService.findAllRentFlats();
        Map<Long, RentFlat> rentFlatsCache = new ConcurrentHashMap<>();
        for (RentFlat temp: allExistedRentFlats) {
            rentFlatsCache.put(temp.getId(), temp);
        }
        LOGGER.info("ALL RentFlats FROM DATABASE TO CACHE");
        return rentFlatsCache;
    }

    Map<Long, UserCache> initUsersCache() {
        List<User> allExistedUsers = userService.findAllUsers();
        Map<Long, UserCache> usersCache = new ConcurrentHashMap<>();
        for (User temp: allExistedUsers) {
            // saved ставлю true, потому что в базе он уже есть
            usersCache.put(temp.getChatId(), new UserCache(temp, true));
        }
        LOGGER.info("ALL Users FROM DATABASE TO CACHE");
        return usersCache;
    }
}
