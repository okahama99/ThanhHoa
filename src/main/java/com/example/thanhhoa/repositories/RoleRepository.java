package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, String> {
    Role getRoleByRoleName(String roleName);

    Role findByIdAndStatus(String id, Status status);

    Role findFirstByOrderByIdDesc();
}
