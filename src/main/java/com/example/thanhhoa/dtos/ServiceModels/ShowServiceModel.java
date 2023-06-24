package com.example.thanhhoa.dtos.ServiceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ShowServiceModel implements Serializable {
    private String serviceID;
    private String name;
    private Double price;
    private String description;
    private List<ShowServiceTypeModel> typeList;
    private Double totalPage;
}
