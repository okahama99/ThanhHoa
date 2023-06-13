package com.example.thanhhoa.services.plantIMG;

import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantIMGServiceImpl implements PlantIMGService{

    @Autowired
    private PlantIMGRepository plantIMGRepository;

    @Override
    public List<PlantIMG> getByPlantID(Long plantID) {
        return plantIMGRepository.findByPlantId(plantID);
    }
}
