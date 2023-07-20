package com.example.thanhhoa.services.order;

import com.example.thanhhoa.dtos.OrderModels.CreateOrderModel;
import com.example.thanhhoa.dtos.OrderModels.GetStaffModel;
import com.example.thanhhoa.dtos.OrderModels.OrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowDistancePriceModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowPlantModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.dtos.OrderModels.UpdateOrderModel;
import com.example.thanhhoa.entities.Cart;
import com.example.thanhhoa.entities.DistancePrice;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.OrderFeedback;
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
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.OrderPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private OrderFeedbackRepository orderFeedbackRepository;
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
        order.setPaymentMethod(createOrderModel.getFullName());
        order.setCreatedDate(LocalDateTime.now());
        order.setDistance(createOrderModel.getDistance());
        order.setLatLong(createOrderModel.getLatLong());
        order.setProgressStatus(Status.WAITING);

        if(createOrderModel.getStaffID() != null) {
            tblAccount staff = userRepository.getById(createOrderModel.getStaffID());
            staff.setStatus(Status.UNAVAILABLE);
            userRepository.save(staff);
            order.setStaff(staff);
        }

        order.setCustomer(userRepository.getById(customerID));
        order.setStore(storeRepository.getById(createOrderModel.getStoreID()));
        orderRepository.save(order);

        DistancePrice distancePrice = distancePriceRepository.getById(createOrderModel.getDistancePriceID());
        Double totalPriceOfAPlant = 0.0;
        Double totalShipCost = 0.0;
        Double total = 0.0;
        for(OrderDetailModel model : createOrderModel.getDetailList()) {
            OrderDetail lastDetailRecord = orderDetailRepository.findFirstByOrderByIdDesc();
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
            detail.setPrice(totalPriceOfAPlant);
            detail.setQuantity(model.getQuantity());
            detail.setTblOrder(order);
            detail.setPlant(plant);
            orderDetailRepository.save(detail);

            Cart cart = cartRepository.findByPlant_Id(model.getPlantID());
            cart.setQuantity(0);
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
        order.setCreatedDate(LocalDateTime.now());
        order.setDistance(updateOrderModel.getDistance());
        order.setLatLong(updateOrderModel.getLatLong());
        order.setProgressStatus(Status.WAITING);

        tblAccount staff = userRepository.getById(updateOrderModel.getStaffID());
        staff.setStatus(Status.UNAVAILABLE);
        userRepository.save(staff);

        order.setStaff(staff);
        order.setCustomer(userRepository.getById(customerID));
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
            detail.setPrice(totalPriceOfAPlant);
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
            if(order.getStaff() != null){
                order.getStaff().setStatus(Status.AVAILABLE);
            }
            order.setReason(reason);
            order.setProgressStatus(status);
            order.setRejectDate(LocalDateTime.now());

            List<OrderDetail> orderDetailList = order.getOrderDetailList();
            for(OrderDetail orderDetail : orderDetailList) {
                StorePlant storePlant = storePlantRepository.
                        findByPlantIdAndStoreIdAndPlant_Status(orderDetail.getPlant().getId(), order.getStore().getId(), Status.ONSALE);
                storePlant.setQuantity(storePlant.getQuantity() + orderDetail.getQuantity());
                storePlantRepository.save(storePlant);

                StorePlantRecord storePlantRecord = new StorePlantRecord();
                StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
                if(lastRecord == null) {
                    storePlantRecord.setId(util.createNewID("SPR"));
                } else {
                    storePlantRecord.setId(util.createIDFromLastID("SPR", 3, lastRecord.getId()));
                }
                storePlantRecord.setImportDate(LocalDateTime.now());
                storePlantRecord.setAmount(orderDetail.getQuantity());
                storePlantRecord.setStorePlant(storePlant);
                storePlantRecord.setReason("Hủy đơn hàng với mã là : " + orderID + ".");
                storePlantRecordRepository.save(storePlantRecord);
            }
            orderRepository.save(checkExistedOrder.get());
            return "Xóa thành công.";
        }
        return "Không tồn tại Order với ID là : " + orderID + ".";
    }

    @Override
    public String approveOrder(String orderID) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(orderID);
        if(checkExistedOrder != null) {
            tblOrder order = checkExistedOrder.get();
            order.setProgressStatus(Status.APPROVED);
            order.getStaff().setStatus(Status.UNAVAILABLE);
            orderRepository.save(checkExistedOrder.get());

            List<OrderDetail> orderDetailList = order.getOrderDetailList();
            for(OrderDetail orderDetail : orderDetailList) {
                StorePlant storePlant = storePlantRepository.
                        findByPlantIdAndStoreIdAndPlant_Status(orderDetail.getPlant().getId(), order.getStore().getId(), Status.ONSALE);
                storePlant.setQuantity(storePlant.getQuantity() - orderDetail.getQuantity());
                storePlantRepository.save(storePlant);

                StorePlantRecord storePlantRecord = new StorePlantRecord();
                StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
                if(lastRecord == null) {
                    storePlantRecord.setId(util.createNewID("SPR"));
                } else {
                    storePlantRecord.setId(util.createIDFromLastID("SPR", 3, lastRecord.getId()));
                }
                storePlantRecord.setImportDate(LocalDateTime.now());
                storePlantRecord.setAmount(orderDetail.getQuantity());
                storePlantRecord.setStorePlant(storePlant);
                storePlantRecord.setReason("Chấp nhận đơn hàng với mã là : " + orderID + ".");
                storePlantRecordRepository.save(storePlantRecord);
            }
            return "Chấp nhận thành công.";
        }
        return "Không tồn tại Order với ID là : " + orderID + ".";
    }

    @Override
    public Boolean changeOrderStatus(String orderID, String status) {
        Optional<tblOrder> checkExistedOrder = orderRepository.findById(orderID);
        if(checkExistedOrder != null) {
            if(status.equals("PACKAGING")) {
                checkExistedOrder.get().setProgressStatus(Status.PACKAGING);
            } else if(status.equals("DELIVERING")) {
                checkExistedOrder.get().setProgressStatus(Status.DELIVERING);
            } else {
                checkExistedOrder.get().setProgressStatus(Status.RECEIVED);
                checkExistedOrder.get().getStaff().setStatus(Status.AVAILABLE);
            }
            orderRepository.save(checkExistedOrder.get());
            return true;
        }
        return false;
    }

    @Override
    public List<ShowOrderModel> getAllOrderByUsername(String username, Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findByCustomer_Username(username, pageable);
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getAllByStatusOrderByUsername(Status status, String username, Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findByCustomer_UsernameAndProgressStatus(username, status, pageable);
        return util.orderPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowOrderModel> getWaitingOrder(Pageable pageable) {
        Page<tblOrder> pagingResult = orderPagingRepository.findByProgressStatus(Status.WAITING, pageable);
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
            ShowOrderDetailModel model = new ShowOrderDetailModel();
            //detail
            model.setId(orderDetail.getId());
            model.setPrice(orderDetail.getPrice());
            model.setQuantity(orderDetail.getQuantity());

            //order
            ShowOrderModel orderModel = new ShowOrderModel();
            orderModel.setId(orderDetail.getTblOrder().getId());
            orderModel.setFullName(orderDetail.getTblOrder().getFullName());
            orderModel.setAddress(orderDetail.getTblOrder().getAddress());
            orderModel.setEmail(orderDetail.getTblOrder().getEmail());
            orderModel.setPhone(orderDetail.getTblOrder().getPhone());
            orderModel.setCreatedDate(orderDetail.getTblOrder().getCreatedDate());
            orderModel.setPackageDate(orderDetail.getTblOrder().getPackageDate());
            orderModel.setDeliveryDate(orderDetail.getTblOrder().getDeliveryDate());
            orderModel.setReceivedDate(orderDetail.getTblOrder().getReceivedDate());
            orderModel.setApproveDate(orderDetail.getTblOrder().getApproveDate());
            orderModel.setRejectDate(orderDetail.getTblOrder().getRejectDate());
            orderModel.setPaymentMethod(orderDetail.getTblOrder().getPaymentMethod());
            orderModel.setProgressStatus(orderDetail.getTblOrder().getProgressStatus());
            orderModel.setReason(orderDetail.getTblOrder().getReason());
            orderModel.setLatLong(orderDetail.getTblOrder().getLatLong());
            orderModel.setDistance(orderDetail.getTblOrder().getDistance());
            orderModel.setTotalShipCost(orderDetail.getTblOrder().getTotalShipCost());
            orderModel.setTotal(orderDetail.getTblOrder().getTotal());
            OrderFeedback orderFeedback = orderFeedbackRepository.findByOrderDetail_Id(orderDetail.getId());
            if(orderFeedback != null){
                model.setIsFeedback(true);
            }

            //customer
            ShowCustomerModel customerModel = new ShowCustomerModel();
            customerModel.setId(orderDetail.getTblOrder().getCustomer().getId());
            customerModel.setAddress(orderDetail.getTblOrder().getCustomer().getAddress());
            customerModel.setEmail(orderDetail.getTblOrder().getCustomer().getEmail());
            customerModel.setPhone(orderDetail.getTblOrder().getCustomer().getPhone());
            customerModel.setFullName(orderDetail.getTblOrder().getCustomer().getFullName());

            //staff
            ShowStaffModel staffModel = new ShowStaffModel();
            if(orderDetail.getTblOrder().getStaff() != null) {
                staffModel.setId(orderDetail.getTblOrder().getStaff().getId());
                staffModel.setAddress(orderDetail.getTblOrder().getStaff().getAddress());
                staffModel.setEmail(orderDetail.getTblOrder().getStaff().getEmail());
                staffModel.setPhone(orderDetail.getTblOrder().getStaff().getPhone());
                staffModel.setFullName(orderDetail.getTblOrder().getStaff().getFullName());
            }

            //distance price
            ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
            distancePriceModel.setId(orderDetail.getTblOrder().getDistancePrice().getId());
            distancePriceModel.setApplyDate(orderDetail.getTblOrder().getDistancePrice().getApplyDate());
            distancePriceModel.setPricePerKm(orderDetail.getTblOrder().getDistancePrice().getPricePerKm());

            //plant
            ShowPlantModel plantModel = new ShowPlantModel();
            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(orderDetail.getPlant().getId(), Status.ACTIVE);
            plantModel.setId(orderDetail.getPlant().getId());
            if(orderDetail.getPlant().getPlantIMGList() != null  && !orderDetail.getPlant().getPlantIMGList().isEmpty()) {
                plantModel.setImage(orderDetail.getPlant().getPlantIMGList().get(0).getImgURL());
            }
            plantModel.setQuantity(orderDetail.getQuantity());
            plantModel.setPlantName(orderDetail.getPlant().getName());
            plantModel.setPlantPrice(newestPrice.getPrice());
            plantModel.setShipPrice(orderDetail.getPlant().getPlantShipPrice().getPricePerPlant());

            //store
            ShowStoreModel storeModel = new ShowStoreModel();
            storeModel.setId(orderDetail.getTblOrder().getStore().getId());
            storeModel.setStoreName(orderDetail.getTblOrder().getStore().getStoreName());
            storeModel.setAddress(orderDetail.getTblOrder().getStore().getAddress());
            storeModel.setPhone(orderDetail.getTblOrder().getStore().getPhone());

            model.setShowOrderModel(orderModel);
            model.setShowCustomerModel(customerModel);
            model.setShowPlantModel(plantModel);
            model.setShowDistancePriceModel(distancePriceModel);
            model.setShowStaffModel(staffModel);
            model.setShowStoreModel(storeModel);
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<GetStaffModel> getStaffForOrder() {
        List<tblAccount> listStaff = userRepository.findByStatusAndRole_RoleName(Status.AVAILABLE, "Staff");
        List<GetStaffModel> modelList = new ArrayList<>();
        for(tblAccount staff : listStaff) {
            GetStaffModel model = new GetStaffModel();
            model.setStaffID(staff.getId());
            model.setStaffName(staff.getFullName());
            modelList.add(model);
        }
        return modelList;
    }
}
