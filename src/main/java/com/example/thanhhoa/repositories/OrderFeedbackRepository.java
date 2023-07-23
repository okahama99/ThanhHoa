package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderFeedbackRepository extends JpaRepository<OrderFeedback, String> {

    Optional<OrderFeedback> findByCustomer_UsernameAndStatus(String userID, Status status);

    Optional<OrderFeedback> findById(String orderFeedbackID);

    OrderFeedback findByOrderDetail_Id(String orderDetailID);

    OrderFeedback findFirstByOrderByIdDesc();

    List<OrderFeedback> findAllByStatusAndPlant_Id(Status status, String plantID);
}
