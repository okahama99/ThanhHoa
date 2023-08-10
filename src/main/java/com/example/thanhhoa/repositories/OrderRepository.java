package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<tblOrder, String> {
    tblOrder findFirstByOrderByIdDesc();

    Integer countByProgressStatusAndCreatedDateBetween(Status status, LocalDateTime from, LocalDateTime to);

    Integer  countByStore_IdAndProgressStatusAndCreatedDateBetween(String storeID, Status status, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM tbl_order o WHERE o.created_date between ?1 and ?2", nativeQuery = true)
    Double sumTotal(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM tbl_order o WHERE o.store_id = ?1 and o.created_date between ?2 and ?3", nativeQuery = true)
    Double sumTotalOfAStore(String storeID, LocalDateTime from, LocalDateTime to);
}
