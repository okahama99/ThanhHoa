package com.example.thanhhoa.services.cart;

import com.example.thanhhoa.dtos.CartModels.AddCartModel;
import com.example.thanhhoa.dtos.CartModels.ShowCartModel;
import com.example.thanhhoa.dtos.CartModels.UpdateCartModel;
import com.example.thanhhoa.entities.Cart;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CartRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private Util util;


    @Override
    public List<ShowCartModel> getCartByUserID(Long userID) {
        List<Cart> cartList = cartRepository.findByAccount_IdAndQuantityGreaterThan(userID, 0);
        if(cartList == null) {
            return null;
        }
        List<ShowCartModel> modelList = new ArrayList<>();
        for(Cart cart : cartList) {
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(cart.getPlant().getId(), Status.ACTIVE);
            ShowCartModel model = new ShowCartModel();
            model.setId(cart.getId());
            model.setPlantID(cart.getPlant().getId());
            model.setPlantName(cart.getPlant().getName());
            model.setQuantity(cart.getQuantity());
            model.setPlantPrice(newestPrice.getPrice());
            if(cart.getPlant().getPlantIMGList().size() != 0){
                model.setImage(cart.getPlant().getPlantIMGList().get(0).getImgURL());
            }
            model.setShipPrice(cart.getPlant().getPlantShipPrice().getPricePerPlant());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String addToCart(AddCartModel addCartModel, Long userID) {
        Cart checkExisted = cartRepository.findByPlant_IdAndAccount_Id(addCartModel.getPlantID(), userID);
        if(checkExisted != null){
            checkExisted.setQuantity(checkExisted.getQuantity()+addCartModel.getQuantity());
            if(checkExisted.getQuantity()<0){
                checkExisted.setQuantity(0);
            }
            cartRepository.save(checkExisted);
            return "Thêm thành công.";
        }
        tblAccount account = userRepository.getById(userID);
        Plant plant = plantRepository.getById(addCartModel.getPlantID());
        Cart cart = new Cart();
        Cart lastCart = cartRepository.findFirstByOrderByIdDesc();
        if(lastCart == null) {
            cart.setId(util.createNewID("CART"));
        } else {
            cart.setId(util.createIDFromLastID("CART", 4, lastCart.getId()));
        }
        cart.setQuantity(addCartModel.getQuantity());
        if(addCartModel.getQuantity()<0){
            cart.setQuantity(0);
        }
        cart.setAccount(account);
        cart.setPlant(plant);
        cartRepository.save(cart);
        return "Thêm thành công.";
    }

    @Override
    public String deleteItem(String cartID, Long userID) {
        Cart cart = cartRepository.findByAccount_IdAndId(userID, cartID);
        if(cart == null){
            return "Không tồn tại Cart nào với UserID " + userID + " và CartID " + cartID + ".";
        }
        cart.setQuantity(0);
        cartRepository.save(cart);
        return "Xóa thành công.";
    }

    @Override
    public String updateItem(UpdateCartModel updateCartModel, Long userID) {
        Cart cart = cartRepository.findByAccount_IdAndId(userID, updateCartModel.getCartID());
        if(cart == null){
            return "Không tồn tại Cart nào với UserID " + userID + " và CartID " + updateCartModel.getCartID() + ".";
        }
        if(updateCartModel.getQuantity() < 0){
            return "Quantity phải lớn hơn 0.";
        }
        cart.setQuantity(updateCartModel.getQuantity());
        cartRepository.save(cart);
        return "Chỉnh sửa thành công.";
    }
}
