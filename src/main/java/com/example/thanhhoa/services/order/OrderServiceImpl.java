package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.CreateOrderModel;
import com.example.thanhhoa.dtos.OrderModels.GetStaffModel;
import com.example.thanhhoa.dtos.OrderModels.OrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.UpdateOrderModel;
import com.example.thanhhoa.entities.Cart;
import com.example.thanhhoa.entities.DistancePrice;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CartRepository;
import com.example.thanhhoa.repositories.DistancePriceRepository;
import com.example.thanhhoa.repositories.OrderDetailRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.OrderDetailPagingRepository;
import com.example.thanhhoa.repositories.pagings.OrderPagingRepository;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DistancePriceRepository distancePriceRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private OrderPagingRepository orderPagingRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private StorePlantRecordRepository storePlantRecordRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderDetailPagingRepository orderDetailPagingRepository;
    @Autowired
    private Util util;

    @Override
    public String createOrder(CreateOrderModel createOrderModel, Long customerID) {
        if(createOrderModel.getDetailList() == null) {
            return "Danh sách cây không được để trống.";
        }

        tblOrder order = new tblOrder();
        tblOrder lastOrderRecord = orderRepository.findFirstByOrderByIdDesc();
        if(lastOrderRecord == null) {
            order.setId(util.createNewID("O"));
        } else {
            order.setId(util.createIDFromLastID("O", 1, lastOrderRecord.getId()));
        }
        order.setFullName(createOrderModel.getFullName());
        order.setAddress(createOrderModel.getAddress());
        order.setPhone(createOrderModel.getPhone());
        order.setEmail(createOrderModel.getEmail());
        order.setPaymentMethod(createOrderModel.getPaymentMethod());
        order.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        order.setDistance(createOrderModel.getDistance());
        order.setLatLong(createOrderModel.getLatLong());
        order.setProgressStatus(Status.WAITING);

        tblAccount account = userRepository.getById(customerID);
        if(account.getRole().getRoleName().equalsIgnoreCase("Staff")){
            order.setStaff(account);
            order.setProgressStatus(Status.APPROVED);
        }
        if(account.getRole().getRoleName().equalsIgnoreCase("Customer")){
            order.setCustomer(account);
        }
        order.setStore(storeRepository.getById(createOrderModel.getStoreID()));

        DistancePrice distancePrice = distancePriceRepository.getById(createOrderModel.getDistancePriceID());
        Double totalPriceOfAPlant = 0.0;
        Double totalShipCost = 0.0;
        Double total = 0.0;
        for(OrderDetailModel model : createOrderModel.getDetailList()) {
            Plant plant = plantRepository.getById(model.getPlantID());
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
            totalPriceOfAPlant += newestPrice.getPrice() * model.getQuantity();
            totalShipCost += plant.getPlantShipPrice().getPricePerPlant() * model.getQuantity();

            OrderDetail detail = new OrderDetail();
            OrderDetail lastDetailRecord = orderDetailRepository.findFirstByOrderByIdDesc();
            if(lastDetailRecord == null) {
                detail.setId(util.createNewID("OD"));
            } else {
                detail.setId(util.createIDFromLastID("OD", 2, lastDetailRecord.getId()));
            }
            detail.setPrice(newestPrice.getPrice());
            detail.setQuantity(model.getQuantity());
            detail.setTblOrder(order);
            detail.setPlant(plant);


            Cart cart = cartRepository.findByPlant_IdAndAccount_Id(model.getPlantID(), customerID);
            cart.setQuantity(0);

            orderDetailRepository.save(detail);
            cartRepository.save(cart);
        }
        Double totalDistancePrice = distancePrice.getPricePerKm() * createOrderModel.getDistance();
        total += totalPriceOfAPlant + totalShipCost + totalDistancePrice;

        order.setDistancePrice(distancePrice);
        order.setTotalShipCost(totalShipCost);
        order.setTotal(total);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public String updateOrder(UpdateOrderModel updateOrderModel, Long customerID) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(updateOrderModel.getOrderID());
        if(checkExistedOrder == null) {
            return "Không tồn tại Order với ID là " + updateOrderModel.getOrderID() + ".";
        }
        if(updateOrderModel.getDetailList() == null) {
            return "Danh sách cây không được để trống.";
        }

        tblOrder order = new tblOrder();
        tblOrder lastOrderRecord = orderRepository.findFirstByOrderByIdDesc();
        if(lastOrderRecord == null) {
            order.setId(util.createNewID("O"));
        } else {
            order.setId(util.createIDFromLastID("O", 1, lastOrderRecord.getId()));
        }
        order.setFullName(updateOrderModel.getFullName());
        order.setAddress(updateOrderModel.getAddress());
        order.setPhone(updateOrderModel.getPhone());
        order.setEmail(updateOrderModel.getEmail());
        order.setPaymentMethod(updateOrderModel.getFullName());
        order.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        order.setDistance(updateOrderModel.getDistance());
        order.setLatLong(updateOrderModel.getLatLong());
        order.setProgressStatus(Status.WAITING);

        if(updateOrderModel.getStaffID() != null){
            Optional<tblAccount> checkStaff = userRepository.findById(updateOrderModel.getStaffID());
            if(checkStaff == null){
                return "Không tìm thấy Staff với ID là " + updateOrderModel.getStaffID() + ".";
            }
            tblAccount staff = checkStaff.get();
            order.setStaff(staff);
        }

        order.setStore(storeRepository.getById(updateOrderModel.getStoreID()));
        orderRepository.save(order);

        DistancePrice distancePrice = distancePriceRepository.getById(updateOrderModel.getDistancePriceID());
        Double totalPriceOfAPlant = 0.0;
        Double totalShipCost = 0.0;
        Double total = 0.0;
        OrderDetail lastDetailRecord = orderDetailRepository.findFirstByOrderByIdDesc();
        for(OrderDetailModel model : updateOrderModel.getDetailList()) {
            Plant plant = plantRepository.getById(model.getPlantID());
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
            totalPriceOfAPlant += newestPrice.getPrice() * model.getQuantity();
            totalShipCost += plant.getPlantShipPrice().getPricePerPlant() * model.getQuantity();

            OrderDetail detail = new OrderDetail();
            if(lastDetailRecord == null) {
                detail.setId(util.createNewID("OD"));
            } else {
                detail.setId(util.createIDFromLastID("OD", 2, lastDetailRecord.getId()));
            }
            detail.setPrice(newestPrice.getPrice());
            detail.setQuantity(model.getQuantity());
            detail.setTblOrder(order);
            detail.setPlant(plant);
            orderDetailRepository.save(detail);
        }
        Double totalDistancePrice = distancePrice.getPricePerKm() * updateOrderModel.getDistance();
        total += totalPriceOfAPlant + totalShipCost + totalDistancePrice;

        order.setDistancePrice(distancePrice);
        order.setTotalShipCost(totalShipCost);
        order.setTotal(total);
        orderRepository.save(order);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteOrder(String orderID, String reason, Status status) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(orderID);
        if(checkExistedOrder != null) {
            tblOrder order = checkExistedOrder.get();
            if(!order.getProgressStatus().toString().equals("WAITING")) {
                return "Chỉ được hủy đơn hàng có trạng thái là WAITING.";
            }
            order.setReason(reason);
            order.setProgressStatus(status);
            order.setRejectDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            orderRepository.save(checkExistedOrder.get());
            return "Xóa thành công.";
        }
        return "Không tồn tại Order với ID là : " + orderID + ".";
    }

    @Override
    public String approveOrder(String orderID, Long staffID) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(orderID);
        if(checkExistedOrder != null) {
            tblAccount staff = userRepository.findByIdAndStatus(staffID, Status.ACTIVE);
            if(staff == null){
                return "Không tìm thấy Staff với ID là : " + staffID + " có trạng thái ACTIVE.";
            }
            tblOrder order = checkExistedOrder.get();
            order.setStaff(staff);
            order.setProgressStatus(Status.APPROVED);
            order.setApproveDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

            List<OrderDetail> orderDetailList = order.getOrderDetailList();
            for(OrderDetail orderDetail : orderDetailList) {
                StorePlant storePlant = storePlantRepository.
                        findByPlantIdAndStoreIdAndPlant_Status(orderDetail.getPlant().getId(), order.getStore().getId(), Status.ONSALE);
                storePlant.setQuantity(storePlant.getQuantity() - orderDetail.getQuantity());

                StorePlantRecord storePlantRecord = new StorePlantRecord();
                StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
                if(lastRecord == null) {
                    storePlantRecord.setId(util.createNewID("SPR"));
                } else {
                    storePlantRecord.setId(util.createIDFromLastID("SPR", 3, lastRecord.getId()));
                }
                storePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                storePlantRecord.setAmount(orderDetail.getQuantity());
                storePlantRecord.setStorePlant(storePlant);
                storePlantRecord.setReason("Chấp nhận đơn hàng với mã là : " + orderID + ".");
                storePlantRepository.save(storePlant);
                storePlantRecordRepository.save(storePlantRecord);
            }
            orderRepository.save(checkExistedOrder.get());
            return "Chấp nhận thành công.";
        }
        return "Không tồn tại Order với ID là : " + orderID + ".";
    }

    @Override
    public Boolean changeOrderStatus(String orderID, String receiptIMG, String status) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(orderID);
        if(checkExistedOrder != null) {
            tblOrder order = checkExistedOrder.get();
            if(status.equalsIgnoreCase("PACKAGING")) {
                order.setProgressStatus(Status.PACKAGING);
                order.setPackageDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            } else if(status.equalsIgnoreCase("DELIVERING")) {
                order.setProgressStatus(Status.DELIVERING);
                order.setDeliveryDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            } else if(status.equalsIgnoreCase("RECEIVED")){
                if(receiptIMG != null){
                    order.setReceiptIMG(receiptIMG);
                }
                order.setProgressStatus(Status.RECEIVED);
                order.setReceivedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            }else{
                return false;
            }
            orderRepository.save(checkExistedOrder.get());
            return true;
        }
        return false;
    }

    @Override
    public List<ShowOrderModel> getAllOrderByUserID(Long userID, String roleName, Pageable pageable) {
        Page<tblOrder> pagingResult;
        if(roleName.equalsIgnoreCase("Staff")){
            pagingResult = orderPagingRepository.findByStaff_Id(userID, pageable);
        }else{
            pagingResult = orderPagingRepository.findByCustomer_Id(userID, pageable);
        }
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getAllOrder(Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findAll(pageable);
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getAllOrderByStoreID(String storeID, Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findByStore_Id(storeID, pageable);
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getAllByStatusOrderByUserID(Status status, Long userID, String roleName, Pageable pageable) {
        Page<tblOrder> pagingResult;
        if(roleName.equalsIgnoreCase("Staff")){
            pagingResult = orderPagingRepository.findByStaff_IdAndProgressStatus(userID, status, pageable);
        }else{
            pagingResult = orderPagingRepository.findByCustomer_IdAndProgressStatus(userID, status, pageable);
        }
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getWaitingOrder(String storeID, Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findByProgressStatusAndStore_Id(Status.WAITING, storeID, pageable);
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderDetailModel> getOrderDetailByOrderID(String orderID) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByTblOrder_Id(orderID);
        if(orderDetailList == null) {
            return null;
        }
        List<ShowOrderDetailModel> modelList = new ArrayList<>();
        for(OrderDetail orderDetail : orderDetailList) {
            modelList.add(util.returnOrderDetailModelList(orderDetail));
        }
        return modelList;
    }

    @Override
    public List<ShowOrderDetailModel> getOrderDetailByIsFeedback(String isFeedback, Pageable pageable) {
        Page<OrderDetail> pagingResult = null;
        if(isFeedback == null) {
            pagingResult = orderDetailPagingRepository.findAllByTblOrder_ProgressStatus(Status.RECEIVED, pageable);
        } else if(isFeedback.equalsIgnoreCase("true")) {
            pagingResult = orderDetailPagingRepository.findByIsFeedbackAndTblOrder_ProgressStatus(true, Status.RECEIVED, pageable);
        } else {
            pagingResult = orderDetailPagingRepository.findByIsFeedbackAndTblOrder_ProgressStatus(false, Status.RECEIVED, pageable);
        }
        return util.orderDetailPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<GetStaffModel> getStaffForOrder() {
        List<tblAccount> listStaff = userRepository.findByStatusAndRole_RoleName(Status.ACTIVE, "Staff");
        List<GetStaffModel> modelList = new ArrayList<>();
        for(tblAccount staff : listStaff) {
            GetStaffModel model = new GetStaffModel();
            model.setStaffID(staff.getId());
            model.setStaffName(staff.getFullName());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String updateIsPaid(String orderID, Boolean isPaid){
        Optional<tblOrder> checkExisted = orderRepository.findById(orderID);
        if(checkExisted == null){
            return "Không tìm thấy Order với ID là : " + orderID + " .";
        }
        tblOrder order = checkExisted.get();
        order.setIsPaid(isPaid);
        orderRepository.save(order);
        return "Chỉnh sửa thành công.";
    }
}
