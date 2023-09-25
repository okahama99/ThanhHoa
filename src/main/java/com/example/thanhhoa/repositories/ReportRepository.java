package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByCustomer_IdAndStatusNot(Long userID, Status status);

    List<Report> findByWorkingDate_Id(String workingDateID);

    Report findFirstByOrderByIdDesc();

    Optional<Report> findByIdAndStatus(String reportID, Status status);
}
