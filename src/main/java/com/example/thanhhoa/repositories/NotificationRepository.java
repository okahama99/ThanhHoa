package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByTblAccount_IdOrderByDateDesc(Long userID);

    Notification findFirstByOrderByIdDesc();
}
