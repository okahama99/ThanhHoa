package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.category.CategoryService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowCategoryModel> getCategory() {
        List<ShowCategoryModel> list = categoryService.getCategory();
        return list;
    }

    @GetMapping(value = "/getCategoryByID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowCategoryModel getCategoryByID(@RequestParam String categoryID) {
        ShowCategoryModel model = categoryService.getCategoryByID(categoryID);
        return model;
    }

    @GetMapping(value = "/getCategoryPaging", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getCategoryPaging(@RequestParam int pageNo,
                                             @RequestParam int pageSize,
                                             @RequestParam(required = false, defaultValue = "ID") SearchType.CATEGORY sortBy,
                                             @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);

        return ResponseEntity.ok().body(categoryService.getAllCategory(paging));
    }

    @PutMapping(value = "/v2/activateCategory", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> activateCategory(@RequestParam String categoryID,
                                                   HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = categoryService.activateCategory(categoryID);
        if(result.equals("Cập nhật thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> create(@RequestParam String name,
                                         HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = categoryService.create(name);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> update(@RequestParam String id,
                                         @RequestParam String name,
                                         HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = categoryService.update(id, name);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}
