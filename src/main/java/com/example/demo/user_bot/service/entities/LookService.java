package com.example.demo.user_bot.service.entities;

import com.example.demo.common_part.model.Look;
import com.example.demo.common_part.repo.LookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class LookService {
    private final LookRepository lookRepository;

    @Autowired
    public LookService(LookRepository lookRepository) {
        this.lookRepository = lookRepository;
    }

    public Look save(Look newLook) {
        return this.lookRepository.save(newLook);
    }
}
