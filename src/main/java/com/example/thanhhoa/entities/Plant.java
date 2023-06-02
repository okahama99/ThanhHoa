package com.example.thanhhoa.entities;

import com.example.thanhhoa.constants.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double rentPrice;

    @Column
    private Boolean withPot = false;

    @Column
    private Boolean forRent = false;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
