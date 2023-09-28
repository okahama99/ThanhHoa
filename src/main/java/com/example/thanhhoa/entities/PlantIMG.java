package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class PlantIMG implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String imgURL;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private Plant plant;
}
