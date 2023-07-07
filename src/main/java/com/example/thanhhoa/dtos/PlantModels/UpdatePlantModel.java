package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    private List<String> categoryIDList;
    private List<String> imageUrlList;

    @Nullable
    private Double price;
}
