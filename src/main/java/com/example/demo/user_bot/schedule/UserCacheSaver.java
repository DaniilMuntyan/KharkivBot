package com.example.demo.user_bot.schedule;


import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.service.entities.UserChoiceService;
import com.example.demo.user_bot.service.entities.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@PropertySource("classpath:program.properties")
public final class UserCacheSaver {
    private static final Logger LOGGER = Logger.getLogger(UserCacheSaver.class);

    private final DataCache dataCache;
    private final UserService userService;
    private final UserChoiceService userChoiceService;

    @Autowired
    public UserCacheSaver(DataCache dataCache, UserService userService, UserChoiceService userChoiceService) {
        this.dataCache = dataCache;
        this.userService = userService;
        this.userChoiceService = userChoiceService;
    }

    @Scheduled(fixedDelayString = "${delay.user.saveToDb}")
    private void saveCache() {
        HashSet<User> newUsers = (HashSet<User>) dataCache.getNewUsersSet();
        HashMap<Long, UserCache> usersCacheMap = (HashMap<Long, UserCache>) dataCache.getUsersCacheMap();

        System.out.println(usersCacheMap);

        long time1 = System.currentTimeMillis();
        int c = 0;
        // Сначала сохраняю всех новых пользователей
        for (User temp: newUsers) {
            userService.saveUser(temp);
            newUsers.remove(temp); // Удалил с множества новых пользователей, потому что он уже в базе
            c++;
        }
        LOGGER.info("TIME save all new users (" + c + "): " + (System.currentTimeMillis() - time1));

        time1 = System.currentTimeMillis();
        c = 0;
        // Теперь изменяю данные всех юзеров из кэша, которые еще не были сохранены в базу
        for (Map.Entry<Long, UserCache> entry : usersCacheMap.entrySet()) {
            Long chatId = entry.getKey();
            UserCache userCache = entry.getValue();
            if (!userCache.getSaved()) { // Еще не были сохранены
                Optional<User> user = userService.findByChatId(chatId);
                if (user.isPresent()) { // Если есть в базе - обновляем
                    this.dataCache.updateUser(user.get());
                    userChoiceService.saveUserChoice(userCache.getUserChoice());
                    userService.saveUser(user.get());
                    userCache.setSaved(true); // Пометили как сохраненный кэш
                    //LOGGER.info("UserCache saved: " + (user.get().getUserChoice().getUserChoicesRent() != null));
                    c++;
                }
            }
        }
        LOGGER.info("TIME update all changed users (" + c + "): " + (System.currentTimeMillis() - time1));
    }
}
