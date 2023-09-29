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
public class ContractDetail implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String timeWorking;

    @Lob
    @Column(nullable = false)
    private String note;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private LocalDateTime expectedEndDate;

    @Column
    private Double price;

    @Column
    private String plantName;

    @Column
    private String plantStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contractDetail")
    private List<WorkingDate> workingDateList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contractDetail")
    private List<PlantStatusIMG> plantStatusIMGList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_pack_id")
    private ServicePack servicePack;
}
