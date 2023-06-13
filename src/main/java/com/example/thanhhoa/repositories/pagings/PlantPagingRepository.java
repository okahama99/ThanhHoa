package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantPagingRepository extends PagingAndSortingRepository<Plant, Long> {
    Page<Plant> findAllByStatus(Status status, Pageable pageable);

    Page<Plant> findByNameAndStatus(String name, Status status, Pageable pageable);

    Page<Plant> findByPriceGreaterThan(Double minPrice, Status status, Pageable pageable);

    Page<Plant> findByPriceLessThan(Double maxPrice, Status status, Pageable pageable);

    Page<Plant> findByPriceBetweenAndStatus(Double fromPrice, Double toPrice, Status status, Pageable pageable);

    Page<Plant> findByPriceBetweenAndNameAndStatus(Double fromPrice, Double toPrice, String name, Status status, Pageable pageable);
}
