package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantShipPricePagingRepository extends PagingAndSortingRepository<PlantShipPrice, String> {
    Page<PlantShipPrice> findByStatus(Status status, Pageable pageable);
}
