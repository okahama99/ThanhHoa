package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PlantPrice implements Serializable {
    @Id
    private String id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private Plant plant;
}
