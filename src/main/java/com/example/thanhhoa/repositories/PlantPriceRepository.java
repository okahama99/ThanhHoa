package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantPriceRepository extends JpaRepository<PlantPrice, String> {
    Optional<PlantPrice> findById(String plantPriceID);
}
