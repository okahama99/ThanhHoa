package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPagingRepository extends PagingAndSortingRepository<tblAccount, String> {

    Page<tblAccount> findAllByStatus(Pageable pageable, Status status);

    Page<tblAccount> findByUsernameContainingAndStatus(String username, Pageable pageable, Status status);

    Page<tblAccount> findByFullNameContainingAndStatus(String fullName, Pageable pageable, Status status);

    Page<tblAccount> findByEmailContainingAndStatus(String email, Pageable pageable, Status status);

    Page<tblAccount> findByPhoneAndStatus(String phone, Pageable pageable, Status status);

    Page<tblAccount> findByUsernameContainingAndFullNameContainingAndStatus(String username, String fullName, Pageable pageable, Status status);

    Page<tblAccount> findByFullNameContainingAndEmailContainingAndStatus(String fullName, String email, Pageable pageable, Status status);

    Page<tblAccount> findByFullNameContainingAndPhoneAndStatus(String fullName, String phone, Pageable pageable, Status status);

    Page<tblAccount> findByUsernameContainingAndEmailContainingAndStatus(String username, String email, Pageable pageable, Status status);

    Page<tblAccount> findByUsernameContainingAndPhoneAndStatus(String username, String phone, Pageable pageable, Status status);

    Page<tblAccount> findByEmailContainingAndPhoneAndStatus(String email, String phone, Pageable pageable, Status status);

    Page<tblAccount> findByUsernameContainingAndFullNameContainingAndEmailContainingAndPhoneAndStatus(String username, String fullName, String email, String phone, Pageable pageable, Status status);


}
