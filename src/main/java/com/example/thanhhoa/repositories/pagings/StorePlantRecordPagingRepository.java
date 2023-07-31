package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.StorePlantRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePlantRecordPagingRepository extends PagingAndSortingRepository<StorePlantRecord, String> {
    Page<StorePlantRecord> findByStorePlant_Id(String storePlantID, Pageable pageable);
}
