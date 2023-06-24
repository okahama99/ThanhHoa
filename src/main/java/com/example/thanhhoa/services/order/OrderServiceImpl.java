package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private Util util;

    @Override
    public List<ShowOrderModel> getAllOrderByUsername(String username, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowOrderDetailModel> getOrderDetailByOrderID(String orderID) {
        return null;
    }
}
