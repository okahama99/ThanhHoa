package com.example.thanhhoa.dtos.FeedbackModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateContractFeedbackModel implements Serializable {
    private String description;
    private String ratingID;
    private String contractID;
}
