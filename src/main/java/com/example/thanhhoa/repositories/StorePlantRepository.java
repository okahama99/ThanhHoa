package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StorePlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, Long> {
    StorePlant findByPlantAndStore(Plant plant, Store store);

    List<StorePlant> findByPlantId(Long plantID);
}
