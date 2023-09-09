package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantShipPriceModels.CreatePlantShipPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.plantShipPrice.PlantShipPriceService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/plantShipPrice")
public class PlantShipPriceController {

    @Autowired
    private Util util;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PlantShipPriceService plantShipPriceService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPSP(@RequestBody CreatePlantShipPriceModel createPlantShipPriceModel,
                                            HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = plantShipPriceService.createPSP(createPlantShipPriceModel);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{plantShipPriceID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePSP(@PathVariable(name = "plantShipPriceID") String plantShipPriceID,
                                            HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = plantShipPriceService.deletePSP(plantShipPriceID);
        if(result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantShipPriceModel> getAllPSP(@RequestParam int pageNo,
                                            @RequestParam int pageSize,
                                            @RequestParam(required = false, defaultValue = "ID") SearchType.PSP sortBy,
                                            @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        if(sortBy.equals("POTSIZE")) {
            paging = util.makePaging(pageNo, pageSize, "potSize", sortAsc);
        } else if(sortBy.equals("PRICEPERPLANT")) {
            paging = util.makePaging(pageNo, pageSize, "pricePerPlant", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        return plantShipPriceService.getAllPlantShipPrice(paging);
    }
}
