package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.CreateOrderModel;
import com.example.thanhhoa.dtos.OrderModels.GetStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.UpdateOrderModel;
import com.example.thanhhoa.enums.Status;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService {

    String createOrder(CreateOrderModel createOrderModel, Long customerID) throws FirebaseMessagingException, MessagingException;

    String updateOrder(UpdateOrderModel updateOrderModel, Long customerID);

    String deleteOrder(String orderID, String reason, Status status) throws FirebaseMessagingException;

    String approveOrder(String orderID, Long staffID, Long userID) throws FirebaseMessagingException;

    Boolean changeOrderStatus(String orderID, String receiptIMG, String status) throws FirebaseMessagingException;

    List<ShowOrderModel> getAllOrderByUserID(Long userID, String roleName, Pageable pageable);

    List<ShowOrderModel> getAllOrder(Pageable pageable);

    List<ShowOrderModel> getAllOrderByStoreID(String storeID, Pageable pageable);

    List<ShowOrderModel> getAllByStatusOrderByUserID(Status status, Long userID, String roleName, Pageable pageable);

    List<ShowOrderModel> getWaitingOrder(String storeID, Pageable pageable);

    ShowOrderModel getByID(String orderID);

    List<ShowOrderDetailModel> getOrderDetailByOrderID(String orderID);

    List<ShowOrderDetailModel> getOrderDetailByIsFeedback(String isFeedback, Pageable pageable);

    List<GetStaffModel> getStaffForOrder();

    String updateIsPaid(String orderID, Boolean isPaid);


}
