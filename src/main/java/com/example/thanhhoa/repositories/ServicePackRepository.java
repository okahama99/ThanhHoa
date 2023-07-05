package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServicePack;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePackRepository  extends JpaRepository<ServicePack, String> {
    ServicePack findByIdAndStatus(String id, Status status);
}
