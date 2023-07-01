package com.example.thanhhoa.dtos.FeedbackModels;

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
    private ShowRatingModel ratingModel;
    private Double totalPage;

}
