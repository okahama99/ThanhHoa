package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Store implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "store")
    private List<StoreEmployee> empList;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "store")
    private List<Contract> contractList;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "store")
    private List<StorePlant> storePlantList;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "store")
    private List<tblOrder> tblOrderList;


}
