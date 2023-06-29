package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.DistancePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistancePriceRepository extends JpaRepository<DistancePrice, String> {
}
