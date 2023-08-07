package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlantRequest;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<StorePlantRequest, String> {

    StorePlantRequest findFirstByOrderByIdDesc();

    StorePlantRequest findByIdAndStatus(String requestID, Status status);
}
