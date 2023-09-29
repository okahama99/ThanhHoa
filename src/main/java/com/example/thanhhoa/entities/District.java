package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class District implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 50)
    private String districtName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;
}
