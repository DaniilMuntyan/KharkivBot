package com.example.demo.user_bot.cache;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.Flat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class DataCache {
    private static final Logger LOGGER = Logger.getLogger(DataCache.class);

    // Необходимые для кэша параметры
    // Ключ - chatId, значение - объект UserCache
    private Map<Long, UserCache> usersCacheMap;
    // Set добавленных пользователей
    //private Set<User> newUsersSet;
    // HashMap квартир под аренду
    private Map<Long, RentFlat> rentFlatsCacheMap;
    // HashMap квартир на продажу
    private Map<Long, BuyFlat> buyFlatsCacheMap;
    // HashMap квартир под аренду, которые еще не были отправлены пользователю, для которого они подходят
    private Map<Long, ArrayList<RentFlat>> notSentRentFlatsMap;
    // HashMap квартир под продажу, которые еще не были отправлены пользователю, для которого они подходят
    private Map<Long, ArrayList<BuyFlat>> notSentBuyFlatsMap;

    public DataCache() {
        LOGGER.info("DataCache is creating...");
        this.usersCacheMap = new ConcurrentHashMap<>(); // Загрузка из базы будет в InitCache
        this.rentFlatsCacheMap = new ConcurrentHashMap<>(); // Загрузка из базы будет в InitCache
        this.buyFlatsCacheMap = new ConcurrentHashMap<>(); // Загрузка из базы будет в InitCache
        //this.newUsersSet = new HashSet<>(); // Новых незарегистрированных пользователей пока нет
        this.notSentRentFlatsMap = new ConcurrentHashMap<>(); // HashMap с неотправленными квартирами пока пуст
        this.notSentBuyFlatsMap = new ConcurrentHashMap<>(); // HashMap с неотправленными квартирами пока пуст
    }

    // region <User caching>
    public void newUser(User user) { // Добавление юзера с последующим сохранением в базе
        //newUsersSet.add(user);
        usersCacheMap.put(user.getChatId(), new UserCache(user, false));
    }

    public void addUser(User user) { // Добавление юзера без дополнительного сохранения в базе
        usersCacheMap.put(user.getChatId(), new UserCache(user, false));
    }

    public void removeUser(Long chatId) {
        LOGGER.info("REMOVING USER CACHE: " + chatId);
        this.usersCacheMap.remove(chatId);
        this.notSentBuyFlatsMap.remove(chatId);
        this.notSentRentFlatsMap.remove(chatId);
    }

    public void refreshUserName(Long chatId, String userName, String firstName, String lastName) {
        //LOGGER.info(chatId + " " + userName + " " + firstName + " " + lastName);
        this.usersCacheMap.get(chatId).setUsername(userName);
        this.usersCacheMap.get(chatId).setFirstName(firstName);
        this.usersCacheMap.get(chatId).setLastName(lastName);
        this.markNotSaved(chatId); // Записываю на сохранение в базу
    }

    public List<UserCache> findAllAdmins() {
        List<UserCache> answer = new ArrayList<>();
        for (Map.Entry<Long, UserCache> entry : this.usersCacheMap.entrySet()) {
            if (entry.getValue().isAdmin()) {
                answer.add(entry.getValue());
            }
        }
        return answer;
    }

    public void setMenuMsgId(String chatIdString, Integer menuMessageId) {
        try {
            Long chatId = Long.valueOf(chatIdString);
            this.usersCacheMap.get(chatId).getUserChoice().setMenuMessageId(menuMessageId);
            this.markNotSaved(chatId);
        } catch (NumberFormatException ex) {
            LOGGER.error(ex);
            ex.printStackTrace();
        }
    }

    public void saveUserCache(User user) {
        usersCacheMap.put(user.getChatId(), new UserCache(user, false));
    }

    public void saveUserCache(UserCache userCache) {
        usersCacheMap.put(userCache.getChatId(), userCache);
        this.markNotSaved(userCache.getChatId());
    }

    private void markNotSaved(Long chatId) { // Чтобы сразу сохранили в БД по расписанию
        this.usersCacheMap.get(chatId).setSaved(false);
    }

    public Optional<UserCache> findUserByChatId(Long chatId) {
        return Optional.ofNullable(this.usersCacheMap.getOrDefault(chatId, null));
    }

    public void setUserChoiceRentFlats(Set<RentFlat> userChoiceFlats, UserCache user) {
        user.getUserChoice().setUserChoicesRent(userChoiceFlats);
    }

    public void setUserChoiceBuyFlats(Set<BuyFlat> userChoiceFlats, UserCache user) {
        user.getUserChoice().setUserChoicesBuy(userChoiceFlats);
    }

    public void setUsersCacheMap(Map<Long, UserCache> usersCacheMap) {
        this.usersCacheMap = usersCacheMap;
    }

    public void updateUser(User user) {
        UserCache userCache = this.usersCacheMap.get(user.getChatId());
        user.setBotUserState(userCache.getBotUserState());
        user.setBotAdminState(userCache.getBotAdminState());
        user.setFirstName(userCache.getFirstName());
        user.setLastName(userCache.getLastName());
        user.setUsername(userCache.getUsername());
        user.setUserChoice(userCache.getUserChoice());
        user.setAdminChoice(userCache.getAdminChoice());
        user.setAdminMode(userCache.isAdmin());
        user.setPhone(userCache.getPhone());
        user.setLastAction(userCache.getLastAction());
        user.setWantsUpdates(userCache.getIsWantsUpdates());
    }

    public UserCache getUserCache(User user) {
        return usersCacheMap.get(user.getChatId());
    }

    public Map<Long, UserCache> getUsersCacheMap() {
        return usersCacheMap;
    }

    /*public Set<User> getNewUsersSet() {
        return newUsersSet;
    }*/

    // endregion

    // region <Flats caching>
    public void removeRentFlat(Long flatId) {
        RentFlat rentFlat = this.rentFlatsCacheMap.get(flatId);
        this.rentFlatsCacheMap.remove(flatId);
        this.notSentRentFlatsMap.remove(flatId);
    }
    public void removeBuyFlat(Long flatId) {
        BuyFlat buyFlat = this.buyFlatsCacheMap.get(flatId);
        this.buyFlatsCacheMap.remove(flatId);
        this.notSentBuyFlatsMap.remove(flatId);
    }

    public void newFlat(Flat flat) {
        //newBuyFlatsSet.add(buyFlat);
        if (flat instanceof RentFlat) {
            rentFlatsCacheMap.put(flat.getId(), (RentFlat) flat);
        }
        if (flat instanceof BuyFlat) {
            buyFlatsCacheMap.put(flat.getId(), (BuyFlat) flat);
        }
    }

    public void setRentFlatsCacheMap(Map<Long, RentFlat> rentFlatsCacheMap) {
        this.rentFlatsCacheMap = rentFlatsCacheMap;
    }
    public void setBuyFlatsCacheMap(Map<Long, BuyFlat> buyFlatsCacheMap) {
        this.buyFlatsCacheMap = buyFlatsCacheMap;
    }

    public void setNotSentRentFlats(List<RentFlat> notSentRentFlats, UserCache user) {
        this.notSentRentFlatsMap.put(user.getChatId(), (ArrayList<RentFlat>) notSentRentFlats);
    }
    public void setNotSentBuyFlats(List<BuyFlat> notSentBuyFlats, UserCache user) {
        this.notSentBuyFlatsMap.put(user.getChatId(), (ArrayList<BuyFlat>) notSentBuyFlats);
    }

    public void removeNotSentFlat(UserCache user, RentFlat rentFlat) {
        ArrayList<RentFlat> userRentList = this.notSentRentFlatsMap.get(user.getChatId());
        if (userRentList == null) {
            userRentList = new ArrayList<>();
        }
        userRentList.remove(rentFlat);
        this.notSentRentFlatsMap.put(user.getChatId(), userRentList);
    }
    public void removeNotSentFlat(UserCache user, BuyFlat buyFlat) {
        ArrayList<BuyFlat> userBuyList = this.notSentBuyFlatsMap.get(user.getChatId());
        if (userBuyList == null) {
            userBuyList = new ArrayList<>();
        }
        userBuyList.remove(buyFlat);
        this.notSentBuyFlatsMap.put(user.getChatId(), userBuyList);
    }

    public Map<Long, RentFlat> getRentFlatsCacheMap() {
        return rentFlatsCacheMap;
    }

    public Map<Long, BuyFlat> getBuyFlatsCacheMap() {
        return buyFlatsCacheMap;
    }

    public Map<Long, ArrayList<RentFlat>> getNotSentRentFlatsMap() {
        return notSentRentFlatsMap;
    }

    public Map<Long, ArrayList<BuyFlat>> getNotSentBuyFlatsMap() {
        return notSentBuyFlatsMap;
    }

    // endregion
}
