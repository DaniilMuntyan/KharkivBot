package com.example.demo.common_part.repo;

import com.example.demo.common_part.model.Look;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookRepository extends JpaRepository<Look, Long> {

}
