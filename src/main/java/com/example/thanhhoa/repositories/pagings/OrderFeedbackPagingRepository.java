package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFeedbackPagingRepository extends PagingAndSortingRepository<OrderFeedback, String> {
    Page<OrderFeedback> findAllByStatusAndCustomer_Id(Status status, Long userID, Pageable pageable);

    Page<OrderFeedback> findByPlant_Id(String plantID, Pageable pageable);

    Page<OrderFeedback> findByPlant_IdAndStatus(String plantID, Status status, Pageable pageable);

    Page<OrderFeedback> findByRating_Id(String plantID, Pageable pageable);

    Page<OrderFeedback> findByRating_IdAndStatus(String plantID, Status status, Pageable pageable);

}
