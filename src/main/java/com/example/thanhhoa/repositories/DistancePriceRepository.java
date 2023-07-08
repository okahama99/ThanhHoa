package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.DistancePrice;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistancePriceRepository extends JpaRepository<DistancePrice, String> {
    DistancePrice findFirstByOrderByIdDesc();

    DistancePrice findByStatus(Status status);
}
