package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailPagingRepository extends PagingAndSortingRepository<OrderDetail, String> {
    Page<OrderDetail> findByIsFeedbackAndTblOrder_ProgressStatus(Boolean isFeedback, Status status, Pageable pageable);

    Page<OrderDetail> findAllByTblOrder_ProgressStatus(Status status, Pageable pageable);
}
