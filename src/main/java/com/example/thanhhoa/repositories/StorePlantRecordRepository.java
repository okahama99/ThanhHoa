package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlantRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePlantRecordRepository extends JpaRepository<StorePlantRecord, String> {
    StorePlantRecord findFirstByOrderByIdDesc();
}
