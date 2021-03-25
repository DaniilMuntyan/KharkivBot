package com.example.demo.user_bot.schedule;


import com.example.demo.common_part.constants.ProgramVariables;
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

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@PropertySource("classpath:program.properties")
public final class UserCacheSaver {
    private static final Logger LOGGER = Logger.getLogger(UserCacheSaver.class);

    private final DataCache dataCache;
    private final UserService userService;
    private final ProgramVariables programVariables;
    private final UserChoiceService userChoiceService;

    @Autowired
    public UserCacheSaver(DataCache dataCache, UserService userService, ProgramVariables programVariables, UserChoiceService userChoiceService) {
        this.dataCache = dataCache;
        this.userService = userService;
        this.programVariables = programVariables;
        this.userChoiceService = userChoiceService;
    }

    private String f() {
        String s = "";
        for (int i = 0; i < 300; ++i) {
            s += "-";
        }
        return s;
    }

    @PreDestroy
    @Scheduled(fixedDelayString = "${delay.user.saveToDb}")
    private void saveCache() {
        //HashSet<User> newUsers = (HashSet<User>) dataCache.getNewUsersSet();
        ConcurrentHashMap<Long, UserCache> usersCacheMap = (ConcurrentHashMap<Long, UserCache>) dataCache.getUsersCacheMap();

        //System.out.println("\n" + f());
        //this.printMemory(); // Печатаю объем занятой мной памяти
        //System.out.println(f() + "\n");

        int c = 0;
        long time1 = System.currentTimeMillis();
        // Сначала сохраняю всех новых пользователей
        /*for (User temp: newUsers) {
            userService.saveUser(temp);
            newUsers.remove(temp); // Удалил с множества новых пользователей, потому что он уже в базе
            c++;
        }*/
        //LOGGER.info("TIME save all new users (" + c + "): " + (System.currentTimeMillis() - time1));
        c = 0;
        time1 = System.currentTimeMillis();
        Date now = new Date();
        List<Long> removeFromCache = new ArrayList<>();
        // Теперь изменяю данные всех юзеров из кэша, которые еще не были сохранены в базу
        for (Map.Entry<Long, UserCache> entry : usersCacheMap.entrySet()) {
            Long chatId = entry.getKey();
            UserCache userCache = entry.getValue();
            if (!userCache.getSaved()) { // Еще не был сохранен
                Optional<User> user = userService.findByChatId(chatId);
                if (user.isPresent()) { // Если есть в базе - обновляем
                    this.dataCache.updateUser(user.get());
                    userService.saveUser(user.get());
                    userCache.setSaved(true); // Пометили как сохраненный кэш
                    c++;
                } else { // Нет в базе, но есть в кэше => сохраняю в базу
                    userService.saveUser(userCache);
                    //LOGGER.info("");
                }
            } else { // Если пользователь уже сохранен в базе
                LOGGER.info("Difference: " + (now.getTime() - userCache.getLastAction().getTime()) / 1000);
                boolean toRemove = (now.getTime() - userCache.getLastAction().getTime()) / 1000 > this.programVariables.getTimeInCache();
                // Если пользователь не является админом и давно ничего не делал - убираю из кэша
                if (!userCache.isAdmin() && toRemove)
                {
                    removeFromCache.add(chatId);
                }
            }
        }
        for (Long temp: removeFromCache) {
            this.dataCache.removeUser(temp); // Удаляю пользователя с кэша
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
