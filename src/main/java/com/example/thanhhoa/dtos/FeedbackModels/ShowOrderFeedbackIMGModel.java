package com.example.thanhhoa.dtos.FeedbackModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowOrderFeedbackIMGModel implements Serializable {
    private String id;
    private String imgURL;
}