package com.example.thanhhoa.dtos.StoreModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowStorePlantRecordModel implements Serializable {
    private String id;
    private Integer amount;
    private LocalDateTime importDate;
    private String reason;
}
