package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Transaction findFirstByOrderByIdDesc();

    List<Transaction> findAllByUser_Id(Long userID);

    Transaction findByTransNo(String transNo);
}
