package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Transaction findFirstByOrderByIdDesc();
}
