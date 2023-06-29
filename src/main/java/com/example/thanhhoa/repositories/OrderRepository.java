package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.tblOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<tblOrder, String> {
    tblOrder findFirstByOrderByIdDesc();
}
