package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ServicePriceModels.ShowServicePriceModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.servicePrice.ServicePriceService;
import com.example.thanhhoa.utils.JwtUtil;
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
@RequestMapping("/servicePrice")
public class ServicePriceController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private ServicePriceService service;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowServicePriceModel> getAll(@RequestParam(required = false) String serviceID,
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

        if(serviceID != null){
            return service.getAllServicePriceByServiceID(serviceID, paging);
        }else{
            return service.getAllServicePrice(paging);
        }
    }

    @GetMapping(value = "/{servicePriceID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowServicePriceModel getServicePriceByServiceID(@PathVariable("servicePriceID") String servicePriceID) {
        ShowServicePriceModel model = service.getByID(servicePriceID);
        return model;
    }
}
