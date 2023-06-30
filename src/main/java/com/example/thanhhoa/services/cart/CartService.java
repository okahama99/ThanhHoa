package com.example.thanhhoa.services.cart;

import com.example.thanhhoa.dtos.CartModels.AddCartModel;
import com.example.thanhhoa.dtos.CartModels.ShowCartModel;
import com.example.thanhhoa.dtos.CartModels.UpdateCartModel;

import java.util.List;

public interface CartService {
    List<ShowCartModel> getCartByUserID(Long userID);

    String addToCart(AddCartModel addCartModel, Long userID);

    String deleteItem(String cartID, Long userID);

    String updateItem(UpdateCartModel updateCartModel, Long userID);


}
