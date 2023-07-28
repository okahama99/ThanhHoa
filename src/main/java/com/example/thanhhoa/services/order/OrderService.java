package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.CreateOrderModel;
import com.example.thanhhoa.dtos.OrderModels.GetStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.UpdateOrderModel;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    String createOrder(CreateOrderModel createOrderModel, Long customerID);

    String updateOrder(UpdateOrderModel updateOrderModel, Long customerID);

    String deleteOrder(String orderID, String reason, Status status);

    String approveOrder(String orderID);

    Boolean changeOrderStatus(String orderID, String status);

    List<ShowOrderModel> getAllOrderByUsername(String username, Pageable pageable);

    List<ShowOrderModel> getAllOrder(Pageable pageable);

    List<ShowOrderModel> getAllOrderByStoreID(String storeID, Pageable pageable);

    List<ShowOrderModel> getAllByStatusOrderByUsername(Status status, String username, Pageable pageable);

    List<ShowOrderModel> getWaitingOrder(Pageable pageable);

    List<ShowOrderDetailModel> getOrderDetailByOrderID(String orderID);

    List<ShowOrderDetailModel> getOrderDetailByIsFeedback(String isFeedback, Pageable pageable);

    List<GetStaffModel> getStaffForOrder();


}
