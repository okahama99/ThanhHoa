package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class PaymentType implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private Integer value;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paymentType")
    private List<Contract> contractList;
}
