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
public class CreatePlantModel implements Serializable {
    private String name;
    private String description;
    private String careNote;
    private String height;
    private String unit;
    private Boolean withPot;

    private String shipPriceID;
    private List<String> categoryIDList;

    private Double price;
    private LocalDateTime applyDate;
    @Nullable
    private List<String> listURL;
}
