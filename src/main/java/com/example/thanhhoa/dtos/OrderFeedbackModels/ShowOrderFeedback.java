package com.example.thanhhoa.dtos.OrderFeedbackModels;

import com.example.thanhhoa.entities.OrderFeedbackIMG;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowOrderFeedback implements Serializable {
    private Long orderFeedbackID;
    private String description;
    private LocalDateTime createdDate;
    private List<OrderFeedbackIMG> orderFeedbackIMGList;

    private Long ratingID;
    private String ratingDes;

}
