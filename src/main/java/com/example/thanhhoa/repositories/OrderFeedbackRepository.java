package com.example.thanhhoa.repositories;

import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.entities.OrderFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderFeedbackRepository extends JpaRepository<OrderFeedback, Long> {

    Optional<OrderFeedback> findByCustomer_IdAndStatus(Long userID, Status status);
}
