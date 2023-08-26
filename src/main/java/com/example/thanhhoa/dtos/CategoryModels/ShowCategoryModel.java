package com.example.thanhhoa.dtos.CategoryModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowCategoryModel {
    private String categoryID;
    private String categoryName;
    private Status status;
    private Double totalPage;
}
