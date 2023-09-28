package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.CreateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.CreateOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateOrderFeedbackModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowDistancePriceModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.Rating;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractFeedbackRepository;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderDetailRepository;
import com.example.thanhhoa.repositories.OrderFeedbackIMGRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.RatingRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.ContractFeedbackPagingRepository;
import com.example.thanhhoa.repositories.pagings.OrderFeedbackPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private OrderFeedbackRepository orderFeedbackRepository;
    @Autowired
    private OrderFeedbackPagingRepository orderFeedbackPagingRepository;
    @Autowired
    private ContractFeedbackRepository contractFeedbackRepository;
    @Autowired
    private ContractFeedbackPagingRepository contractFeedbackPagingRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private OrderFeedbackIMGRepository orderFeedbackIMGRepository;
    @Autowired
    private Util util;
    @Autowired
    private PlantPriceRepository plantPriceRepository;


    @Override
    public String createOrder(CreateOrderFeedbackModel createOrderFeedbackModel, Long userID) {
        Optional<Plant> plant = plantRepository.findById(createOrderFeedbackModel.getPlantID());
        if(plant == null) {
            return "Không tìm thấy Cây với ID là " + createOrderFeedbackModel.getPlantID() + ".";
        }
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(createOrderFeedbackModel.getOrderDetailID());
        if(orderDetail == null) {
            return "Không tìm thấy Chi tiết Order với ID là " + createOrderFeedbackModel.getOrderDetailID() + ".";
        }

        OrderFeedback orderFeedback = new OrderFeedback();
        OrderFeedback lastOrderFeedback = orderFeedbackRepository.findFirstByOrderByIdDesc();
        if(lastOrderFeedback == null) {
            orderFeedback.setId(util.createNewID("OF"));
        } else {
            orderFeedback.setId(util.createIDFromLastID("OF", 2, lastOrderFeedback.getId()));
        }
        orderFeedback.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        orderFeedback.setStatus(Status.ACTIVE);
        orderFeedback.setCustomer(userRepository.getById(userID));
        orderFeedback.setDescription(createOrderFeedbackModel.getDescription());
        orderFeedback.setPlant(plant.get());
        orderFeedback.setRating(ratingRepository.getById(createOrderFeedbackModel.getRatingID()));
        orderDetail.get().setIsFeedback(true);
        orderFeedback.setOrderDetail(orderDetail.get());

        if(orderFeedback.getOrderFeedbackIMGList() != null){
            for(OrderFeedbackIMG image : orderFeedback.getOrderFeedbackIMGList()) {
                orderFeedbackIMGRepository.deleteById(image.getId());
            }
        }

        for(String imageURL : createOrderFeedbackModel.getListURL()) {
            OrderFeedbackIMG orderFeedbackIMG = new OrderFeedbackIMG();
            OrderFeedbackIMG lastOrderFeedbackIMG = orderFeedbackIMGRepository.findFirstByOrderByIdDesc();
            if(lastOrderFeedbackIMG == null) {
                orderFeedbackIMG.setId(util.createNewID("OFIMG"));
            } else {
                orderFeedbackIMG.setId(util.createIDFromLastID("OFIMG", 5, lastOrderFeedbackIMG.getId()));
            }
            orderFeedbackIMG.setOrderFeedback(orderFeedback);
            orderFeedbackIMG.setImgURL(imageURL);
            orderFeedbackIMGRepository.save(orderFeedbackIMG);
        }
        orderFeedbackRepository.save(orderFeedback);
        return "Tạo thành công.";
    }

    @Override
    public String updateOrder(UpdateOrderFeedbackModel updateOrderFeedbackModel) {
        Optional<OrderFeedback> checkExisted = orderFeedbackRepository.findById(updateOrderFeedbackModel.getId());
        if(checkExisted == null) {
            return "Không tìm thấy Order Feedback với ID là " + updateOrderFeedbackModel.getId() + ".";
        }
        Optional<Plant> plant = plantRepository.findById(updateOrderFeedbackModel.getPlantID());
        if(plant == null) {
            return "Không tìm thấy Cây với ID là " + updateOrderFeedbackModel.getPlantID() + ".";
        }
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(updateOrderFeedbackModel.getOrderDetailID());
        if(orderDetail == null) {
            return "Không tìm thấy Chi tiết Order với ID là " + updateOrderFeedbackModel.getOrderDetailID() + ".";
        }
        OrderFeedback orderFeedback = checkExisted.get();
        orderFeedback.setDescription(updateOrderFeedbackModel.getDescription());
        orderFeedback.setPlant(plant.get());
        orderFeedback.setRating(ratingRepository.getById(updateOrderFeedbackModel.getRatingID()));
        orderDetail.get().setIsFeedback(true);
        orderFeedback.setOrderDetail(orderDetail.get());

        if(orderFeedback.getOrderFeedbackIMGList() != null){
            for(OrderFeedbackIMG image : orderFeedback.getOrderFeedbackIMGList()) {
                orderFeedbackIMGRepository.deleteById(image.getId());
            }
        }

        for(String imageURL : updateOrderFeedbackModel.getListURL()) {
            OrderFeedbackIMG orderFeedbackIMG = new OrderFeedbackIMG();
            OrderFeedbackIMG lastOrderFeedbackIMG = orderFeedbackIMGRepository.findFirstByOrderByIdDesc();
            if(lastOrderFeedbackIMG == null) {
                orderFeedbackIMG.setId(util.createNewID("OFIMG"));
            } else {
                orderFeedbackIMG.setId(util.createIDFromLastID("OFIMG", 5, lastOrderFeedbackIMG.getId()));
            }
            orderFeedbackIMG.setOrderFeedback(orderFeedback);
            orderFeedbackIMG.setImgURL(imageURL);
            orderFeedbackIMGRepository.save(orderFeedbackIMG);
        }
        orderFeedbackRepository.save(orderFeedback);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteOrder(String id) {
        Optional<OrderFeedback> checkExisted = orderFeedbackRepository.findById(id);
        if(checkExisted == null) {
            return "Không tìm thấy Order Feedback với ID là " + id + ".";
        }
        OrderFeedback orderFeedback = checkExisted.get();
        orderFeedback.getOrderDetail().setIsFeedback(false);
        orderFeedback.setStatus(Status.INACTIVE);
        orderFeedbackRepository.save(orderFeedback);
        orderDetailRepository.save(orderFeedback.getOrderDetail());
        return "Xóa thành công.";
    }

    @Override
    public List<ShowOrderFeedbackModel> getAllOrderFeedback(Long userID, Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findAllByStatusAndCustomer_Id(Status.ACTIVE, userID, pageable);
        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderFeedbackModel> getFeedbackForManagerOwner(Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findAll(pageable);
        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByID(String orderFeedbackID) {
        Optional<OrderFeedback> searchResult = orderFeedbackRepository.findById(orderFeedbackID);
        if(searchResult == null) {
            return null;
        }
        OrderFeedback orderFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for(OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
            imgModel.setId(img.getId());
            imgModel.setUrl(img.getImgURL());
            imgModelList.add(imgModel);
        }

        //customer
        ShowCustomerModel customerModel = new ShowCustomerModel();
        customerModel.setId(orderFeedback.getCustomer().getId());
        customerModel.setAddress(orderFeedback.getCustomer().getAddress());
        customerModel.setEmail(orderFeedback.getCustomer().getEmail());
        customerModel.setPhone(orderFeedback.getCustomer().getPhone());
        customerModel.setFullName(orderFeedback.getCustomer().getFullName());
        customerModel.setAvatar(orderFeedback.getCustomer().getAvatar());

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        plantModel.setId(orderFeedback.getPlant().getId());
        if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setPlantName(orderFeedback.getPlant().getName());

        // start of order
        tblOrder order = orderFeedback.getOrderDetail().getTblOrder();
        ShowOrderModel orderModel = new ShowOrderModel();
        orderModel.setId(order.getId());
        orderModel.setFullName(order.getFullName());
        orderModel.setAddress(order.getAddress());
        orderModel.setEmail(order.getEmail());
        orderModel.setPhone(order.getPhone());
        orderModel.setCreatedDate(order.getCreatedDate());
        orderModel.setPackageDate(order.getPackageDate());
        orderModel.setDeliveryDate(order.getDeliveryDate());
        orderModel.setReceivedDate(order.getReceivedDate());
        orderModel.setApproveDate(order.getApproveDate());
        orderModel.setRejectDate(order.getRejectDate());
        orderModel.setPaymentMethod(order.getPaymentMethod());
        orderModel.setProgressStatus(order.getProgressStatus());
        orderModel.setReason(order.getReason());
        orderModel.setLatLong(order.getLatLong());
        orderModel.setDistance(order.getDistance());
        orderModel.setTotalShipCost(order.getTotalShipCost());
        orderModel.setTotal(order.getTotal());
        orderModel.setIsPaid(order.getIsPaid());
        orderModel.setIsRefund(order.getIsRefund());
        orderModel.setReceiptIMG(order.getReceiptIMG());

        //store
        ShowStoreModel storeModel = new ShowStoreModel();
        storeModel.setId(order.getStore().getId());
        storeModel.setStoreName(order.getStore().getStoreName());
        storeModel.setAddress(order.getStore().getAddress());
        storeModel.setPhone(order.getStore().getPhone());

        //staff
        ShowStaffModel staffModel = new ShowStaffModel();
        if(order.getStaff() != null) {
            staffModel.setId(order.getStaff().getId());
            staffModel.setAddress(order.getStaff().getAddress());
            staffModel.setEmail(order.getStaff().getEmail());
            staffModel.setPhone(order.getStaff().getPhone());
            staffModel.setFullName(order.getStaff().getFullName());
            staffModel.setAvatar(order.getStaff().getAvatar());
        }

//                    //customer
//                    ShowCustomerModel customerModel = new ShowCustomerModel();
//                    if(order.getCustomer() != null) {
//                        customerModel.setId(order.getCustomer().getId());
//                        customerModel.setAddress(order.getCustomer().getAddress());
//                        customerModel.setEmail(order.getCustomer().getEmail());
//                        customerModel.setPhone(order.getCustomer().getPhone());
//                        customerModel.setFullName(order.getCustomer().getFullName());
//                    }

        //distance price
        ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
        distancePriceModel.setId(order.getDistancePrice().getId());
        distancePriceModel.setApplyDate(order.getDistancePrice().getApplyDate());
        distancePriceModel.setPricePerKm(order.getDistancePrice().getPricePerKm());

        //plant
        List<com.example.thanhhoa.dtos.OrderModels.ShowPlantModel> listPlantModel = new ArrayList<>();
        for(OrderDetail detail : order.getOrderDetailList()) {
            com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plant = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(detail.getStorePlant().getPlant().getId(), Status.ACTIVE);
            plant.setId(detail.getStorePlant().getPlant().getId());
            if(detail.getStorePlant().getPlant().getPlantIMGList() != null && !detail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
                plant.setImage(detail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
            }
            plant.setQuantity(detail.getQuantity());
            plant.setPlantName(detail.getStorePlant().getPlant().getName());
            plant.setPlantPriceID(newestPrice.getId());
            plant.setPlantPrice(newestPrice.getPrice());
            plant.setShipPrice(detail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());
            listPlantModel.add(plant);
        }

        orderModel.setShowPlantModel(listPlantModel);
        orderModel.setShowStaffModel(staffModel);
        orderModel.setShowStoreModel(storeModel);
        //orderModel.setShowCustomerModel(customerModel);
        orderModel.setShowDistancePriceModel(distancePriceModel);
        orderModel.setNumOfPlant(order.getOrderDetailList().size());
        // end of order model

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setShowCustomerModel(customerModel);
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
        model.setShowPlantModel(plantModel);
        model.setShowOrderModel(orderModel);
        util.getSetRatingFeedbackForModel(orderFeedback, model);
        return model;
    }

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByUsername(String username) {
        Optional<OrderFeedback> searchResult = orderFeedbackRepository.findByCustomer_UsernameAndStatus(username, Status.ACTIVE);
        if(searchResult == null) {
            return null;
        }
        OrderFeedback orderFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for(OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
            imgModel.setId(img.getId());
            imgModel.setUrl(img.getImgURL());
            imgModelList.add(imgModel);
        }

        //customer
        ShowCustomerModel customerModel = new ShowCustomerModel();
        customerModel.setId(orderFeedback.getCustomer().getId());
        customerModel.setAddress(orderFeedback.getCustomer().getAddress());
        customerModel.setEmail(orderFeedback.getCustomer().getEmail());
        customerModel.setPhone(orderFeedback.getCustomer().getPhone());
        customerModel.setFullName(orderFeedback.getCustomer().getFullName());
        customerModel.setAvatar(orderFeedback.getCustomer().getAvatar());

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        plantModel.setId(orderFeedback.getPlant().getId());
        if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setPlantName(orderFeedback.getPlant().getName());

        // start of order
        tblOrder order = orderFeedback.getOrderDetail().getTblOrder();
        ShowOrderModel orderModel = new ShowOrderModel();
        orderModel.setId(order.getId());
        orderModel.setFullName(order.getFullName());
        orderModel.setAddress(order.getAddress());
        orderModel.setEmail(order.getEmail());
        orderModel.setPhone(order.getPhone());
        orderModel.setCreatedDate(order.getCreatedDate());
        orderModel.setPackageDate(order.getPackageDate());
        orderModel.setDeliveryDate(order.getDeliveryDate());
        orderModel.setReceivedDate(order.getReceivedDate());
        orderModel.setApproveDate(order.getApproveDate());
        orderModel.setRejectDate(order.getRejectDate());
        orderModel.setPaymentMethod(order.getPaymentMethod());
        orderModel.setProgressStatus(order.getProgressStatus());
        orderModel.setReason(order.getReason());
        orderModel.setLatLong(order.getLatLong());
        orderModel.setDistance(order.getDistance());
        orderModel.setTotalShipCost(order.getTotalShipCost());
        orderModel.setTotal(order.getTotal());
        orderModel.setIsPaid(order.getIsPaid());
        orderModel.setIsRefund(order.getIsRefund());
        orderModel.setReceiptIMG(order.getReceiptIMG());

        //store
        ShowStoreModel storeModel = new ShowStoreModel();
        storeModel.setId(order.getStore().getId());
        storeModel.setStoreName(order.getStore().getStoreName());
        storeModel.setAddress(order.getStore().getAddress());
        storeModel.setPhone(order.getStore().getPhone());

        //staff
        ShowStaffModel staffModel = new ShowStaffModel();
        if(order.getStaff() != null) {
            staffModel.setId(order.getStaff().getId());
            staffModel.setAddress(order.getStaff().getAddress());
            staffModel.setEmail(order.getStaff().getEmail());
            staffModel.setPhone(order.getStaff().getPhone());
            staffModel.setFullName(order.getStaff().getFullName());
            staffModel.setAvatar(order.getStaff().getAvatar());
        }

//                    //customer
//                    ShowCustomerModel customerModel = new ShowCustomerModel();
//                    if(order.getCustomer() != null) {
//                        customerModel.setId(order.getCustomer().getId());
//                        customerModel.setAddress(order.getCustomer().getAddress());
//                        customerModel.setEmail(order.getCustomer().getEmail());
//                        customerModel.setPhone(order.getCustomer().getPhone());
//                        customerModel.setFullName(order.getCustomer().getFullName());
//                    }

        //distance price
        ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
        distancePriceModel.setId(order.getDistancePrice().getId());
        distancePriceModel.setApplyDate(order.getDistancePrice().getApplyDate());
        distancePriceModel.setPricePerKm(order.getDistancePrice().getPricePerKm());

        //plant
        List<com.example.thanhhoa.dtos.OrderModels.ShowPlantModel> listPlantModel = new ArrayList<>();
        for(OrderDetail detail : order.getOrderDetailList()) {
            com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plant = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(detail.getStorePlant().getPlant().getId(), Status.ACTIVE);
            plant.setId(detail.getStorePlant().getPlant().getId());
            if(detail.getStorePlant().getPlant().getPlantIMGList() != null && !detail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
                plant.setImage(detail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
            }
            plant.setQuantity(detail.getQuantity());
            plant.setPlantName(detail.getStorePlant().getPlant().getName());
            plant.setPlantPriceID(newestPrice.getId());
            plant.setPlantPrice(newestPrice.getPrice());
            plant.setShipPrice(detail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());
            listPlantModel.add(plant);
        }

        orderModel.setShowPlantModel(listPlantModel);
        orderModel.setShowStaffModel(staffModel);
        orderModel.setShowStoreModel(storeModel);
        //orderModel.setShowCustomerModel(customerModel);
        orderModel.setShowDistancePriceModel(distancePriceModel);
        orderModel.setNumOfPlant(order.getOrderDetailList().size());
        // end of order model

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setShowCustomerModel(customerModel);
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
        model.setShowPlantModel(plantModel);
        model.setShowOrderModel(orderModel);
        util.getSetRatingFeedbackForModel(orderFeedback, model);
        return model;
    }

    @Override
    public List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(String plantID, String role, Pageable pageable) {
        Page<OrderFeedback> pagingResult;
        if(role.equalsIgnoreCase("CUSTOMER") || role.equalsIgnoreCase("STAFF")){
            pagingResult = orderFeedbackPagingRepository.findByPlant_IdAndStatus(plantID, Status.ACTIVE, pageable);
        }else{
            pagingResult = orderFeedbackPagingRepository.findByPlant_Id(plantID, pageable);
        }

        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderFeedbackModel> getOrderFeedbackByRatingID(String ratingID, String role, Pageable pageable) {
        Page<OrderFeedback> pagingResult;
        if(role.equalsIgnoreCase("CUSTOMER") || role.equalsIgnoreCase("STAFF")){
            pagingResult = orderFeedbackPagingRepository.findByRating_IdAndStatus(ratingID, Status.ACTIVE, pageable);
        }else{
            pagingResult = orderFeedbackPagingRepository.findByRating_Id(ratingID, pageable);
        }
        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByOrderDetailID(String orderDetailID) {
        OrderFeedback orderFeedback = orderFeedbackRepository.findByOrderDetail_Id(orderDetailID);
        if(orderFeedback == null) {
            return null;
        }
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for(OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
            imgModel.setId(img.getId());
            imgModel.setUrl(img.getImgURL());
            imgModelList.add(imgModel);
        }

        //customer
        ShowCustomerModel customerModel = new ShowCustomerModel();
        customerModel.setId(orderFeedback.getCustomer().getId());
        customerModel.setAddress(orderFeedback.getCustomer().getAddress());
        customerModel.setEmail(orderFeedback.getCustomer().getEmail());
        customerModel.setPhone(orderFeedback.getCustomer().getPhone());
        customerModel.setFullName(orderFeedback.getCustomer().getFullName());
        customerModel.setAvatar(orderFeedback.getCustomer().getAvatar());

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        plantModel.setId(orderFeedback.getPlant().getId());
        if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setPlantName(orderFeedback.getPlant().getName());

        // start of order
        tblOrder order = orderFeedback.getOrderDetail().getTblOrder();
        ShowOrderModel orderModel = new ShowOrderModel();
        orderModel.setId(order.getId());
        orderModel.setFullName(order.getFullName());
        orderModel.setAddress(order.getAddress());
        orderModel.setEmail(order.getEmail());
        orderModel.setPhone(order.getPhone());
        orderModel.setCreatedDate(order.getCreatedDate());
        orderModel.setPackageDate(order.getPackageDate());
        orderModel.setDeliveryDate(order.getDeliveryDate());
        orderModel.setReceivedDate(order.getReceivedDate());
        orderModel.setApproveDate(order.getApproveDate());
        orderModel.setRejectDate(order.getRejectDate());
        orderModel.setPaymentMethod(order.getPaymentMethod());
        orderModel.setProgressStatus(order.getProgressStatus());
        orderModel.setReason(order.getReason());
        orderModel.setLatLong(order.getLatLong());
        orderModel.setDistance(order.getDistance());
        orderModel.setTotalShipCost(order.getTotalShipCost());
        orderModel.setTotal(order.getTotal());
        orderModel.setIsPaid(order.getIsPaid());
        orderModel.setIsRefund(order.getIsRefund());
        orderModel.setReceiptIMG(order.getReceiptIMG());

        //store
        ShowStoreModel storeModel = new ShowStoreModel();
        storeModel.setId(order.getStore().getId());
        storeModel.setStoreName(order.getStore().getStoreName());
        storeModel.setAddress(order.getStore().getAddress());
        storeModel.setPhone(order.getStore().getPhone());

        //staff
        ShowStaffModel staffModel = new ShowStaffModel();
        if(order.getStaff() != null) {
            staffModel.setId(order.getStaff().getId());
            staffModel.setAddress(order.getStaff().getAddress());
            staffModel.setEmail(order.getStaff().getEmail());
            staffModel.setPhone(order.getStaff().getPhone());
            staffModel.setFullName(order.getStaff().getFullName());
            staffModel.setAvatar(order.getStaff().getAvatar());
        }

//                    //customer
//                    ShowCustomerModel customerModel = new ShowCustomerModel();
//                    if(order.getCustomer() != null) {
//                        customerModel.setId(order.getCustomer().getId());
//                        customerModel.setAddress(order.getCustomer().getAddress());
//                        customerModel.setEmail(order.getCustomer().getEmail());
//                        customerModel.setPhone(order.getCustomer().getPhone());
//                        customerModel.setFullName(order.getCustomer().getFullName());
//                    }

        //distance price
        ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
        distancePriceModel.setId(order.getDistancePrice().getId());
        distancePriceModel.setApplyDate(order.getDistancePrice().getApplyDate());
        distancePriceModel.setPricePerKm(order.getDistancePrice().getPricePerKm());

        //plant
        List<com.example.thanhhoa.dtos.OrderModels.ShowPlantModel> listPlantModel = new ArrayList<>();
        for(OrderDetail detail : order.getOrderDetailList()) {
            com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plant = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(detail.getStorePlant().getPlant().getId(), Status.ACTIVE);
            plant.setId(detail.getStorePlant().getPlant().getId());
            if(detail.getStorePlant().getPlant().getPlantIMGList() != null && !detail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
                plant.setImage(detail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
            }
            plant.setQuantity(detail.getQuantity());
            plant.setPlantName(detail.getStorePlant().getPlant().getName());
            plant.setPlantPriceID(newestPrice.getId());
            plant.setPlantPrice(newestPrice.getPrice());
            plant.setShipPrice(detail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());
            listPlantModel.add(plant);
        }

        orderModel.setShowPlantModel(listPlantModel);
        orderModel.setShowStaffModel(staffModel);
        orderModel.setShowStoreModel(storeModel);
        //orderModel.setShowCustomerModel(customerModel);
        orderModel.setShowDistancePriceModel(distancePriceModel);
        orderModel.setNumOfPlant(order.getOrderDetailList().size());
        // end of order model

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setShowCustomerModel(customerModel);
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
        model.setShowPlantModel(plantModel);
        model.setShowOrderModel(orderModel);
        util.getSetRatingFeedbackForModel(orderFeedback, model);
        return model;
    }

    // -------------------------------------------- CONTRACT -----------------------------------------

    @Override
    public String createContract(CreateContractFeedbackModel createContractFeedbackModel, Long userID) {
        Optional<Contract> contract = contractRepository.findById(createContractFeedbackModel.getContractID());
        if(contract == null) {
            return "Không tìm thấy Hợp đồng với ID là " + createContractFeedbackModel.getContractID() + ".";
        }
        ContractFeedback contractFeedback = new ContractFeedback();
        ContractFeedback lastContractFeedback = contractFeedbackRepository.findFirstByOrderByIdDesc();
        if(lastContractFeedback == null) {
            contractFeedback.setId(util.createNewID("CF"));
        } else {
            contractFeedback.setId(util.createIDFromLastID("CF", 2, lastContractFeedback.getId()));
        }
        contract.get().setIsFeedback(true);
        contractFeedback.setContract(contract.get());
        contractFeedback.setStatus(Status.ACTIVE);
        contractFeedback.setDescription(createContractFeedbackModel.getDescription());
        contractFeedback.setRating(ratingRepository.getById(createContractFeedbackModel.getRatingID()));
        contractFeedback.setDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contractFeedbackRepository.save(contractFeedback);
        contractRepository.save(contract.get());
        return "Tạo thành công.";
    }

    @Override
    public String updateContract(UpdateContractFeedbackModel updateContractFeedbackModel) {
        Optional<ContractFeedback> checkExisted = contractFeedbackRepository.findById(updateContractFeedbackModel.getId());
        if(checkExisted == null) {
            return "Không tìm thấy Feedback Hợp đồng với ID là " + updateContractFeedbackModel.getId() + ".";
        }
        Optional<Contract> contract = contractRepository.findById(updateContractFeedbackModel.getContractID());
        if(contract == null) {
            return "Không tìm thấy Hợp đồng với ID là " + updateContractFeedbackModel.getContractID() + ".";
        }
        ContractFeedback contractFeedback = checkExisted.get();
        contract.get().setIsFeedback(true);
        contractFeedback.setContract(contract.get());
        contractFeedback.setDescription(updateContractFeedbackModel.getDescription());
        contractFeedback.setRating(ratingRepository.getById(updateContractFeedbackModel.getRatingID()));
        contractFeedbackRepository.save(contractFeedback);
        contractRepository.save(contract.get());
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteContract(String id) {
        Optional<ContractFeedback> checkExisted = contractFeedbackRepository.findById(id);
        if(checkExisted == null) {
            return "Không tìm thấy Feedback Hợp đồng với ID là " + id + ".";
        }
        ContractFeedback contractFeedback = checkExisted.get();
        contractFeedback.setStatus(Status.INACTIVE);
        contractFeedbackRepository.save(contractFeedback);
        return "Xóa thành công.";
    }

    @Override
    public List<ShowContractFeedbackModel> getAllContractFeedback(Pageable pageable) {
        Page<ContractFeedback> pagingResult = contractFeedbackPagingRepository.findAllByStatus(Status.ACTIVE, pageable);
        return util.contractFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowContractFeedbackModel getContractFeedbackByID(String contractFeedbackID) {
        Optional<ContractFeedback> searchResult = contractFeedbackRepository.findById(contractFeedbackID);
        if(searchResult == null) {
            return null;
        }
        ContractFeedback contractFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(contractFeedback.getRating().getId());
        ratingModel.setDescription(contractFeedback.getRating().getDescription());

        ShowContractFeedbackModel model = new ShowContractFeedbackModel();
        model.setContractFeedbackID(contractFeedback.getId());
        model.setDescription(contractFeedback.getDescription());
        model.setCreatedDate(contractFeedback.getDate());
        model.setRatingModel(ratingModel);
        model.setStatus(contractFeedback.getStatus());
        return model;
    }

    @Override
    public ShowContractFeedbackModel getContractFeedbackByContractID(String contractID) {
        Optional<ContractFeedback> searchResult = contractFeedbackRepository.findByContract_Id(contractID);
        if(searchResult == null) {
            return null;
        }
        ContractFeedback contractFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(contractFeedback.getRating().getId());
        ratingModel.setDescription(contractFeedback.getRating().getDescription());

        ShowContractFeedbackModel model = new ShowContractFeedbackModel();
        model.setContractFeedbackID(contractFeedback.getId());
        model.setDescription(contractFeedback.getDescription());
        model.setCreatedDate(contractFeedback.getDate());
        model.setRatingModel(ratingModel);
        model.setStatus(contractFeedback.getStatus());
        return model;
    }

    @Override
    public List<ShowRatingModel> getRating() {
        List<Rating> ratingList = ratingRepository.findAll();
        if(ratingList == null) {
            return null;
        }
        List<ShowRatingModel> modelList = new ArrayList<>();
        for(Rating rating : ratingList) {
            ShowRatingModel model = new ShowRatingModel();
            model.setId(rating.getId());
            model.setDescription(rating.getDescription());
            modelList.add(model);
        }
        return modelList;
    }
}
