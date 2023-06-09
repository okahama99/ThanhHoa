package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<tblAccount, Long> {
    tblAccount findByEmailAndStatus(String email, Status status);

    tblAccount getByUsername(String username);

    tblAccount getByEmail(String email);

    tblAccount getByPhone(String phone);

    Optional<tblAccount> findByUsernameOrEmailOrPhone(String username, String email, String password);

}
