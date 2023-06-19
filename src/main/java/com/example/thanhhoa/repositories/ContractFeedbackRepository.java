package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractFeedbackRepository extends JpaRepository<ContractFeedback, Long> {
    Optional<ContractFeedback> findByContract_Customer_UsernameAndStatus(String username, Status status);

    Optional<ContractFeedback> findById(String contractFeedbackID);

    Optional<ContractFeedback> findByContract_Id(String contractID);
}
