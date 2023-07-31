package com.example.thanhhoa.controllers;

import com.example.thanhhoa.enums.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/enum")
public class EnumController {

    @GetMapping(value = "/order", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> order() {
        List<String> list = new ArrayList<>();
        list.add(Status.WAITING.toString());
        list.add(Status.APPROVED.toString());
        list.add(Status.DENIED.toString());
        list.add(Status.PACKAGING.toString());
        list.add(Status.DELIVERING.toString());
        list.add(Status.RECEIVED.toString());
        list.add(Status.STAFFCANCELED.toString());
        list.add(Status.CUSTOMERCANCELED.toString());
        return list;
    }

    @GetMapping(value = "/contract", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> contract() {
        List<String> list = new ArrayList<>();
        list.add(Status.WAITING.toString());
        list.add(Status.DENIED.toString());
        list.add(Status.STAFFCANCELED.toString());
        list.add(Status.CUSTOMERCANCELED.toString());
        list.add(Status.SIGNED.toString());
        list.add(Status.WORKING.toString());
        list.add(Status.DONE.toString());
        return list;
    }

    @GetMapping(value = "/plant", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> plant() {
        List<String> list = new ArrayList<>();
        list.add(Status.ONSALE.toString());
        list.add(Status.INACTIVE.toString());
        return list;
    }

    @GetMapping(value = "/report", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> report() {
        List<String> list = new ArrayList<>();
        list.add(Status.ACTIVE.toString());
        list.add(Status.INACTIVE.toString());
        list.add(Status.DONE.toString());
        return list;
    }

    @GetMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> user() {
        List<String> list = new ArrayList<>();
        list.add(Status.ACTIVE.toString());
        list.add(Status.INACTIVE.toString());
        return list;
    }

    @GetMapping(value = "/activeInactive", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> activeInactive() {
        List<String> list = new ArrayList<>();
        list.add(Status.ACTIVE.toString());
        list.add(Status.INACTIVE.toString());
        return list;
    }

    @GetMapping(value = "/imgEntity", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> imgEntity() {
        List<String> list = new ArrayList<>();
        list.add("CONTRACT");
        list.add("SERVICE");
        list.add("PLANT");
        list.add("ORDERFEEDBACK");
        return list;
    }
}
