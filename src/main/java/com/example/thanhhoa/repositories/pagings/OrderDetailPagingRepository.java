package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailPagingRepository extends PagingAndSortingRepository<OrderDetail, String> {
    Page<OrderDetail> findByIsFeedbackAndTblOrder_ProgressStatusAndTblOrder_Customer_Id(Boolean isFeedback, Status status, Long userID, Pageable pageable);

    Page<OrderDetail> findAllByTblOrder_ProgressStatusAndTblOrder_Customer_Id(Status status, Long userID, Pageable pageable);
}
