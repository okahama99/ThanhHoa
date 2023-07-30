package com.example.thanhhoa.dtos.ServiceModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ShowServiceModel implements Serializable {
    private String serviceID;
    private String name;
    private String priceID;
    private Double price;
    private String description;
    private Status status;
    private List<ShowServiceTypeModel> typeList;
    private Double totalPage;
    private List<ShowServiceIMGModel> imgList;
    @Nullable
    private Boolean atHome = false;
}
