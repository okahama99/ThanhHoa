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
    private String description;
    private String careNote;
    private String height;
    private String unit;
    private Boolean withPot;

    private String shipPriceID;
    private List<String> categoryIDList;

    @Nullable
    private Double price;
    @Nullable
    private List<String> listURL;
    @Nullable
    private String name;
}
