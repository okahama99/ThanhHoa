package com.example.thanhhoa.services.plantIMG;

import com.example.thanhhoa.entities.PlantIMG;

import java.util.List;

public interface PlantIMGService {
    List<PlantIMG> getByPlantID(Long plantID);
}
