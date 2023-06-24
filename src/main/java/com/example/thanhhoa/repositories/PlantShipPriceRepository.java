package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantShipPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantShipPriceRepository extends JpaRepository<PlantShipPrice, String> {
    Optional<PlantShipPrice> findById(String plantShipPriceID);
}
