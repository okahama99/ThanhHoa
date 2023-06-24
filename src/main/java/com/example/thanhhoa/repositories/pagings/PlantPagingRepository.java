package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantPagingRepository extends PagingAndSortingRepository<Plant, String> {
    Page<Plant> findAllByStatus(Status status, Pageable pageable);

    Page<Plant> findByNameContainingAndStatus(String name, Status status, Pageable pageable);

    Page<Plant> findByPlantPrice_PriceGreaterThan(Double minPrice, Status status, Pageable pageable);

    Page<Plant> findByPlantPrice_PriceLessThan(Double maxPrice, Status status, Pageable pageable);

    Page<Plant> findByPlantPrice_PriceBetweenAndStatus(Double fromPrice, Double toPrice, Status status, Pageable pageable);

    Page<Plant> findByPlantPrice_PriceBetweenAndNameContainingAndStatus(Double fromPrice, Double toPrice, String name, Status status, Pageable pageable);
}
