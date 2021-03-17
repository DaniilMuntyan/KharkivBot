package com.example.demo.user_bot.service.entities;

import com.example.demo.user_bot.model.UserChoice;
import com.example.demo.user_bot.repo.UserChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UserChoiceService {
    private final UserChoiceRepository userChoiceRepository;

    @Autowired
    public UserChoiceService(UserChoiceRepository userChoiceRepository) {
        this.userChoiceRepository = userChoiceRepository;
    }

    public void saveUserChoice(UserChoice userChoice) {
        this.userChoiceRepository.save(userChoice);
    }
}
