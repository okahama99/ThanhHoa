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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction implements Serializable {

    @Id
    private String id;

    @Column
    private String billNo;

    @Column
    private String transNo;

    @Column
    private String bankCode;

    @Column
    private String cardType;

    @Column
    private Integer amount;

    @Column
    private String currency;

    @Column
    private String bankAccountNo;

    @Column
    private String bankAccount;

    @Column
    private String refundBankCode;

    @Lob
    @Column
    private String reason;

    @Column
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private tblOrder tblOrder;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private tblAccount user;
}
