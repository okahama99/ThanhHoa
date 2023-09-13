package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.ContractDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDetailPagingRepository extends PagingAndSortingRepository<ContractDetail, String> {
    Page<ContractDetail> findByContract_Id(String contractID, Pageable pageable);

    Page<ContractDetail> findByContract_Customer_Id(Long userID, Pageable pageable);

    Page<ContractDetail> findByContract_Customer_IdAndTimeWorkingContaining(Long userID, String timeWorking, Pageable pageable);
}
