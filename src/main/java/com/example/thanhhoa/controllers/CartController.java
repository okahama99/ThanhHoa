package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.CartModels.AddCartModel;
import com.example.thanhhoa.dtos.CartModels.ShowCartModel;
import com.example.thanhhoa.dtos.CartModels.UpdateCartModel;
import com.example.thanhhoa.services.cart.CartService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private CartService cartService;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowCartModel> getCart(HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromJWT(request);
        if (!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.substring(7);

        List<ShowCartModel> list = cartService.getCartByUsername(jwtUtil.getUserNameFromJWT(token));
        return list;
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addToCart(@RequestBody AddCartModel addCartModel,
                                            HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromJWT(request);
        if (!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.substring(7);
        String result = cartService.addToCart(addCartModel, jwtUtil.getUserNameFromJWT(token));
        if (result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateCart(@RequestBody UpdateCartModel updateCartModel,
                                            HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromJWT(request);
        if (!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.substring(7);
        String result = cartService.updateItem(updateCartModel, jwtUtil.getUserNameFromJWT(token));
        if (result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{cartID}",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteCart(@PathVariable(name = "cartID") String cartID,
                                             HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromJWT(request);
        if (!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.substring(7);
        String result = cartService.deleteItem(cartID, jwtUtil.getUserNameFromJWT(token));
        if (result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}
