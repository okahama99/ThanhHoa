//package com.example.thanhhoa.entities;
//
//import com.example.thanhhoa.enums.Status;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.Lob;
//import javax.persistence.ManyToOne;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//public class StorePlantRequest implements Serializable {
//
//    @Id
//    private String id;
//
//    @Column
//    private Integer quantity;
//
//    @Lob
//    @Column
//    private String reason;
//
//    @Column
//    private LocalDateTime createDate;
//
//    @Column
//    private LocalDateTime updateDate;
//
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "from_store_id")
//    private Store fromStore;
//
//    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "to_store_id")
//    private Store toStore;
//
//    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "from_manager_id")
//    private StoreEmployee fromManager;
//
//    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "to_manager_id")
//    private StoreEmployee toManager;
//
//    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "plant_id")
//    private Plant plant;
//}
