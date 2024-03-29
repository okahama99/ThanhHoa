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

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.created_date between ?1 and ?2 and o.progress_status = 'APPROVED'", nativeQuery = true)
    Double sumTotalApproved(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.created_date between ?1 and ?2 and o.progress_status = 'PACKAGING'", nativeQuery = true)
    Double sumTotalPackaging(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.created_date between ?1 and ?2 and o.progress_status = 'DELIVERING'", nativeQuery = true)
    Double sumTotalDelivering(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.created_date between ?1 and ?2 and o.progress_status = 'RECEIVED'", nativeQuery = true)
    Double sumTotalReceived(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.store_id = ?1 and o.created_date between ?2 and ?3 and o.progress_status = 'APPROVED'", nativeQuery = true)
    Double sumTotalOfAStoreApproved(String storeID, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.store_id = ?1 and o.created_date between ?2 and ?3 and o.progress_status = 'PACKAGING'", nativeQuery = true)
    Double sumTotalOfAStorePackaging(String storeID, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.store_id = ?1 and o.created_date between ?2 and ?3 and o.progress_status = 'DELIVERING'", nativeQuery = true)
    Double sumTotalOfAStoreDelivering(String storeID, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(o.total) FROM heroku_0409ea84bb3cb3d.tbl_order o WHERE o.store_id = ?1 and o.created_date between ?2 and ?3 and o.progress_status = 'RECEIVED'", nativeQuery = true)
    Double sumTotalOfAStoreReceived(String storeID, LocalDateTime from, LocalDateTime to);
}
