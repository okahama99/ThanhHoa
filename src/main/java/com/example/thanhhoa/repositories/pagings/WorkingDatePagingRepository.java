package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.WorkingDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingDatePagingRepository extends PagingAndSortingRepository<WorkingDate, String>{
    Page<WorkingDate> findByContractDetail_IdOrderByWorkingDateDesc(String contractDetailID, Pageable pageable);

    Page<WorkingDate> findByContractDetail_Contract_IdOrderByWorkingDateDesc(String contractID, Pageable pageable);
}
