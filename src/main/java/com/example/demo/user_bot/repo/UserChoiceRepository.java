package com.example.demo.user_bot.repo;

import com.example.demo.common_part.model.BuyFlat;
import com.example.demo.user_bot.model.UserChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChoiceRepository extends JpaRepository<UserChoice, Long> {
}
