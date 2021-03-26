package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.service.publishing.FindFlatsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    public Optional<UserCache> findUserInCacheOrDb(Long chatId) {
        long time1 = System.currentTimeMillis();
        Optional<UserCache> userCache = dataCache.findUserByChatId(chatId);
        if (userCache.isEmpty()) { // Если в кэше нет - ищу в базе
            Optional<User> user = userRepository.findByChatId(chatId);
            if (user.isPresent()) { // Если существует в базе - копирую в кэш
                UserCache userFromDb = new UserCache(user.get(), true);
                if (userFromDb.getUserChoice() != null && userFromDb.getUserChoice().getIsRentFlat() != null) { // Если выбор уже создан
                    this.findFlatsForUser(userFromDb, userFromDb.getUserChoice().getIsRentFlat()); // Заполняю сэты подходящих квартир
                }
                dataCache.saveUserCache(userFromDb); // Сохраняю в кэше
                LOGGER.info("GET FROM DB: " + userFromDb.getChatId() + ". TIME: " + (System.currentTimeMillis() - time1));
                return Optional.of(userFromDb);
            }
        } else {
            LOGGER.info("GET FROM CACHE: " + userCache.get().getChatId() + ". TIME: " + (System.currentTimeMillis() - time1));
        }
        return userCache;
    }

    private void findFlatsForUser(UserCache user, boolean isRentFlat) {
        FindFlatsService findFlatsService = BeanUtil.getBean(FindFlatsService.class);
        if (isRentFlat) { // Если для аренды
            List<RentFlat> notSentRentFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<RentFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            findFlatsService.findRentFlatsForUser(user, notSentRentFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentRentFlats(notSentRentFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceRentFlats(userChoiceFlats, user);
        } else {
            List<BuyFlat> notSentBuyFlats = new ArrayList<>(); // Список неотправленных юзеру квартир
            Set<BuyFlat> userChoiceFlats = new HashSet<>(); // Сэт квартир под выбор пользователя

            findFlatsService.findBuyFlatsForUser(user, notSentBuyFlats, userChoiceFlats); // Заполняю списки

            // Устанавливаю список неотправленных квартир юзеру
            this.dataCache.setNotSentBuyFlats(notSentBuyFlats, user);
            // Устанавливаю только что заполненный, новый сэт квартир под выбор пользователя, так как теперь он полностью другой
            this.dataCache.setUserChoiceBuyFlats(userChoiceFlats, user);
        }

        this.dataCache.saveUserCache(user); // Сохраняю изменения юзера
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
