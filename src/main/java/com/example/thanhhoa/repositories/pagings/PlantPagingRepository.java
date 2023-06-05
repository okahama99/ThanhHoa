package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantPagingRepository extends PagingAndSortingRepository<Plant, Long> {
    Page<Plant> findAllByStatus(String status, Pageable pageable);
}
