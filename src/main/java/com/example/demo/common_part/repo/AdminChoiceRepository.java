package com.example.demo.common_part.repo;

import com.example.demo.common_part.model.AdminChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminChoiceRepository extends JpaRepository<AdminChoice, Long> {
}
