package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WorkingDate implements Serializable {

    @Id
    private String id;

    @Lob
    @Column
    private String note;

    @Column(nullable = false)
    private LocalDateTime workingDate;

    @Column
    private LocalDateTime startWorking;

    @Column
    private LocalDateTime endWorking;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_detail_id")
    private ContractDetail contractDetail;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private tblAccount staff;
}
