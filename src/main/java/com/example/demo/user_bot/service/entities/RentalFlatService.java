package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.RentFlatRepository;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.common_part.utils.money_range.RentalRange;
import com.example.demo.user_bot.cache.DataCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class RentalFlatService {
    private static final Logger LOGGER = Logger.getLogger(RentalFlatService.class);
    private final RentFlatRepository rentFlatRepository;
    private final DataCache dataCache;

    @Autowired
    public RentalFlatService(RentFlatRepository rentFlatRepository, DataCache dataCache) {
        this.rentFlatRepository = rentFlatRepository;
        this.dataCache = dataCache;

        /*for (int i = 0; i < 10000; ++i) {
            RentFlat rentFlat = RentFlat.builder()
                    .address("Проспект Гагарина, ул. Москалёвская 5")
                    .contact("https://t.me/daniil_muntyan")
                    .floor((short) i)
                    .allFloors((short) (i + 5))
                    .district(District.KLOCHKOVSKOY)
                    .info("Новострой! Без собак!")
                    .rentalRange(RentalRange.UAH_10000_12000)
                    .metro("м. Холодная гора")
                    .money("4600 грн + интернет")
                    .telegraph("https://telegra.ph/83544-Sdayotsya-1-komnatnaya-kvartira-Saltovka-03-13")
                    .rooms(Rooms.THREE)
                    .mapLink("https://www.google.com/maps/search/?api=1&query=%D1%83%D0%BB.+%D0%9C%D0%B8%D1%80%D0%BE%D0%BD%D0%BE%D1%81%D0%B8%D1%86%D0%BA%D0%B0%D1%8F%2C+88%2C+%D0%A5%D0%B0%D1%80%D1%8C%D0%BA%D0%BE%D0%B2")
                    .square(45.5f)
                    .build();
            rentFlatRepository.save(rentFlat);
        }*/
    }

    public void deleteRentFlat(Long flatId) {
        this.rentFlatRepository.deleteById(flatId);
    }

    public Optional<RentFlat> findById(Long id) {
        return this.rentFlatRepository.findById(id);
    }

    public List<RentFlat> findAllRentFlats() {
        return this.rentFlatRepository.findAll();
    }

    public RentFlat save(RentFlat rentFlat) {
        return this.rentFlatRepository.save(rentFlat);
    }
}
