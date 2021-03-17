package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.RentFlatRepository;
import com.example.demo.common_part.utils.District;
import com.example.demo.common_part.utils.RentalRange;
import com.example.demo.common_part.utils.Rooms;
import com.example.demo.user_bot.cache.DataCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public final class RentalFlatService {
    private static final Logger LOGGER = Logger.getLogger(RentalFlatService.class);
    private final RentFlatRepository rentFlatRepository;
    private final DataCache dataCache;

    @Autowired
    public RentalFlatService(RentFlatRepository rentFlatRepository, DataCache dataCache) {
        this.rentFlatRepository = rentFlatRepository;
        this.dataCache = dataCache;

        /*for (int i = 0; i < 48; ++i) {
            RentFlat rentFlat = RentFlat.builder()
                    .address("Проспект Гагарина, ул. Москалёвская 5")
                    .contact("https://t.me/daniil_muntyan")
                    //.floor((short) ThreadLocalRandom.current().nextInt(1, 12))
                    .floor((short) i)
                    .allFloors((short) 16)
                    .district(District.KLOCHKOVSKOY)
                    .info("Новострой!")
                    .rentalRange(RentalRange.UAH_6000_8000)
                    .metro("м. Холодная гора")
                    .money("4600 грн + интернет")
                    .telegraph("https://telegra.ph/83544-Sdayotsya-1-komnatnaya-kvartira-Saltovka-03-13")
                    .rooms(Rooms.TWO)
                    .square(45.5f)
                    .build();
            rentFlatRepository.save(rentFlat);
        }*/

        /*// Добавляю все квартиры под аренду из базы данных в HashMap
        this.initRentFlatsCache();*/

    }
    /*private void initRentFlatsCache() {
        List<RentFlat> allExistedRentFlats = this.rentFlatRepository.findAll();
        Map<Long, RentFlat> rentFlatsCache = this.dataCache.getRentFlatsCacheMap();
        for (RentFlat temp: allExistedRentFlats) {
            rentFlatsCache.put(temp.getId(), temp);
        }
        LOGGER.info("RentFlatsCache has " + rentFlatsCache.size() + " flats");
    }*/

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
