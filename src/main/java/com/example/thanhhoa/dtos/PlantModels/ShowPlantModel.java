package com.example.thanhhoa.dtos.PlantModels;

import com.example.thanhhoa.dtos.OrderFeedbackModels.ShowOrderFeedback;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowPlantModel {
    private Long plantID;
    private String name;
    private Double height;
    private Double price;
    private Boolean withPot;
    private String potSize;

    private List<ShowPlantCategory> plantCategoryList;
    private List<ShowOrderFeedback> orderFeedbackList;

    private Integer totalFeedback;
    private Double totalPage;
}
