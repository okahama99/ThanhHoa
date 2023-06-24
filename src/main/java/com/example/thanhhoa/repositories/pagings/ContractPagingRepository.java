package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Contract;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractPagingRepository extends PagingAndSortingRepository<Contract, String> {
}
