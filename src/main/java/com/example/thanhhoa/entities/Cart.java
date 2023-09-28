package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Cart implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private tblAccount account;
}
