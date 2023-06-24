package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantIMGRepository extends JpaRepository<PlantIMG, String> {
    PlantIMG findByImgURL(String imgName);

    List<PlantIMG> findByPlantId(String plantId);
}
