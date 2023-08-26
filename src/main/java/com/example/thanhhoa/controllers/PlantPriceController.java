package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.plantPrice.PlantPriceService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plantPrice")
public class PlantPriceController {

    @Autowired
    private PlantPriceService service;
    @Autowired
    private Util util;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantPriceModel> getAll(@RequestParam(required = false) String plantID,
                                     @RequestParam int pageNo,
                                     @RequestParam int pageSize,
                                     @RequestParam(required = false, defaultValue = "ID") SearchType.SERVICE_PRICE sortBy,
                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {

        Pageable paging;
        if(sortBy.equals("APPLYDATE")) {
            paging = util.makePaging(pageNo, pageSize, "applyDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(plantID != null) {
            return service.getAllByPlantID(plantID, paging);
        } else {
            return service.getAll(paging);
        }
    }

    @GetMapping(value = "/{plantPriceID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowPlantPriceModel getByID(@PathVariable("plantPriceID") String plantPriceID) {
        ShowPlantPriceModel model = service.getByID(plantPriceID);
        return model;
    }
}
