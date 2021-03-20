package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private final DataCache dataCache;

    private final UserRepository userRepository;

    @Autowired
    public UserService(DataCache dataCache, UserRepository userRepository) {
        this.dataCache = dataCache;
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveUserCache(UserCache userCache) {
        dataCache.saveUserCache(userCache);
    }

    public UserCache saveNewUser(User user) {
        dataCache.newUser(this.userRepository.save(user));
        return dataCache.getUserCache(user);
    }

    public boolean checkFlatWithUserChoice(RentFlat flat, UserChoice userChoice) {
        return userChoice.getBudget().contains(flat.getRange().getIdentifier()) &&
                userChoice.getRooms().contains(flat.getRooms().getIdentifier()) &&
                userChoice.getDistricts().contains(flat.getDistrict().getIdentifier());
    }
    public boolean checkFlatWithUserChoice(BuyFlat flat, UserChoice userChoice) {
        return userChoice.getBudget().contains(flat.getRange().getIdentifier()) &&
                userChoice.getRooms().contains(flat.getRooms().getIdentifier()) &&
                userChoice.getDistricts().contains(flat.getDistrict().getIdentifier());
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
