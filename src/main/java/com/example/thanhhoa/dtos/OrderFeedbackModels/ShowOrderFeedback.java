package com.example.thanhhoa.dtos.OrderFeedbackModels;

import com.example.thanhhoa.entities.OrderFeedbackIMG;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowOrderFeedback {
    private Long orderFeedbackID;
    private String description;
    private LocalDateTime createdDate;

    private List<OrderFeedbackIMG> orderFeedbackIMGList;
}
