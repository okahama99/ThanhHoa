package com.example.thanhhoa.services.vnpay;

import javax.servlet.http.HttpServletRequest;

public interface VNPayService {

    String createOrder(int total, String orderInfor, String urlReturn);

    int orderReturn(HttpServletRequest request);
}
