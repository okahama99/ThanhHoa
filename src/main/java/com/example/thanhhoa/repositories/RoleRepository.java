package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role getRoleByRoleName(String roleName);
}
