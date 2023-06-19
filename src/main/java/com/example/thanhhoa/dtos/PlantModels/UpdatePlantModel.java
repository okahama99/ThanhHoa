package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdatePlantModel implements Serializable{
    private String plantID;
    private String name;
    private String description;
    private String careNote;
    private Double height;
    private Boolean withPot;

    private String shipPriceID;
    private String plantPriceID;
    private List<String> categoryIDList;
    private List<MultipartFile> files;
}
