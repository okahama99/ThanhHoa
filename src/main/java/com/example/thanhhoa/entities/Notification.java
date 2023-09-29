package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification implements Serializable {

    @Id
    private String id;

    @Lob
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String link;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private Boolean isRead = false;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private tblAccount tblAccount;
}
