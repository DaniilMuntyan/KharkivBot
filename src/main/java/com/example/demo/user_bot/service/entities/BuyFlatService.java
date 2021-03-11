package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.common_part.model.RentFlat;
import com.example.demo.common_part.repo.BuyFLatRepository;
import com.example.demo.common_part.repo.RentFlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class BuyFlatService {
    private final BuyFLatRepository buyFLatRepository;

    @Autowired
    public BuyFlatService(BuyFLatRepository buyFLatRepository) {
        this.buyFLatRepository = buyFLatRepository;
    }

    public BuyFlat save(BuyFlat buyFlat) {
        return this.buyFLatRepository.save(buyFlat);
    }
}
