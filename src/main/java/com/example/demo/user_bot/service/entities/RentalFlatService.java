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
                    .address("Павлово Поле, ул. Отакара Яроша 55")
                    .contact("https://t.me/daniil_muntyan")
                    .floor((short) (i % 12))
                    .allFloors((short) (12))
                    .district(District.ALEKSEEVKA)
                    .info("Квартира с ремонтом!")
                    .rentalRange(RentalRange.UAH_16000_PLUS)
                    .metro("м. Ботанический Сад")
                    .money("46500 грн + свет, интернет")
                    .telegraph("https://telegra.ph/85542-Prodayotsya-1-komnatnaya-kvartira-Harkov-03-23")
                    .rooms(Rooms.TWO)
                    .mapLink("https://www.google.com/maps/search/?api=1&query=%D0%96%D0%9A+%D0%9B%D0%B5%D0%B2%D0%B0%D0%B4%D0%B0%2C+%D1%83%D0%BB.+%D0%95%D0%BB%D0%B8%D0%B7%D0%B0%D0%B2%D0%B5%D1%82%D0%B8%D0%BD%D1%81%D0%BA%D0%B0%D1%8F+7%D0%B2%2C+%D0%A5%D0%B0%D1%80%D1%8C%D0%BA%D0%BE%D0%B2")
                    .square(32f)
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
