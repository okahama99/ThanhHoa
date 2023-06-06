package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class tblAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String UID;

    @Column
    private String fcmToken;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column
    private Boolean gender = false;

    @Column(nullable = false)
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tblAccount")
    private List<Notification> notificationList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}