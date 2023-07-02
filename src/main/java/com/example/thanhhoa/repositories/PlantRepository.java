package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, String> {
    Plant findByName(String plantName);

    Optional<Plant> findById(String plantID);

    Plant findFirstByOrderByIdDesc();

    List<Plant> findAllByStatus(Status status);

    List<Plant> findAllByNameContainingAndStatus(String name, Status status);
}
