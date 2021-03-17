package com.example.demo;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.model.User;
import com.example.demo.common_part.repo.UserRepository;
import com.example.demo.common_part.utils.BeanUtil;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.model.UserChoice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TgBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
        RentFlat rentFlat = RentFlat.builder()
                .address("Проспект Гагарина, ул. Москалёвская 5")
                .contact("@daniil_muntyan")
                .floor((short) ThreadLocalRandom.current().nextInt(1, 12))
                .allFloors((short) 16)
                .district(District.KLOCHKOVSKOY)
                .info("Новострой!")
                .rentalRange(RentalRange.UAH_6000_8000)
                .metro("м. Холодная гора")
                .money("4600 грн + интернет")
                .telegraph("https://telegra.ph/83562-Sdayotsya-1-komnatnaya-kvartira-Harkov-03-13")
                .rooms(Rooms.TWO)
                .square(45.5f)
                .build();

        /*UserRepository userRepository = BeanUtil.getBean(UserRepository.class);
        User user = new User();
        UserChoice userChoice = new UserChoice();
        HashSet<RentFlat> set = new HashSet<>();
        set.add();
        userChoice.setUserChoicesRent(new HashSet<>());*/

    }
}
