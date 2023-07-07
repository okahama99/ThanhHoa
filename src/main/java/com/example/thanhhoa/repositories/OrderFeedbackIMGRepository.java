package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.OrderFeedbackIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFeedbackIMGRepository extends JpaRepository<OrderFeedbackIMG, String> {
    OrderFeedbackIMG findFirstByOrderByIdDesc();
}
