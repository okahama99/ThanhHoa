package com.example.thanhhoa.dtos.FeedbackModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateOrderFeedbackModel implements Serializable {
    private String id;
    private String description;
    private String plantID;
    private String ratingID;
    private String orderDetailID;
    private List<String> listURL;
}
