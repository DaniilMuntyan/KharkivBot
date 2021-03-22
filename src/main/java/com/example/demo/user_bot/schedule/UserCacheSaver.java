package com.example.demo.user_bot.schedule;


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
import java.util.concurrent.ConcurrentHashMap;

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
        ConcurrentHashMap<Long, UserCache> usersCacheMap = (ConcurrentHashMap<Long, UserCache>) dataCache.getUsersCacheMap();

        // TODO: this.printMemory(); // Печатаю объем занятой мной памяти
        //System.out.println(usersCacheMap);

        int c = 0;
        long time1 = System.currentTimeMillis();
        // Сначала сохраняю всех новых пользователей
        for (User temp: newUsers) {
            userService.saveUser(temp);
            newUsers.remove(temp); // Удалил с множества новых пользователей, потому что он уже в базе
            c++;
        }
        //LOGGER.info("TIME save all new users (" + c + "): " + (System.currentTimeMillis() - time1));
        c = 0;
        time1 = System.currentTimeMillis();
        // Теперь изменяю данные всех юзеров из кэша, которые еще не были сохранены в базу
        for (Map.Entry<Long, UserCache> entry : usersCacheMap.entrySet()) {
            Long chatId = entry.getKey();
            UserCache userCache = entry.getValue();
            if (!userCache.getSaved()) { // Еще не был сохранен
                Optional<User> user = userService.findByChatId(chatId);
                if (user.isPresent()) { // Если есть в базе - обновляем
                    this.dataCache.updateUser(user.get());
                    userChoiceService.saveUserChoice(userCache.getUserChoice());
                    userService.saveUser(user.get());
                    userCache.setSaved(true); // Пометили как сохраненный кэш
                    c++;
                } else { // Нет в базе, но есть в кэше => сохраняю в базу
                    userService.saveUser(userCache);
                    //LOGGER.info("");
                }
            }
            try {
                /*LOGGER.info("ID " + userCache.getChatId() + ". ChoiceRentSize: " +
                        userCache.getUserChoice().getUserChoicesRent().size() + ". ChoiceBuySize: " +
                        userCache.getUserChoice().getUserChoicesBuy().size());*/
            } catch (Exception ignored) {}
        }
        //LOGGER.info("TIME update all changed users (" + c + "): " + (System.currentTimeMillis() - time1));
        //LOGGER.info("ALL RENT FLATS CACHE SIZE: " + this.dataCache.getRentFlatsCacheMap().size());
        //LOGGER.info("ALL BUY FLATS CACHE SIZE: " + this.dataCache.getBuyFlatsCacheMap().size());
    }

    private void printMemory() {
        final long megabyte = 1024L * 1024L;
        // Считаю память несколько раз - чтобы исключить случайность
        long[] memory = {Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
                Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
                Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()};
        StringBuilder mem = new StringBuilder();
        for (long l : memory) {
            mem.append(l).append(" B (").append(l / megabyte).append(" MB);  ");
        }
        long totalMemory = Runtime.getRuntime().totalMemory(), freeMemory = Runtime.getRuntime().freeMemory(),
                maxMemory = Runtime.getRuntime().maxMemory();
        LOGGER.info("TOTAL MEMORY: " + totalMemory + " B (" + (totalMemory / megabyte) + " MB); " +
                "FREE MEMORY: " + freeMemory + " B (" + (freeMemory / megabyte) + " MB); " +
                "MAX MEMORY: " + maxMemory + " B (" + (maxMemory / megabyte) + " MB);");
        LOGGER.info("USED MEMORY: " + mem.toString());
    }
}
