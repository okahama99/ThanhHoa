package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {
    List<ServiceType> findByService_Id(String serviceID);
}
