package com.example.demo.user_bot.cache;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class DataCache {
    private static final Logger LOGGER = Logger.getLogger(DataCache.class);

    // Необходимые для кэша параметры
    // Ключ - chatId, значение - объект UserCache
    private final Map<Long, UserCache> usersCache = new HashMap<>();
    // Set добавленных пользователей
    private final Set<User> newUsersSet = new HashSet<>();
    // HashMap квартир под аренду
    private final Map<Long, RentFlat> rentFlatsCache = new HashMap<>();
    // Set добавленных квартир под аренду
    private final Set<com.example.demo.common_part.model.RentFlat> newRentFlatsSet = new HashSet<>();

    public void newUser(User user) {
        newUsersSet.add(user);
        usersCache.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void newRentFlat(com.example.demo.common_part.model.RentFlat rentFlat) {
        newRentFlatsSet.add(rentFlat);
    }

    public void saveUser(User user) {
        usersCache.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void saveUser(UserCache userCache) {
        usersCache.put(userCache.getChatId(), userCache);
    }

    public Optional<UserCache> findUserByChatId(Long chatId) {
        return Optional.ofNullable(this.usersCache.getOrDefault(chatId, null));
    }

    public UserCache getUserCache(User user) {
        return usersCache.get(user.getChatId());
    }

    public UserCache getNewCacheFromUser (User user, boolean saved) {
        return UserCache.builder()
                .chatId(user.getChatId())
                .botUserState(user.getBotUserState())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .userChoice(user.getUserChoice())
                .phone(user.getPhone())
                .lastAction(user.getLastAction())
                .lastMessage(System.currentTimeMillis())
                .saved(saved)
                .build();
    }

    public RentFlat getNewCacheFromRentFlat(RentFlat rentFlat) {
        return RentFlat.builder()
                .id(rentFlat.getId())
                .address(rentFlat.getAddress()).floor(rentFlat.getFloor())
                .allFloors(rentFlat.getAllFloors()).contact(rentFlat.getContact())
                .district(rentFlat.getDistrict()).rentalRange(rentFlat.getRange())
                .info(rentFlat.getInfo()).mapLink(rentFlat.getMapLink())
                .metro(rentFlat.getMetro()).money(rentFlat.getMoney()).rooms(rentFlat.getRooms())
                .square(rentFlat.getSquare()).telegraph(rentFlat.getTelegraph())
                //.userChoices(rentFlat.getUserChoices())
                .build();
    }

    public Map<Long, UserCache> getUsersCacheMap() {
        return usersCache;
    }

    public Map<Long, RentFlat> getRentFlatsCacheMap() {
        return rentFlatsCache;
    }

    public Set<User> getNewUsersSet() {
        return newUsersSet;
    }
}
