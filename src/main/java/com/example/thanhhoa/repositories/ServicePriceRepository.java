package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePriceRepository extends JpaRepository<ServicePrice, String> {
    ServicePrice findByIdAndStatus(String id, Status status);

    List<ServicePrice> findByService_IdAndStatus(String serviceID, Status status);

    ServicePrice findFirstByOrderByIdDesc();

    ServicePrice findFirstByService_IdAndStatusOrderByApplyDateDesc(String serviceID, Status status);
}
