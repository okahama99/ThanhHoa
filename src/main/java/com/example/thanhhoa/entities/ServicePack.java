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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ServicePack implements Serializable {
    @Id
    private String id;

    @Column(nullable = false)
    private String range;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private Integer percentage;

    @Column(nullable = false)
    private LocalDateTime applyDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "servicePack")
    private List<ContractDetail> contractDetailList;
}
