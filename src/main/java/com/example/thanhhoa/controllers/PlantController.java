package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.plant.PlantService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
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
                                              HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        if (plantService.checkDuplicate(createPlantModel.getName()) != null) {
            return ResponseEntity.badRequest().body("Cây cùng tên đã tồn tại.");
        } else {
            String result = plantService.createPlant(createPlantModel);
            if (result.equals("Tạo thành công.")) {
                return ResponseEntity.ok().body(result);
            }
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePlant(@RequestBody UpdatePlantModel updatePlantModel,
                                              HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner") || !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        if (plantService.checkDuplicate(updatePlantModel.getName()) != null) {
            return ResponseEntity.badRequest().body("Cây cùng tên đã tồn tại.");
        } else {
            String result = plantService.updatePlant(updatePlantModel);
            if (result.equals("Chỉnh sửa thành công.")) {
                return ResponseEntity.ok().body(result);
            }
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping(value = "/{plantID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePlant(@PathVariable(name = "plantID") String plantID,
                                              HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        if (plantService.deletePlant(plantID)) {
            return ResponseEntity.ok().body("Xóa cây thành công.");
        } else {
            return ResponseEntity.badRequest().body("Xóa cây thất bại.");
        }
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowPlantModel> getAllPlant(@RequestParam int pageNo,
                                     @RequestParam int pageSize,
                                     @RequestParam SearchType.PLANT sortBy,
                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        return plantService.getAllPlant(util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/{plantID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getPlantByID(@PathVariable("plantID") String plantID) {
        ShowPlantModel model = plantService.getPlantByID(plantID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Cây không tồn tại.");
    }

    @GetMapping(value = "/plantFilter", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> searchPlant(@RequestParam(required = false) String plantName,
                                       @RequestParam(required = false) String categoryID,
                                       @RequestParam(required = false) Double fromPrice,
                                       @RequestParam(required = false) Double toPrice,
                                       @RequestParam int pageNo,
                                       @RequestParam int pageSize,
                                       @RequestParam SearchType.PLANT sortBy,
                                       @RequestParam(required = false, defaultValue = "true") boolean sortAsc) {
        Pageable paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);

        if (!StringUtils.isEmpty(plantName) && categoryID == null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByName(plantName, paging));
        } else if (StringUtils.isEmpty(plantName) && categoryID != null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategory(categoryID, paging));
        } else if (StringUtils.isEmpty(plantName) && categoryID == null
                && fromPrice != null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceMin(fromPrice, paging));
        } else if (StringUtils.isEmpty(plantName) && categoryID == null
                && fromPrice == null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceMax(toPrice, paging));
        } else if (StringUtils.isEmpty(plantName) && categoryID == null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getNameByPriceInRange(fromPrice, toPrice, paging));
        } else if (!StringUtils.isEmpty(plantName) && categoryID != null
                && fromPrice == null && toPrice == null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndName(categoryID, plantName, paging));
        } else if (!StringUtils.isEmpty(plantName) && categoryID == null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByNameAndPrice(plantName, fromPrice, toPrice, paging));
        } else if (StringUtils.isEmpty(plantName) && categoryID != null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndPrice(categoryID, fromPrice, toPrice, paging));
        } else if (!StringUtils.isEmpty(plantName) && categoryID != null
                && fromPrice != null && toPrice != null) {
            return ResponseEntity.ok().body(plantService.getPlantByCategoryAndNameAndPrice(categoryID, plantName, fromPrice, toPrice, paging));
        } else {
            return ResponseEntity.badRequest().body("Không thể tìm cây với giá trị đã nhập.");
        }
    }
}
