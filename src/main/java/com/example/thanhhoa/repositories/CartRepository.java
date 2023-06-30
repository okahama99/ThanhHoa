package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByAccount_IdAndId(Long accountID, String cartID);

    List<Cart> findByAccount_Id(Long accountID);

    Cart findFirstByOrderByIdDesc();

    void deleteById(String cartID);

    Cart findByPlant_Id(String plantID);
}
