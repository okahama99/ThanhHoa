package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition="nvarchar(max)")
    private String description;

    @Column(nullable = false, columnDefinition="nvarchar(max)")
    private String careNote;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double price;

    @Column
    private Boolean withPot = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<StorePlant> storePlantList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<PlantIMG> plantIMGList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_ship_price_id")
    private PlantShipPrice plantShipPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<PlantCategory> plantCategoryList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<Cart> cartList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<OrderDetail> orderDetailList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plant")
    private List<OrderFeedback> orderFeedbackList;
}
