package com.example.thanhhoa.dtos.ServiceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowServiceIMGModel implements Serializable {
    private String id;
    private String url;
}
