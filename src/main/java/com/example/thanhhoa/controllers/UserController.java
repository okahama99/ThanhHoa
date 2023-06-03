package com.example.thanhhoa.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UserController {

    @GetMapping
    public String testApi(){
        return "Cai nay la jdk 8 lan so 2";
    }
}
