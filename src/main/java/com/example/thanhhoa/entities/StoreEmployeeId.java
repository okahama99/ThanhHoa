package com.example.thanhhoa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class StoreEmployeeId implements Serializable {
    private String tblAccount_id;
}
