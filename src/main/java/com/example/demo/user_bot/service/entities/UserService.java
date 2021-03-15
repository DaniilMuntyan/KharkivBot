package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.schedule.UserBotSendingQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private final DataCache dataCache;
    private final UserBotSendingQueue userBotSendingQueue;

    private final UserRepository userRepository;

    @Autowired
    public UserService(DataCache dataCache, UserBotSendingQueue userBotSendingQueue, UserRepository userRepository) {
        this.dataCache = dataCache;
        this.userBotSendingQueue = userBotSendingQueue;
        this.userRepository = userRepository;

        // Добавляю всех юзеров из базы данных в HashMap
        this.initUsersCache();
    }

    private void initUsersCache() {
        List<User> allExistedUsers = this.findAllUsers();
        Map<Long, UserCache> usersCache = this.dataCache.getUsersCacheMap();
        for (User temp: allExistedUsers) {
            // saved ставлю true, потому что в базе он уже есть
            usersCache.put(temp.getChatId(), this.dataCache.getNewCacheFromUser(temp, true));
        }
        LOGGER.info("UsersCache has " + usersCache.size() + " users");
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void saveUserCache(UserCache userCache) {
        dataCache.saveUser(userCache);
    }

    public UserCache saveNewUser(User user) {
        dataCache.newUser(this.userRepository.save(user));
        return dataCache.getUserCache(user);
    }

    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    // Отдаем кэш по айди чата
    public Optional<UserCache> findUserInCache(Long chatId) {
        return dataCache.findUserByChatId(chatId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
