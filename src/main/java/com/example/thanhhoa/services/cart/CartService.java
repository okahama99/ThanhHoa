package com.example.thanhhoa.services.cart;

import com.example.thanhhoa.dtos.CartModels.AddCartModel;
import com.example.thanhhoa.dtos.CartModels.ShowCartModel;
import com.example.thanhhoa.dtos.CartModels.UpdateCartModel;

import java.util.List;

public interface CartService {
    List<ShowCartModel> getCartByUsername(String username);

    String addToCart(AddCartModel addCartModel, String username);

    String deleteItem(String cartID, String username);

    String updateItem(UpdateCartModel updateCartModel, String username);


}
