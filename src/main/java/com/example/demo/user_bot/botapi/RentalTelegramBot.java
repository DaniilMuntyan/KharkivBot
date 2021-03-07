package com.example.demo.user_bot.botapi;

import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.user_bot.utils.Rooms;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.RentFlatRepository;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@PropertySource("classpath:application.properties")
@RestController
public class RentalTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = Logger.getLogger(RentalTelegramBot.class);

    private final RentFlatRepository rentFlatRepository;

    @Value("${bot1.name}")
    private String botName;

    @Value("${bot1.token}")
    private String botToken;

    @Autowired
    public RentalTelegramBot(RentFlatRepository rentFlatRepository) {
        this.rentFlatRepository = rentFlatRepository;
        LOGGER.info("RentalTelegramBot is creating...");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        RentFlat rentFlat = RentFlat.builder()
                .square(25.0F)
                .rooms(Rooms.ONE)
                .address("Защитников Украины, ЖК Металлист, ул. Плехановская 92А")
                .metro("м. Спортивная")
                .district(District.GAGARINA)
                .floor((short) 9)
                .allFloors((short) 9)
                .money("10500 + коммуналка")
                .rentalRange(RentalRange.UAH_10000_12000)
                .telegraph("https://telegra.ph/77084-Sdayotsya-3-komnatnaya-kvartira-Saltovka-02-08")
                .info("Цена снижена!")
                .build();
        RentFlat rentFlat2 = RentFlat.builder()
                .square(58.F)
                .rooms(Rooms.GOSTINKA)
                .address("Защитников Украины, ЖК Металлист, ул. Плехановская 92А")
                .district(District.HOLODNAYA_GORA)
                .floor((short) 1)
                .allFloors((short) 5)
                .money("6800 + интернет, вода, электричество")
                .rentalRange(RentalRange.UAH_4500_6000)
                .telegraph("https://telegra.ph/77084-Sdayotsya-3-komnatnaya-kvartira-Saltovka-02-08")
                .build();
        rentFlatRepository.save(rentFlat);
        rentFlatRepository.save(rentFlat2);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(rentFlat.getMarkdownMessage())
                .build();
        sendMessage.enableMarkdown(true);
        execute(sendMessage);


        SendMessage sendMessage2 = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(rentFlat2.getMarkdownMessage())
                .build();
        sendMessage2.enableMarkdown(true);
        execute(sendMessage2);
    }
}
