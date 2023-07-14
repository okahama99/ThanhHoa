package com.example.thanhhoa.entities;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Role implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 20)
    private String roleName;

    @Enumerated(EnumType.STRING)
    private Status status;
}
