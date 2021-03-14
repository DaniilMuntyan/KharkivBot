package com.example.demo.user_bot.service.handler.callback.init;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.user_bot.cache.DataCache;
import com.example.demo.user_bot.cache.UserCache;
import com.example.demo.user_bot.model.UserChoice;
import com.fasterxml.jackson.databind.util.TypeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public final class InitMenuEndHandler {
    private final DataCache dataCache;

    @Autowired
    public InitMenuEndHandler(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public void handleInitMenuEnd(List<BotApiMethod<?>> response, UserCache user) {
        UserChoice userChoice = user.getUserChoice();
        Map<Long, RentFlat> rentFlatsCacheMap = dataCache.getRentFlatsCacheMap(); // Достаю все квартиры под аренду из кэша
        // Прохожусь по всем квартирам и сравниваю их параметры с предпочтениями пользователя
        for (Long name: rentFlatsCacheMap.keySet()) {
            String key = name.toString();
            RentFlat value = rentFlatsCacheMap.get(name);
            System.out.println(key + " " + value.getId());
        }
        for (Map.Entry<Long, RentFlat> entry : rentFlatsCacheMap.entrySet()) {
            RentFlat rentFlat = entry.getValue();
            Set<RentFlat> userChoiceFlats = userChoice.getUserChoicesRent();
            if (userChoiceFlats == null) { // Если этот выбор пользователя еще не имеет своих вариантов квартир
                userChoiceFlats = new HashSet<>();
            }
            if (this.checkFlatWithUserChoice(rentFlat, userChoice)) { // Если параметры совпали
                userChoiceFlats.add(rentFlat); // Добавляю квартиры к выбору пользователя
                // Если пользователь раньше был сохранен в базе - теперь нет,
                // потому что изменилось поле UserChoice и его нужно сохранить
                if (user.getSaved()) {
                    user.setSaved(false);
                }
                // Отправляю подходящие варианты пользователю
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableHtml(true);
                sendMessage.setText(rentFlat.getHtmlMessage());
                sendMessage.setChatId(user.getChatId().toString());
                response.add(sendMessage);
            }
        }
    }

    private boolean checkFlatWithUserChoice(RentFlat flat, UserChoice userChoice) {
        return userChoice.getBudget().contains(flat.getRange().getIdentifier()) &&
                userChoice.getRooms().contains(flat.getRooms().getIdentifier()) &&
                userChoice.getDistricts().contains(flat.getDistrict().getIdentifier());
    }
}
