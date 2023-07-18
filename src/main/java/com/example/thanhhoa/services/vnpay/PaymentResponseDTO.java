package com.example.thanhhoa.services.vnpay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentResponseDTO implements Serializable {

    private String status;
    private String message;
    private String URL;
}
