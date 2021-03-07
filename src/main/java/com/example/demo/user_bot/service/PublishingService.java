package com.example.demo.user_bot.service;

import com.example.demo.common_part.constants.ProgramVariables;
import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.user_bot.keyboards.PublishFlatKeyboard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
public final class PublishingService {
    private static final Logger LOGGER = Logger.getLogger(PublishingService.class);
    private final UserRepository userRepository;
    private final QueryService queryService;
    private final RentalFlatService rentalFlatService;
    private final BuyFlatService buyFlatService;
    private final ProgramVariables programVariables;

    @Autowired
    public PublishingService(UserRepository userRepository, QueryService queryService, RentalFlatService rentalFlatService, BuyFlatService buyFlatService, ProgramVariables programVariables) {
        this.userRepository = userRepository;
        this.queryService = queryService;
        this.rentalFlatService = rentalFlatService;
        this.buyFlatService = buyFlatService;
        this.programVariables = programVariables;
    }

    public String publish(User admin, List<BotApiMethod<?>> response) {
        List<User> allUsers = userRepository.findAll();
        String flatNumber;

        PublishFlatKeyboard publishFlatKeyboard = new PublishFlatKeyboard(admin.getAdminChoice());

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(publishFlatKeyboard.getKeyboard());
        if (admin.getAdminChoice().getIsRentFlat()) {
            RentFlat rentFlat = rentalFlatService.save(new RentFlat(admin.getAdminChoice()));
            sendMessage.setText(rentFlat.getHtmlMessage());
            flatNumber = rentFlat.getId().toString();
        } else {
            BuyFlat buyFlat = buyFlatService.save(new BuyFlat(admin.getAdminChoice()));
            sendMessage.setText(buyFlat.getHtmlMessage());
            flatNumber = buyFlat.getId().toString();
        }

        for (User user: allUsers) {
            // TODO: отправляем только тем пользователям, у которых соответствующие предпочтения
            sendMessage.setChatId(user.getChatId().toString());
            queryService.execute(sendMessage);
        }

        // Публикую в канал
        sendMessage.setChatId(programVariables.getTelegramChannel());
        response.add(sendMessage);

        return flatNumber;
    }
}
