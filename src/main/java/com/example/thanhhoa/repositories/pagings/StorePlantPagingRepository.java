package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePlantPagingRepository extends PagingAndSortingRepository<StorePlant, String> {
    Page<StorePlant> findByStore_IdAndPlant_StatusAndStore_Status(String storeID, Status pStatus, Status sStatus, Pageable pageable);
}
