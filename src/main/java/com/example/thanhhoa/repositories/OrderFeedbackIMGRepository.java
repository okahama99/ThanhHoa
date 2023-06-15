package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFeedbackIMGRepository extends JpaRepository<OrderFeedbackIMG, Long> {
}
