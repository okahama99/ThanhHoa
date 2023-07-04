package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StoreEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreEmployeeRepository extends JpaRepository<StoreEmployee, String> {
    StoreEmployee findByStore_IdAndAccount_Role_RoleName(String storeID, String roleName);
}
