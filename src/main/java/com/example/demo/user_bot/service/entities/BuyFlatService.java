package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.repo.BuyFLatRepository;
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

        /*for (int i = 0; i < 5000; ++i) {
            BuyFlat buyFlat = BuyFlat.builder()
                    .address("Проспект Гагарина, ул. Москалёвская 5")
                    .contact("https://t.me/daniil_muntyan")
                    .floor((short) i)
                    .allFloors((short) 16)
                    .district(District.KLOCHKOVSKOY)
                    .info("Новострой!")
                    .buyRange(BuyRange.USD_45000_50000)
                    .metro("м. Холодная гора")
                    .money("4600 грн + интернет")
                    .telegraph("https://telegra.ph/83544-Sdayotsya-1-komnatnaya-kvartira-Saltovka-03-13")
                    .rooms(Rooms.TWO)
                    .mapLink("https://www.google.com/maps/search/?api=1&query=%D0%96%D0%9A+%D0%90%D1%82%D0%BB%D0%B0%D0%BD%D1%82%2C+%D1%83%D0%BB.+%D0%93%D1%80%D0%B8%D0%B3%D0%BE%D1%80%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B5+%D1%88%D0%BE%D1%81%D1%81%D0%B5+55%2C+%D0%A5%D0%B0%D1%80%D1%8C%D0%BA%D0%BE%D0%B2")
                    .square(45.5f)
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
