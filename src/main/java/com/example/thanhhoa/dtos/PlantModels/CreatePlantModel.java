package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreatePlantModel implements Serializable {
    private String name;
    private String description;
    private String careNote;
    private Double height;
    private Double price;
    private Boolean withPot;

    private Long storeID;
    private Integer quantity;
    private Long shipPriceID;
    private List<Long> categoryIDList;
    private List<MultipartFile> files;
}
