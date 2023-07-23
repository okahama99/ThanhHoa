package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.CreateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.CreateOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateOrderFeedbackModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {

    String createOrder(CreateOrderFeedbackModel createOrderFeedbackModel, Long userID);

    String updateOrder(UpdateOrderFeedbackModel updateOrderFeedbackModel);

    String deleteOrder(String id);

    List<ShowOrderFeedbackModel> getAllOrderFeedback(Pageable pageable);

    ShowOrderFeedbackModel getOrderFeedbackByID(String orderFeedbackID);

    ShowOrderFeedbackModel getOrderFeedbackByUsername(String username);

    List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(String plantID, Pageable pageable);

    List<ShowOrderFeedbackModel> getOrderFeedbackByRatingID(String ratingID, Pageable pageable);

    ShowOrderFeedbackModel getOrderFeedbackByOrderDetailID(String orderDetailID);


    // ------------------------------------------------ CONTRACT -------------------------------------------------------------------

    String createContract(CreateContractFeedbackModel createContractFeedbackModel, Long userID);

    String updateContract(UpdateContractFeedbackModel updateContractFeedbackModel);

    String deleteContract(String id);

    List<ShowContractFeedbackModel> getAllContractFeedback(Pageable pageable);

    ShowContractFeedbackModel getContractFeedbackByID(String contractFeedbackID);

    ShowContractFeedbackModel getContractFeedbackByContractID(String contractID);

    List<ShowRatingModel> getRating();
}
