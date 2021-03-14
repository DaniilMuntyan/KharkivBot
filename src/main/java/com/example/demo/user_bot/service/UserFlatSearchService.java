package com.example.demo.user_bot.service;

import com.example.demo.user_bot.cache.UserCache;
import org.springframework.stereotype.Service;

@Service
public final class UserFlatSearchService {
    public void search(UserCache user) {
        if (user.getUserChoice().getIsRentFlat()) {
            // TODO: искать квартиры в зависимости от предпочтений юзера

        } else {

        }
    }
}
