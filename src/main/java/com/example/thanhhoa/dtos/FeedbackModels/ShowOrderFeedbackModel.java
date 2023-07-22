package com.example.thanhhoa.dtos.FeedbackModels;

import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowPlantModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowOrderFeedbackModel implements Serializable {
    private String orderFeedbackID;
    private String description;
    private Status status;
    private LocalDateTime createdDate;

    private List<ShowOrderFeedbackIMGModel> imgList;
    private ShowPlantModel showPlantModel;
    private ShowRatingModel ratingModel;
    private ShowCustomerModel showCustomerModel;
    private Double totalPage;

}
