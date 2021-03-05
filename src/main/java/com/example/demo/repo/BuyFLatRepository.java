package com.example.demo.repo;

import com.example.demo.model.BuyFlat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyFLatRepository extends JpaRepository<BuyFlat, Long> {

}
