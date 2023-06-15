package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    List<ShowOrderFeedbackModel> getAllOrderFeedback(Pageable pageable);

    ShowOrderFeedbackModel getOrderFeedbackByID(Long orderFeedbackID);

    ShowOrderFeedbackModel getOrderFeedbackByUserID(Long userID);

    List<ShowContractFeedbackModel> getAllContractFeedback(Pageable pageable);

    ShowContractFeedbackModel getContractFeedbackByID(Long contractFeedbackID);
}
