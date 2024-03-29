package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PlantShipPrice implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String potSize;

    @Column(nullable = false)
    private Double pricePerPlant;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plantShipPrice")
    private List<Plant> plantList;
}
