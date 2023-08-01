package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.WorkingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkingDateRepository extends JpaRepository<WorkingDate, String> {
    List<WorkingDate> findByContractDetail_Id(String contractDetailID);

    WorkingDate findByContractDetail_IdAndWorkingDateBetween(String contractDetailID, LocalDateTime fromDate, LocalDateTime toDate);

    WorkingDate findFirstByOrderByIdDesc();

    WorkingDate findFirstByContractDetail_IdOrderByWorkingDateDesc(String contractDetailID);
}
