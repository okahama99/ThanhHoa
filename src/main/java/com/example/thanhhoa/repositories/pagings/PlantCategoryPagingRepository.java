package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantCategoryPagingRepository extends PagingAndSortingRepository<PlantCategory, String> {
    Page<PlantCategory> findByCategory_IdAndStatus(String categoryID, Status status, Pageable pageable);

    Page<PlantCategory> findByCategory_IdAndPlant_NameContainingAndStatus(String categoryID, String plantName, Status status, Pageable pageable);
}
