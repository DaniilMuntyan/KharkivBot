package com.example.demo.user_bot.service;

import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.RentFlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class RentalFlatService {
    private final RentFlatRepository rentFlatRepository;

    @Autowired
    public RentalFlatService(RentFlatRepository rentFlatRepository) {
        this.rentFlatRepository = rentFlatRepository;
    }

    public RentFlat save(RentFlat rentFlat) {
        return this.rentFlatRepository.save(rentFlat);
    }
}
