package com.example.thanhhoa.services.vnpay;

import javax.servlet.http.HttpServletRequest;

public interface VNPayService {

    String createOrder(Double total, String reason, String urlReturn, String ipAddr);

    int orderReturn(HttpServletRequest request);
}
