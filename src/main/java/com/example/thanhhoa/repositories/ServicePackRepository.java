package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServicePack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePackRepository  extends JpaRepository<ServicePack, String> {
}
