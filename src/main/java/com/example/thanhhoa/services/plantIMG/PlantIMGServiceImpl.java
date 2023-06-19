package com.example.thanhhoa.services.plantIMG;

import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.services.firebaseIMG.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlantIMGServiceImpl implements PlantIMGService{

    @Autowired
    private PlantIMGRepository plantIMGRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public List<String> getByPlantID(String plantID) {
        List<PlantIMG> imgList = plantIMGRepository.findByPlantId(plantID);
        List<String> imgURL = new ArrayList<>();
        for (PlantIMG plantIMG : imgList) {
            imgURL.add(imageService.getImageUrl(plantIMG.getImgURL()));
        }
        return imgURL;
    }
}
