package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Province implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 50)
    private String provinceName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "province")
    private List<District> districtList;
}
