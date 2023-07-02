package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class tblOrder implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Lob
    @Column
    private String reason;

    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private Status progressStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime approveDate;

    @Column
    private LocalDateTime packageDate;

    @Column
    private LocalDateTime deliveryDate;

    @Column
    private LocalDateTime receivedDate;

    @Column
    private LocalDateTime rejectDate;

    @Column
    private Double distance = 0.0;

    @Column
    private Double totalShipCost = 0.0;

    @Column
    private Double total = 0.0;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private tblAccount staff;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private tblAccount customer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "distance_price_id")
    private DistancePrice distancePrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tblOrder")
    private List<OrderDetail> orderDetailList;




}
