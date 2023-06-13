package com.example.thanhhoa.controllers;

import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.services.plantIMG.PlantIMGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plantIMG")
public class PlantIMGController {
    @Autowired
    private PlantIMGService plantIMGService;

    @GetMapping(value = "/getByPlantID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<PlantIMG> getIMGByPlantID(@RequestParam Long plantID) {
        return plantIMGService.getByPlantID(plantID);
    }
}
