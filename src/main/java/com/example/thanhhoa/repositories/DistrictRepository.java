package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository  extends JpaRepository<District, String> {
    List<District> findByProvince_Id(String provinceID);
}
