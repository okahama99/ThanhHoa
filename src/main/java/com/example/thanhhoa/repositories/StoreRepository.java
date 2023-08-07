package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Store findByStoreName(String name);

    Store findFirstByOrderByIdDesc();

    Store findByIdAndStatus(String storeID, Status status);

    List<Store> findAllByOrderByIdDesc();
}
