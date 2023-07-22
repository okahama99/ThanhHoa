package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDetailPagingRepository extends PagingAndSortingRepository<OrderDetail, String> {

    Page<OrderDetail> findByIsFeedback(Boolean isFeedback, Pageable pageable);
}
