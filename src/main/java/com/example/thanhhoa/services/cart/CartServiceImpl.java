package com.example.thanhhoa.services.cart;

import com.example.thanhhoa.dtos.CartModels.AddCartModel;
import com.example.thanhhoa.dtos.CartModels.ShowCartModel;
import com.example.thanhhoa.dtos.CartModels.UpdateCartModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Override
    public List<ShowCartModel> getCartByUsername(String username) {
        return null;
    }

    @Override
    public String addToCart(AddCartModel addCartModel, String username) {
        return null;
    }

    @Override
    public String deleteItem(String cartID, String username) {
        return null;
    }

    @Override
    public String updateItem(UpdateCartModel updateCartModel, String username) {
        return null;
    }
}
