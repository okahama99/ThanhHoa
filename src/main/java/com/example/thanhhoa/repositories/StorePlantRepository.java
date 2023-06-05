package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, Long> {
}
