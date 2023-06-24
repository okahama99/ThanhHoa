package com.example.thanhhoa.dtos.CartModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddCartModel implements Serializable {
    private String plantID;
    private Integer quantity;
}
