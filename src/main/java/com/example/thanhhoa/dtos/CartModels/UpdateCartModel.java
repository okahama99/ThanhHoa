package com.example.thanhhoa.dtos.CartModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateCartModel implements Serializable {
    private String cartID;
    private Integer quantity;
}
