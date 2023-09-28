package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class OrderFeedbackIMG implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String imgURL;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_feedback_id")
    private OrderFeedback orderFeedback;
}
