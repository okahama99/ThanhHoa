package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractFeedbackPagingRepository extends PagingAndSortingRepository<ContractFeedback, String> {
    Page<ContractFeedback> findAllByStatus(Status status, Pageable pageable);
}
