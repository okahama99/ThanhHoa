package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.plant.PlantService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPlant(@RequestBody CreatePlantModel createPlantModel,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid jwt.");
        }
        if (plantService.checkDuplicate(createPlantModel.getName()) != null) {
            return ResponseEntity.badRequest().body("Cây cùng tên đã tồn tại.");
        } else {
            boolean result = plantService.createPlant(createPlantModel);
            if (result) {
                return ResponseEntity.ok().body("Tạo thành công.");
            }
            return ResponseEntity.badRequest().body("Tạo thất bại.");
        }
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePlant(@RequestBody UpdatePlantModel updatePlantModel,
                                              @RequestPart(required = false) List<MultipartFile> files,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null) {
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

    @DeleteMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePlant(@PathVariable(name = "plantID") Long plantID,
                                              @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid jwt.");
        }
        if (plantService.deletePlant(plantID)) {
            return ResponseEntity.ok().body("Xóa cây thành công.");
        } else {
            return ResponseEntity.badRequest().body("Xóa cây thất bại.");
        }
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody List<ShowPlantModel> getAllPlant(@RequestParam int pageNo,
                                              @RequestParam int pageSize,
                                              @RequestParam SearchType.PLANT sortBy,
                                              @RequestParam boolean sortAsc) {
        return plantService.getAllPlant(util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/{plantID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getPlantByID(@PathVariable("plantID") Long plantID) {
        ShowPlantModel model = plantService.getPlantByID(plantID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Cây không tồn tại.");
    }

    @GetMapping(value = "/plantFilter", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> searchPlant(@RequestParam(required = false) String plantName,
                                       @RequestParam(required = false) Long categoryID,
                                       @RequestParam(required = false) Double fromPrice,
                                       @RequestParam(required = false) Double toPrice,
                                       @RequestParam int pageNo,
                                       @RequestParam int pageSize,
                                       @RequestParam SearchType.PLANT sortBy,
                                       @RequestParam(required = false, defaultValue = "true") boolean sortAsc) {
        Pageable paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);

        if (!StringUtils.isEmpty(plantName.trim()) && categoryID == null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByName(plantName, paging));
        } else if (StringUtils.isEmpty(plantName.trim()) && categoryID != null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategory(categoryID, paging));
        } else if (StringUtils.isEmpty(plantName.trim()) && categoryID == null
                && fromPrice != null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceMin(fromPrice, paging));
        } else if (StringUtils.isEmpty(plantName.trim()) && categoryID == null
                && fromPrice == null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceMax(toPrice, paging));
        } else if (StringUtils.isEmpty(plantName.trim()) && categoryID == null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceInRange(fromPrice, toPrice, paging));
        } else if (!StringUtils.isEmpty(plantName.trim()) && categoryID != null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndName(categoryID, plantName, paging));
        } else if (!StringUtils.isEmpty(plantName.trim()) && categoryID == null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByNameAndPrice(plantName, fromPrice, toPrice, paging));
        } else if (StringUtils.isEmpty(plantName.trim()) && categoryID != null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndPrice(categoryID, fromPrice, toPrice, paging));
        } else if (!StringUtils.isEmpty(plantName.trim()) && categoryID != null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndNameAndPrice(categoryID, plantName, fromPrice, toPrice, paging));
        } else {
            return ResponseEntity.badRequest().body("Không thể tìm cây với giá trị đã nhập.");
        }
    }
}
