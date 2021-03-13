package com.example.demo.user_bot.cache;

import com.example.demo.common_part.model.User;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class DataCache {
    private static final Logger LOGGER = Logger.getLogger(DataCache.class);
    @Getter
    // Необходимые для кэша параметры
    // Ключ - chatId, значение - объект UserCache
    private final Map<Long, UserCache> usersCache = new HashMap<>();
    @Getter
    private final Set<User> newUsersSet = new HashSet<>(); // Лист добавленных пользователей

    public void newUser(User user) {
        newUsersSet.add(user);
        usersCache.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void saveUser(User user) {
        usersCache.put(user.getChatId(), this.getNewCacheFromUser(user, false));
    }

    public void saveUser(UserCache userCache) {
        usersCache.put(userCache.getChatId(), userCache);
    }

    public Optional<UserCache> findUserByChatId(Long chatId) {
        return Optional.ofNullable(this.usersCache.getOrDefault(chatId, null));
    }

    public UserCache getUserCache(User user) {
        return usersCache.get(user.getChatId());
    }

    public UserCache getNewCacheFromUser(User user, boolean saved) {
        return UserCache.builder()
                .chatId(user.getChatId())
                .botUserState(user.getBotUserState())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .userChoice(user.getUserChoice())
                .phone(user.getPhone())
                .lastAction(user.getLastAction())
                .saved(saved)
                .build();
    }
}
