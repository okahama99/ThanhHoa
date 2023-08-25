package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPagingRepository extends PagingAndSortingRepository<tblAccount, String> {

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


    // --------------------------------------------------------------------------------------------------------------------

    Page<tblAccount> findAllByRole_RoleName(String role, Pageable pageable);

    Page<tblAccount> findByUsernameContainingAndStatusAndRole_RoleName(String username, Pageable pageable, Status status, String role);

    Page<tblAccount> findByFullNameContainingAndStatusAndRole_RoleName(String fullName, Pageable pageable, Status status, String role);

    Page<tblAccount> findByEmailContainingAndStatusAndRole_RoleName(String email, Pageable pageable, Status status, String role);

    Page<tblAccount> findByPhoneAndStatusAndRole_RoleName(String phone, Pageable pageable, Status status, String role);

    Page<tblAccount> findByUsernameContainingAndFullNameContainingAndStatusAndRole_RoleName(String username, String fullName, Pageable pageable, Status status, String role);

    Page<tblAccount> findByFullNameContainingAndEmailContainingAndStatusAndRole_RoleName(String fullName, String email, Pageable pageable, Status status, String role);

    Page<tblAccount> findByFullNameContainingAndPhoneAndStatusAndRole_RoleName(String fullName, String phone, Pageable pageable, Status status, String role);

    Page<tblAccount> findByUsernameContainingAndEmailContainingAndStatusAndRole_RoleName(String username, String email, Pageable pageable, Status status, String role);

    Page<tblAccount> findByUsernameContainingAndPhoneAndStatusAndRole_RoleName(String username, String phone, Pageable pageable, Status status, String role);

    Page<tblAccount> findByEmailContainingAndPhoneAndStatusAndRole_RoleName(String email, String phone, Pageable pageable, Status status, String role);

    Page<tblAccount> findByUsernameContainingAndFullNameContainingAndEmailContainingAndPhoneAndStatusAndRole_RoleName(String username, String fullName, String email, String phone, Pageable pageable, Status status, String role);

}
