package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository  extends JpaRepository<Province, String> {
}
