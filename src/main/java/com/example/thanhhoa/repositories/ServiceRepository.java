package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    Service findByName(String name);

    Service findFirstByOrderByIdDesc();

    Service findByIdAndStatus(String id, Status status);
}
