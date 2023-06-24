package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<ShowOrderModel> getAllOrderByUsername(String username, Pageable pageable);

    List<ShowOrderDetailModel> getOrderDetailByOrderID(String orderID);
}
