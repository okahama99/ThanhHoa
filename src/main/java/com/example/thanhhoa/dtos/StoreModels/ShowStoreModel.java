package com.example.thanhhoa.dtos.StoreModels;

import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ShowStoreModel implements Serializable {
    private String id;
    private String storeName;
    private String address;
    private String district;
    private String province;
    private List<ShowPlantModel> storePlantList;
}
