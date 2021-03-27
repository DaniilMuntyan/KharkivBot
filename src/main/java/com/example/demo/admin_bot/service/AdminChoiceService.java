package com.example.demo.admin_bot.service;

import com.example.demo.admin_bot.keyboards.NewFlatMenu;
import com.example.demo.admin_bot.model.AdminChoice;
import com.example.demo.admin_bot.repo.AdminChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminChoiceService {
    private final AdminChoiceRepository adminChoiceRepository;

    @Autowired
    public AdminChoiceService(AdminChoiceRepository adminChoiceRepository) {
        this.adminChoiceRepository = adminChoiceRepository;
    }

    public AdminChoice saveChoice(AdminChoice adminChoice) {
        return adminChoiceRepository.save(adminChoice);
    }
}
