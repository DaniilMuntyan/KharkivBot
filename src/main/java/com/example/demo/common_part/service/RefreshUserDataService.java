package com.example.demo.common_part.service;

import com.example.demo.user_bot.cache.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public final class RefreshUserDataService {
    private final DataCache dataCache;

    @Autowired
    public RefreshUserDataService(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public void refreshUserName(Message message) {
        String username = message.getFrom().getUserName();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        this.dataCache.refreshUserName(message.getChatId(), username, firstName, lastName); // Обновляю имена пользователя
    }
}
