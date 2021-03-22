package com.example.demo.user_bot.service.entities;

import com.example.demo.admin_bot.model.AdminChoice;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
    public void saveUser(UserCache user) { // Сохраняю кэш в базу (когда юзера в базе нет, а в памяти есть)
        userRepository.save(User.builder()
                .chatId(user.getChatId())
                .adminMode(false)
                .botUserState(user.getBotUserState())
                .firstName(user.getFirstName()).lastName(user.getLastName()).username(user.getUsername())
                .lastAction(user.getLastAction())
                .phone(user.getPhone())
                .wantsUpdates(user.getIsWantsUpdates())
                .build());
    }

    public SendMessage getMyState(boolean first, UserCache user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        if (first) {
            sendMessage.setText("До обработки: " + user.getBotUserState().toString());
        } else {
            sendMessage.setText("После обработки: " + user.getBotUserState().toString());
        }
        return sendMessage;
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
