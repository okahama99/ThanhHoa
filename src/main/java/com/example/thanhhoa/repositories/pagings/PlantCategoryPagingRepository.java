package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.PlantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantCategoryPagingRepository extends PagingAndSortingRepository<PlantCategory, String> {
    Page<PlantCategory> findByCategory_Id(String categoryID, Pageable pageable);

    Page<PlantCategory> findByCategory_IdAndPlant_NameContaining(String categoryID, String plantName, Pageable pageable);
}
