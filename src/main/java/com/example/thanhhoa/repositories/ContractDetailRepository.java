package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractDetailRepository extends JpaRepository<ContractDetail, String> {

    ContractDetail findFirstByOrderByIdDesc();

    List<ContractDetail> findByServiceType_IdAndContract_Status(String serviceTypeID, Status status);

    List<ContractDetail> findByContract_Staff_Id(Long staffID);

    List<ContractDetail> findByContract_Customer_Id(Long customerID);

    List<ContractDetail> findByContract_Staff_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long staffID, LocalDateTime startDate, LocalDateTime endDate);

    List<ContractDetail> findByContract_Customer_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long customerID, LocalDateTime startDate, LocalDateTime endDate);

    List<ContractDetail> findByContract_Staff_IdAndStartDateBetweenAndEndDateBetween
            (Long staffID, LocalDateTime fStartDate, LocalDateTime tStartDate, LocalDateTime fEndDate, LocalDateTime tEndDate);
}
