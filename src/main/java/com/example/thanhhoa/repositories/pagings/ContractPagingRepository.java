package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractPagingRepository extends PagingAndSortingRepository<Contract, String> {
    Page<Contract> findByStatus(Status status, Pageable pageable);
}
