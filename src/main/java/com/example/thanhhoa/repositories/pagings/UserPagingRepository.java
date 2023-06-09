package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPagingRepository extends PagingAndSortingRepository<tblAccount, Long> {
    Page<tblAccount> findByRole_RoleNameLike(String roleName, Pageable pageable);

    Page<tblAccount> findByFullNameLike(String fullName, Pageable pageable);

    Page<tblAccount> findByStatus(Status status, Pageable pageable);
}
