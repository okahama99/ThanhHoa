package com.example.thanhhoa.dtos.FeedbackModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowContractFeedbackModel implements Serializable {
    private String contractFeedbackID;
    private String description;
    private LocalDateTime createdDate;

    private ShowRatingModel ratingModel;
    private Double totalPage;
}
