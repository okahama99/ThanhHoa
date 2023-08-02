package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantPricePagingRepository extends PagingAndSortingRepository<PlantPrice, String> {

    Page<PlantPrice> findByPlant_Id(String plantID, Pageable pageable);

    Page<PlantPrice> findByPriceGreaterThanEqualAndStatus(Double minPrice, Status status, Pageable pageable);

    Page<PlantPrice> findByPriceLessThanEqualAndStatus(Double maxPrice, Status status, Pageable pageable);

    Page<PlantPrice> findByPriceBetweenAndStatus(Double minPrice, Double maxPrice, Status status, Pageable pageable);

    Page<PlantPrice> findByPlant_NameContainingAndPriceBetweenAndStatus(String name, Double minPrice, Double maxPrice, Status status, Pageable pageable);
}
