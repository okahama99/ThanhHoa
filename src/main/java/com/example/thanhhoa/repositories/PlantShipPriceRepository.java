package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantShipPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantShipPriceRepository extends JpaRepository<PlantShipPrice, Long> {
}
