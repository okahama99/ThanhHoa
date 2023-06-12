package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.plant.PlantService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/plant")
public class PlantController {

    @Autowired
    private PlantService plantService;
    @Autowired
    private Util util;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/createPlant", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPlant(@RequestBody CreatePlantModel createPlantModel,
                                              @RequestPart(required = false) @Size(min = 1) MultipartFile[] files,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
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

    @PutMapping(value = "/updatePlant", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePlant(@RequestBody UpdatePlantModel updatePlantModel,
                                              @RequestPart(required = false) @Size(min = 1) MultipartFile[] files,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
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

    @DeleteMapping(value = "/deletePlant/{plantID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePlant(@PathVariable(name = "plantID") Long plantID,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
        if (plantService.deletePlant(plantID)) {
            return ResponseEntity.ok().body("Xóa cây thành công.");
        } else {
            return ResponseEntity.badRequest().body("Xóa cây thất bại.");
        }
    }

    @GetMapping(value = "/getAllPlant", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getAllPlant(@RequestParam int pageNo,
                                     @RequestParam int pageSize,
                                     @RequestParam SearchType.PLANT sortBy,
                                     @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getAllPlant(util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByCategory", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByCategory(@RequestParam List<Long> categoryIDList,
                                            @RequestParam int pageNo,
                                            @RequestParam int pageSize,
                                            @RequestParam SearchType.PLANT sortBy,
                                            @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByCategory(categoryIDList, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByName", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByName(@RequestParam String name,
                                        @RequestParam int pageNo,
                                        @RequestParam int pageSize,
                                        @RequestParam SearchType.PLANT sortBy,
                                        @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByName(name, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getNameByPrice", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getNameByPrice(@RequestParam Double fromPrice,
                                        @RequestParam Double toPrice,
                                        @RequestParam int pageNo,
                                        @RequestParam int pageSize,
                                        @RequestParam SearchType.PLANT sortBy,
                                        @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getNameByPrice(fromPrice, toPrice, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByCategoryAndName", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByCategoryAndName(@RequestParam List<Long> categoryIDList,
                                                   @RequestParam String name,
                                                   @RequestParam int pageNo,
                                                   @RequestParam int pageSize,
                                                   @RequestParam SearchType.PLANT sortBy,
                                                   @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByCategoryAndName(categoryIDList, name, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByNameAndPrice", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByNameAndPrice(@RequestParam String name,
                                                @RequestParam Double fromPrice,
                                                @RequestParam Double toPrice,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam SearchType.PLANT sortBy,
                                                @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByNameAndPrice(name, fromPrice, toPrice, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByCategoryAndPrice", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByCategoryAndPrice(@RequestParam List<Long> categoryIDList,
                                                   @RequestParam Double fromPrice,
                                                   @RequestParam Double toPrice,
                                                   @RequestParam int pageNo,
                                                   @RequestParam int pageSize,
                                                   @RequestParam SearchType.PLANT sortBy,
                                                   @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByCategoryAndPrice(categoryIDList, fromPrice, toPrice, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }

    @GetMapping(value = "/getPlantByCategoryAndNameAndPrice", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(@RequestParam List<Long> categoryIDList,
                                                    @RequestParam String name,
                                                    @RequestParam Double fromPrice,
                                                    @RequestParam Double toPrice,
                                                    @RequestParam int pageNo,
                                                    @RequestParam int pageSize,
                                                    @RequestParam SearchType.PLANT sortBy,
                                                    @RequestParam boolean sortAsc) {
        List<ShowPlantModel> plant = plantService.getPlantByCategoryAndNameAndPrice(categoryIDList, name, fromPrice, toPrice, util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
        return plant;
    }
}
