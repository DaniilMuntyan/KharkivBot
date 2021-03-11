package com.example.demo.admin_bot.repo;

import com.example.demo.admin_bot.model.AdminChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminChoiceRepository extends JpaRepository<AdminChoice, Long> {
}
