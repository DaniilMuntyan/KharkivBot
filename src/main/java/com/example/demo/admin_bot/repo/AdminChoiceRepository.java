package com.example.demo.admin_bot.repo;

import com.example.demo.admin_bot.model.AdminChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminChoiceRepository extends JpaRepository<AdminChoice, Long> {

}
