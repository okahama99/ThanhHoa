package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreEmployeePagingRepository extends PagingAndSortingRepository<StoreEmployee, String> {
    Page<StoreEmployee> findByStore_IdAndStore_Status(String storeID, Status status, Pageable pageable);
}
