package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class DistancePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Column(nullable = false)
    private Double pricePerKm;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "distancePrice")
    private List<tblOrder> tblOrderList;
}
