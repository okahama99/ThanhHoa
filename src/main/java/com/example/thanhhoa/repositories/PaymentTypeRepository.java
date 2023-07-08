package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, String> {
    PaymentType findFirstByOrderByIdDesc();
}
