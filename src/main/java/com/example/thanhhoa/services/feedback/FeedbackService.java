package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    List<ShowOrderFeedbackModel> getAllOrderFeedback(Pageable pageable);

    ShowOrderFeedbackModel getOrderFeedbackByID(String orderFeedbackID);

    ShowOrderFeedbackModel getOrderFeedbackByUsername(String username);

    List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(String plantID, Pageable pageable);

    ShowOrderFeedbackModel getOrderFeedbackByOrderID(String orderID);


    // ------------------------------------------------ CONTRACT -------------------------------------------------------------------

    List<ShowContractFeedbackModel> getAllContractFeedback(Pageable pageable);

    ShowContractFeedbackModel getContractFeedbackByID(String contractFeedbackID);

    ShowContractFeedbackModel getContractFeedbackByContractID(String contractID);
}
