package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Contract implements Serializable {

    @Id
    private String id;

    @Lob
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(length = 50)
    private String paymentMethod;

    @Column
    private Boolean isPaid = false;

    @Lob
    @Column
    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Column
    private LocalDateTime startedDate;

    @Column
    private LocalDateTime endedDate;

    @Column
    private LocalDateTime expectedEndedDate;

    @Column
    private LocalDateTime approvedDate;

    @Column
    private LocalDateTime rejectedDate;

    @Column
    private Double total = 0.0;

    @Column
    private Boolean isFeedback = false;

    @Column
    private Boolean isSigned = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_staff_id")
    private tblAccount staff;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_customer_id")
    private tblAccount customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
    private List<ContractDetail> contractDetailList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
    private List<ContractIMG> contractIMGList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
    private List<Transaction> transactionList;
}
