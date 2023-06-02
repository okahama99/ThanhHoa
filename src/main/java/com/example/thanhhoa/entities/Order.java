package com.example.thanhhoa.entities;

import com.example.thanhhoa.constants.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @Column(nullable = false)
    private String progressStatus;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime approveDate;

    @Column(nullable = false)
    private LocalDateTime packageDate;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Column(nullable = false)
    private LocalDateTime receivedDate;

    @Column(nullable = false)
    private LocalDateTime rejectDate;

    @Column
    private Double distance = 0.0;

    @Column
    private Double totalShipCost = 0.0;

    @Column
    private Double total = 0.0;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
