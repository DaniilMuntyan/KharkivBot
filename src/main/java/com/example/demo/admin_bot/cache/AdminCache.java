package com.example.demo.admin_bot.cache;

import com.example.demo.user_bot.cache.DataCache;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class AdminCache {
    private static final Logger LOGGER = Logger.getLogger(AdminCache.class);

    private Map<Long, Long> flatToDelete; // Ключ - chatId админа, который удаляет. Значение - айди удаляемой квартиры

    public AdminCache() {
        flatToDelete = new HashMap<>();
    }

    public void addFlatToDelete(Long adminId, Long flatId) {
        this.flatToDelete.put(adminId, flatId);
    }

    public void removeFlatToDelete(Long adminId) {
        this.flatToDelete.remove(adminId);
    }

    public Long getFlatIdToDelete(Long adminId) {
        return this.flatToDelete.get(adminId);
    }

}
