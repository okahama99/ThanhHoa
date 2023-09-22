package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPagingRepository extends PagingAndSortingRepository<Report, String> {
    Page<Report> findAllByStatus(Status status, Pageable pageable);

    Page<Report> findByWorkingDate_ContractDetail_Id(String contractDetailID, Pageable pageable);
}
