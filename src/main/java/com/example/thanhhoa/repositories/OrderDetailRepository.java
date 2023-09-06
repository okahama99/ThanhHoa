package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByTblOrder_Id(String orderID);

    OrderDetail findFirstByOrderByIdDesc();

    List<OrderDetail> findByStorePlant_IdAndTblOrder_ProgressStatus(String storePlantID, Status status);
}
