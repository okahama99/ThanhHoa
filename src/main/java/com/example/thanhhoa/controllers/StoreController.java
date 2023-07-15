package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.CreateStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowDistrictModel;
import com.example.thanhhoa.dtos.StoreModels.ShowProvinceModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantRecordModel;
import com.example.thanhhoa.dtos.StoreModels.UpdateStoreModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.store.StoreService;
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
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private StoreService storeService;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowStoreModel> getAllStore() {
        return storeService.getAllStore();
    }

    @GetMapping(value = "/getStorePlant/{storeID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getStorePlantByStoreID(@PathVariable("storeID") String storeID,
                                                  @RequestParam int pageNo,
                                                  @RequestParam int pageSize,
                                                  @RequestParam(required = false, defaultValue = "ID") SearchType.STORE sortBy,
                                                  @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        if(sortBy.equals("STORENAME")) {
            paging = util.makePaging(pageNo, pageSize, "storeName", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        return ResponseEntity.ok().body(storeService.getStorePlantByStoreID(storeID, paging));
    }

    @PostMapping(value = "/addStorePlant", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addStorePlant(@RequestBody AddStorePlantModel addStorePlantModel,
                                                HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.addStorePlant(addStorePlantModel);
        if(result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping(value = "/addStoreEmployee", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addStoreEmployee(@RequestBody AddStoreEmployeeModel addStoreEmployeeModel,
                                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.addStoreEmployee(addStoreEmployeeModel);
        if(result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/employee/{employeeID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(name = "employeeID") Long employeeID,
                                                 HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.removeStoreEmployee(employeeID);
        if(!result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(value = "/getStorePlantRecord/{storePlantID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowStorePlantRecordModel> getStorePlantRecordByStorePlantID(@PathVariable("storePlantID") String storePlantID,
                                                                      HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return storeService.getStorePlantRecordByStorePlantID(storePlantID);
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> create(@RequestBody CreateStoreModel createStoreModel,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.createStore(createStoreModel);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> update(@RequestBody UpdateStoreModel updateStoreModel,
                                         HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.updateStore(updateStoreModel);
        if(result.equals("Cập nhật thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{storeID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteService(@PathVariable(name = "storeID") String storeID,
                                                HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = storeService.deleteStore(storeID);
        if(!result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(value = "/getByID", produces = "application/json;charset=UTF-8")
    public ShowStoreModel getByID(@RequestParam String storeID) {
        return storeService.getByID(storeID);
    }

    @GetMapping(value = "/getAllProvince", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowProvinceModel> getAllProvince() {
        return storeService.getAllProvince();
    }

    @GetMapping(value = "/getDistrictByProvinceID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowDistrictModel> getDistrictByProvinceID(@RequestParam String provinceID) {
        return storeService.getDistrictByProvinceID(provinceID);
    }
}
