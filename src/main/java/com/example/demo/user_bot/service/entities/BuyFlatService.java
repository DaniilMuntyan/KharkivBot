package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.BuyFLatRepository;
import com.example.demo.common_part.repo.RentFlatRepository;
import com.example.demo.common_part.utils.BuyRange;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Rooms;
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

        /*for (int i = 0; i < 48; ++i) {
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
