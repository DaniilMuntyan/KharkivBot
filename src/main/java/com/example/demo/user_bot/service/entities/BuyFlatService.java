package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.repo.BuyFLatRepository;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.common_part.utils.money_range.BuyRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class BuyFlatService {
    private final BuyFLatRepository buyFLatRepository;

    @Autowired
    public BuyFlatService(BuyFLatRepository buyFLatRepository) {
        this.buyFLatRepository = buyFLatRepository;

        /*for (int i = 0; i < 10000; ++i) {
            BuyFlat buyFlat = BuyFlat.builder()
                    .address("Алексеевка, пр.Победы 68в")
                    .contact("https://t.me/daniil_muntyan")
                    .floor((short) (i % 12))
                    .allFloors((short) 10)
                    .district(District.HTZ_ROGAN)
                    .info("Цена снижена!")
                    .buyRange(BuyRange.USD_20000_25000)
                    .metro("м. Победа")
                    .money("60000 $")
                    .telegraph("https://telegra.ph/60421-Prodayotsya-3-komnatnaya-kvartira-Alekseevka-10-26")
                    .rooms(Rooms.GOSTINKA)
                    .mapLink("https://www.google.com/maps/search/?api=1&query=%D0%BF%D1%80.%D0%9F%D0%BE%D0%B1%D0%B5%D0%B4%D1%8B+68%D0%B2%2C+%D0%A5%D0%B0%D1%80%D1%8C%D0%BA%D0%BE%D0%B2")
                    .square(68f)
                    .build();
            buyFLatRepository.save(buyFlat);
        }*/
    }

    public void deleteBuyFlat(Long id) {
        this.buyFLatRepository.deleteById(id);
    }

    public Optional<BuyFlat> findById(Long id) {
        return this.buyFLatRepository.findById(id);
    }

    public List<BuyFlat> findAllBuyFlats() {
        return this.buyFLatRepository.findAll();
    }

    public BuyFlat save(BuyFlat buyFlat) {
        return this.buyFLatRepository.save(buyFlat);
    }
}
