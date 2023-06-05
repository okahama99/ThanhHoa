package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.services.plant.PlantService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/plant")
public class PlantController {

    @Autowired
    private PlantService plantService;
    @Autowired
    private Util util;

    //    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/createPlant", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPlant(@RequestBody CreatePlantModel createPlantModel,
                                              @RequestPart(required = false) @Size(min = 1) MultipartFile[] files) throws Exception {
        if (plantService.checkDuplicate(createPlantModel.getName()) != null) {
            return ResponseEntity.badRequest().body("Cây cùng tên đã tồn tại.");
        } else {
            boolean result = plantService.createPlant(createPlantModel, files);
            if (result) {
                return ResponseEntity.ok().body("Tạo thành công.");
            }
            return ResponseEntity.badRequest().body("Tạo thất bại.");
        }
    }

    //    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/updatePlant", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePlant(@RequestBody UpdatePlantModel updatePlantModel,
                                              @RequestPart(required = false) @Size(min = 1) MultipartFile[] files) throws Exception {
        if (plantService.checkDuplicate(updatePlantModel.getName()) != null) {
            return ResponseEntity.badRequest().body("Cây cùng tên đã tồn tại.");
        } else {
            boolean result = plantService.updatePlant(updatePlantModel, files);
            if (result) {
                return ResponseEntity.ok().body("Chỉnh sửa thành công.");
            }
            return ResponseEntity.badRequest().body("Chỉnh sửa thất bại.");
        }
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/deletePlant/{plantID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePlant(@PathVariable(name = "plantID") Long plantID){
        if(plantService.deletePlant(plantID))
        {
            return ResponseEntity.ok().body("Xóa cây thành công.");
        }else{
            return ResponseEntity.badRequest().body("Xóa cây thất bại.");
        }
    }

    @GetMapping(value = "/getAllPlant", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getAllPlant(@RequestParam int pageNo,
                                     @RequestParam int pageSize,
                                     @RequestParam String sortBy,
                                     @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getAllPlant(util.makePaging(pageNo,pageSize,sortBy,sortAsc));
        return plant;
    }
}
