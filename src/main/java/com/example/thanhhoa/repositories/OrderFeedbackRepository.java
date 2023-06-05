package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.OrderFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFeedbackRepository extends JpaRepository<OrderFeedback, Long> {
    List<OrderFeedback> findAllByPlant_IdAndStatus(Long plantID, String status);
}
