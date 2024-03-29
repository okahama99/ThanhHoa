package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPagingRepository extends PagingAndSortingRepository<tblOrder, String>{
    Page<tblOrder> findByCustomer_Id(Long userID, Pageable pageable);

    Page<tblOrder> findByStaff_Id(Long userID, Pageable pageable);

    Page<tblOrder> findByStore_Id(String storeID, Pageable pageable);

    Page<tblOrder> findByCustomer_IdAndProgressStatus(Long userID, Status status, Pageable pageable);

    Page<tblOrder> findByStaff_IdAndProgressStatus(Long userID, Status status, Pageable pageable);

    Page<tblOrder> findByProgressStatusAndStore_Id(Status status, String storeID, Pageable pageable);
}
