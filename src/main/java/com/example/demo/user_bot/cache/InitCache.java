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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public final class InitCache {
    private static final Logger LOGGER = Logger.getLogger(InitCache.class);

    private final DataCache dataCache;
    private final UserService userService;
    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;

    @Autowired
    public InitCache(DataCache dataCache, UserService userService, RentalFlatService rentalFlatService, BuyFlatService buyFlatService) {
        this.dataCache = dataCache;
        this.userService = userService;
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;

        this.dataCache.setUsersCacheMap(this.initUsersCache()); // Загружаю юзеров из базы в кэш
        this.dataCache.setRentFlatsCacheMap(this.initRentFlatsCache()); // Квартиру под аренду из базы в кэш
        this.dataCache.setBuyFlatsCacheMap(this.initBuyFlatsCache()); // Квартиру под продажу из базы в кэш
    }

    Map<Long, BuyFlat> initBuyFlatsCache() {
        List<BuyFlat> allExistedBuyFlats = this.buyFlatService.findAllBuyFlats();
        Map<Long, BuyFlat> buyFlatsCache = new HashMap<>();
        for (BuyFlat temp: allExistedBuyFlats) {
            buyFlatsCache.put(temp.getId(), temp);
        }
        return buyFlatsCache;
    }
    Map<Long, RentFlat> initRentFlatsCache() {
        List<RentFlat> allExistedRentFlats = this.rentalFlatService.findAllRentFlats();
        Map<Long, RentFlat> rentFlatsCache = new HashMap<>();
        for (RentFlat temp: allExistedRentFlats) {
            rentFlatsCache.put(temp.getId(), temp);
        }
        return rentFlatsCache;
    }

    Map<Long, UserCache> initUsersCache() {
        List<User> allExistedUsers = userService.findAllUsers();
        Map<Long, UserCache> usersCache = new HashMap<>();
        for (User temp: allExistedUsers) {
            // saved ставлю true, потому что в базе он уже есть
            usersCache.put(temp.getChatId(), new UserCache(temp, true));
        }
        return usersCache;
    }
}
