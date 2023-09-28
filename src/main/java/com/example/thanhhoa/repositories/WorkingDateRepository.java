package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.WorkingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkingDateRepository extends JpaRepository<WorkingDate, String> {
    List<WorkingDate> findByContractDetail_IdOrderByWorkingDateDesc(String contractDetailID);

    List<WorkingDate> findByStaff_Id(Long staffID);

    List<WorkingDate> findByWorkingDateBefore(LocalDateTime date);

    List<WorkingDate> findByContractDetail_IdAndWorkingDateBetween(String contractDetailID, LocalDateTime fromDate, LocalDateTime toDate);

    WorkingDate findFirstByOrderByIdDesc();
}
