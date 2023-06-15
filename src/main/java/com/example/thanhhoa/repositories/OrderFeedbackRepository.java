package com.example.thanhhoa.repositories;

import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.entities.OrderFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFeedbackRepository extends JpaRepository<OrderFeedback, Long> {

    OrderFeedback findByIdAndStatus(Long orderFeedbackID, Status status);
}
