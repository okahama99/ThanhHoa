package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFeedbackPagingRepository extends PagingAndSortingRepository<OrderFeedback, String> {
    Page<OrderFeedback> findAllByStatus(Status status, Pageable pageable);

    Page<OrderFeedback> findByPlantId(String plantID, Pageable pageable);
    
}
