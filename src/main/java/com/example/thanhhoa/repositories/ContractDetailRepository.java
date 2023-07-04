package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractDetailRepository extends JpaRepository<ContractDetail, String> {
    List<ContractDetail> findByContract_Id(String contractID);

    ContractDetail findFirstByOrderByIdDesc();

    List<ContractDetail> findByServiceType_IdAndContract_Status(String serviceTypeID, Status status);
}
