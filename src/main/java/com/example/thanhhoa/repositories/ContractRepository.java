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

    Contract findByIdAndStaff_Id(String id, Long customerID);

    List<Contract> findByStaff_Id(Long staffID);

    Contract findFirstByOrderByIdDesc();

    Contract findByIdAndStatus(String contractID, Status status);

    Integer countContractByStatusAndCreatedDateBetween(Status status, LocalDateTime from, LocalDateTime to);

    Integer countContractByStore_IdAndStatusAndCreatedDateBetween(String storeID, Status status, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(c.total) FROM heroku_0409ea84bb3cb3d.contract c WHERE c.created_date between ?1 and ?2 and c.status = 'WORKING'", nativeQuery = true)
    Double sumTotalWorking(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(c.total) FROM heroku_0409ea84bb3cb3d.contract c WHERE c.created_date between ?1 and ?2 and c.status = 'DONE'", nativeQuery = true)
    Double sumTotalDone(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(c.total) FROM heroku_0409ea84bb3cb3d.contract c WHERE c.store_id = ?1 and c.created_date between ?2 and ?3 and c.status = 'WORKING'", nativeQuery = true)
    Double sumTotalOfAStoreWorking(String storeID, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(c.total) FROM heroku_0409ea84bb3cb3d.contract c WHERE c.store_id = ?1 and c.created_date between ?2 and ?3 and c.status = 'DONE'", nativeQuery = true)
    Double sumTotalOfAStoreDone(String storeID, LocalDateTime from, LocalDateTime to);

    List<Contract> findAllByStartedDateLessThanEqualAndStatus(LocalDateTime date, Status status);

    List<Contract> findAllByEndedDateLessThanEqualAndStatus(LocalDateTime date, Status status);
}
