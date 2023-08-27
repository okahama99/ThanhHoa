package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPagingRepository extends PagingAndSortingRepository<Transaction, String> {
    Page<Transaction> findByContract_Store_Id(String storeID, Pageable pageable);

    Page<Transaction> findByTblOrder_Store_Id(String storeID, Pageable pageable);
}
