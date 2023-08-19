package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    List<Contract> findByCustomer_Id(Long customerID);

    List<Contract> findByStaff_Id(Long staffID);

    Contract findFirstByOrderByIdDesc();

    Contract findByIdAndStatus(String contractID, Status status);

    Integer countContractByStatusAndCreatedDateBetween(Status status, LocalDateTime from, LocalDateTime to);

    Integer countContractByStore_IdAndStatusAndCreatedDateBetween(String storeID, Status status, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(c.total) FROM Contract c WHERE c.created_date between ?1 and ?2 and c.status = ?3", nativeQuery = true)
    Double sumTotal(LocalDateTime from, LocalDateTime to, Status status);

    @Query(value = "SELECT SUM(c.total) FROM Contract c WHERE c.store_id = ?1 and c.created_date between ?2 and ?3 and c.status = ?4", nativeQuery = true)
    Double sumTotalOfAStore(String storeID, LocalDateTime from, LocalDateTime to, Status status);

    List<Contract> findAllByStartedDateLessThanEqualAndStatus(LocalDateTime date, Status status);

    List<Contract> findAllByEndedDateLessThanEqualAndStatus(LocalDateTime date, Status status);
}
