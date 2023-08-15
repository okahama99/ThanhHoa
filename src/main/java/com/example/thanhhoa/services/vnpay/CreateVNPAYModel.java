package com.example.thanhhoa.services.vnpay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateVNPAYModel implements Serializable {
    private Integer amount;
    private String reason;
}
