package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class DistancePrice implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Column(nullable = false)
    private Double pricePerKm;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "distancePrice")
    private List<tblOrder> tblOrderList;
}
