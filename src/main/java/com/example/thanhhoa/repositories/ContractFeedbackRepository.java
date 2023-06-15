package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractFeedbackRepository extends JpaRepository<ContractFeedback, Long> {
    ContractFeedback findByIdAndStatus(Long contractFeedbackID, Status status);
}
