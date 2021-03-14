package com.example.demo.user_bot.schedule;


import com.example.demo.common_part.model.User;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
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

    @Autowired
    public UserCacheSaver(DataCache dataCache, UserService userService) {
        this.dataCache = dataCache;
        this.userService = userService;
    }

    @Scheduled(fixedDelayString = "${delay.user.saveToDb}")
    private void saveCache() {
        HashSet<User> newUsers = (HashSet<User>) dataCache.getNewUsersSet();
        HashMap<Long, UserCache> cache = (HashMap<Long, UserCache>) dataCache.getUsersCacheMap();

        System.out.println(cache);

        long time1 = System.currentTimeMillis();
        int c = 0;
        // Сначала сохраняю всех новых пользователей
        for (User temp: newUsers) {
            userService.saveUser(temp);
            newUsers.remove(temp); // Удалил с множества новых пользователей, потому он уже в базе
            c++;
        }
        LOGGER.info("TIME save all new users (" + c + "): " + (System.currentTimeMillis() - time1));

        time1 = System.currentTimeMillis();
        c = 0;
        // Теперь изменяю данные всех юзеров из кэша, которые еще не были сохранены в базу
        for (Map.Entry<Long, UserCache> entry : cache.entrySet()) {
            Long chatId = entry.getKey();
            UserCache userCache = entry.getValue();
            if (!userCache.getSaved()) { // Еще не были сохранены
                Optional<User> user = userService.findByChatId(chatId);
                if (user.isPresent()) {
                    this.changeUser(user.get(), userCache);
                    userService.saveUser(user.get());
                    userCache.setSaved(true); // Пометили как сохраненный кэш
                    c++;
                }
            }
        }
        LOGGER.info("TIME update all changed users (" + c + "): " + (System.currentTimeMillis() - time1));
    }

    private void changeUser(User user, UserCache userCache) {
        user.setFirstName(userCache.getFirstName());
        user.setLastName(userCache.getLastName());
        user.setUsername(userCache.getUsername());
        user.setUserChoice(userCache.getUserChoice());
        user.setBotUserState(userCache.getBotUserState());
        user.setPhone(userCache.getPhone());
        user.setLastAction(userCache.getLastAction());
    }
}
