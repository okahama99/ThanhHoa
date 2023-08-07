package com.example.thanhhoa.dtos.StorePlantRequestModels;

import com.example.thanhhoa.dtos.OrderModels.ShowPlantModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowRequestModel implements Serializable {
    private String id;
    private Integer quantity;
    private String reason;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Status status;
    private ShowStoreModel fromStoreModel;
    private ShowStoreModel toStoreModel;
    private ShowStaffModel fromManagerModel;
    private ShowStaffModel toManagerModel;
    private ShowPlantModel showPlantModel;
    private Double totalPage;
}
