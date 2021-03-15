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
    private final Map<Long, UserCache> usersCacheMap = new HashMap<>();
    // Set добавленных пользователей
    private final Set<User> newUsersSet = new HashSet<>();
    // HashMap квартир под аренду
    private final Map<Long, RentFlat> rentFlatsCacheMap = new HashMap<>();
    // HashMap квартир под аренду, которые еще не были отправлены пользователю, для которого они подходят
    private final Map<Long, ArrayList<RentFlat>> notSentRentFlatsMap = new HashMap<>();
    // Set добавленных квартир под аренду
    private final Set<RentFlat> newRentFlatsSet = new HashSet<>();

    public void newUser(User user) {
        newUsersSet.add(user);
        usersCacheMap.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void newRentFlat(com.example.demo.common_part.model.RentFlat rentFlat) {
        newRentFlatsSet.add(rentFlat);
    }

    public void saveUser(User user) {
        usersCacheMap.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void saveUser(UserCache userCache) {
        usersCacheMap.put(userCache.getChatId(), userCache);
    }

    public Optional<UserCache> findUserByChatId(Long chatId) {
        return Optional.ofNullable(this.usersCacheMap.getOrDefault(chatId, null));
    }

    public void setNotSentRentFlats(ArrayList<RentFlat> notSentRentFlats, UserCache user) {
        this.notSentRentFlatsMap.put(user.getChatId(), notSentRentFlats);
    }

    public void addNotSentFlat(UserCache user, RentFlat rentFlat) {
        ArrayList<RentFlat> userRentList = this.notSentRentFlatsMap.get(user.getChatId());
        if (userRentList == null) {
            userRentList = new ArrayList<>();
        }
        userRentList.add(rentFlat);
        this.notSentRentFlatsMap.put(user.getChatId(), userRentList);
        LOGGER.info("NEW notSentFlat. size: " + this.notSentRentFlatsMap.get(user.getChatId()));
    }

    public void removeNotSentFlat(UserCache user, RentFlat rentFlat) {
        ArrayList<RentFlat> userRentList = this.notSentRentFlatsMap.get(user.getChatId());
        if (userRentList == null) {
            userRentList = new ArrayList<>();
        }
        userRentList.remove(rentFlat);
        this.notSentRentFlatsMap.put(user.getChatId(), userRentList);
    }

    public UserCache getUserCache(User user) {
        return usersCacheMap.get(user.getChatId());
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

    public void updateUser(User user) {
        UserCache userCache = this.usersCacheMap.get(user.getChatId());
        user.setBotUserState(userCache.getBotUserState());
        user.setFirstName(userCache.getFirstName());
        user.setLastName(userCache.getLastName());
        user.setUsername(userCache.getUsername());
        user.setUserChoice(userCache.getUserChoice());
        user.setPhone(userCache.getPhone());
        user.setLastAction(userCache.getLastAction());
    }

    public Map<Long, UserCache> getUsersCacheMap() {
        return usersCacheMap;
    }

    public Map<Long, RentFlat> getRentFlatsCacheMap() {
        return rentFlatsCacheMap;
    }

    public Map<Long, ArrayList<RentFlat>> getNotSentRentFlatsMap() {
        return notSentRentFlatsMap;
    }

    public Set<User> getNewUsersSet() {
        return newUsersSet;
    }
}
