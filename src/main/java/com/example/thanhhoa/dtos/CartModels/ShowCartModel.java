package com.example.thanhhoa.dtos.CartModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowCartModel implements Serializable {
    private String id;
    private String plantID;
    private Integer quantity;
    private String plantName;
    private Double plantPrice;
    private String image;
}
