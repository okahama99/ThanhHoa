package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    List<Contract> findByCustomer_Id(Long customerID);

    List<Contract> findByStaff_Id(Long staffID);

    Contract findFirstByOrderByIdDesc();

    Contract findByIdAndStatus(String contractID, Status status);
}
