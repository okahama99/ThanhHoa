package com.example.thanhhoa.services.google;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GooglePojo {
    private String id;
    private String email;
    private boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String link;
    private String picture;
}
