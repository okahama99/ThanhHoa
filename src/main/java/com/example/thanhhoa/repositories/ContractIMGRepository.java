package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ContractIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractIMGRepository extends JpaRepository<ContractIMG, String> {
    List<ContractIMG> findByContract_Id(String contractID);

    ContractIMG findFirstByOrderByIdDesc();

    ContractIMG findByImgURL(String imgURL);
}
