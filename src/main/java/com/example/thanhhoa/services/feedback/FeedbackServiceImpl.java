package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.CreateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.CreateOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateOrderFeedbackModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Rating;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractFeedbackRepository;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderDetailRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
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
    private Util util;


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
        orderFeedback.setCreatedDate(LocalDateTime.now());
        orderFeedback.setStatus(Status.ACTIVE);
        orderFeedback.setCustomer(userRepository.getById(userID));
        orderFeedback.setDescription(createOrderFeedbackModel.getDescription());
        orderFeedback.setPlant(plant.get());
        orderFeedback.setRating(ratingRepository.getById(createOrderFeedbackModel.getRatingID()));
        orderFeedback.setOrderDetail(orderDetail.get());
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
        orderFeedback.setOrderDetail(orderDetail.get());
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
        orderFeedback.setStatus(Status.INACTIVE);
        orderFeedbackRepository.save(orderFeedback);
        return "Xóa thành công.";
    }

    @Override
    public List<ShowOrderFeedbackModel> getAllOrderFeedback(Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findAllByStatus(Status.ACTIVE, pageable);
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
            imgModel.setImgURL(imgModel.getImgURL());
            imgModelList.add(imgModel);
        }

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        plantModel.setId(orderFeedback.getPlant().getId());
        if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setPlantName(orderFeedback.getPlant().getName());

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
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
            imgModel.setImgURL(imgModel.getImgURL());
            imgModelList.add(imgModel);
        }

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        plantModel.setId(orderFeedback.getPlant().getId());
        if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setPlantName(orderFeedback.getPlant().getName());

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
        return model;
    }

    @Override
    public List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(String plantID, Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findByPlantId(plantID, pageable);
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
            imgModel.setImgURL(imgModel.getImgURL());
            imgModelList.add(imgModel);
        }

        ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
        model.setOrderFeedbackID(orderFeedback.getId());
        model.setDescription(orderFeedback.getDescription());
        model.setCreatedDate(orderFeedback.getCreatedDate());
        model.setRatingModel(ratingModel);
        model.setImgList(imgModelList);
        model.setStatus(orderFeedback.getStatus());
        return model;
    }

    // -------------------------------------------- CONTRACT -----------------------------------------

    @Override
    public String createContract(CreateContractFeedbackModel createContractFeedbackModel, Long userID) {
        Optional<Contract> contract = contractRepository.findById(createContractFeedbackModel.getContractID());
        if(contract == null){
            return "Không tìm thấy Hợp đồng với ID là " + createContractFeedbackModel.getContractID() + ".";
        }
        ContractFeedback contractFeedback = new ContractFeedback();
        ContractFeedback lastContractFeedback = contractFeedbackRepository.findFirstByOrderByIdDesc();
        if(lastContractFeedback == null) {
            contractFeedback.setId(util.createNewID("CF"));
        } else {
            contractFeedback.setId(util.createIDFromLastID("CF", 2, lastContractFeedback.getId()));
        }
        contractFeedback.setContract(contract.get());
        contractFeedback.setStatus(Status.ACTIVE);
        contractFeedback.setDescription(createContractFeedbackModel.getDescription());
        contractFeedback.setRating(ratingRepository.getById(createContractFeedbackModel.getRatingID()));
        contractFeedback.setDate(LocalDateTime.now());
        contractFeedbackRepository.save(contractFeedback);
        return "Tạo thành công.";
    }

    @Override
    public String updateContract(UpdateContractFeedbackModel updateContractFeedbackModel) {
        Optional<ContractFeedback> checkExisted = contractFeedbackRepository.findById(updateContractFeedbackModel.getId());
        if(checkExisted == null){
            return "Không tìm thấy Feedback Hợp đồng với ID là " + updateContractFeedbackModel.getId() + ".";
        }
        Optional<Contract> contract = contractRepository.findById(updateContractFeedbackModel.getContractID());
        if(contract == null){
            return "Không tìm thấy Hợp đồng với ID là " + updateContractFeedbackModel.getContractID() + ".";
        }
        ContractFeedback contractFeedback = checkExisted.get();
        contractFeedback.setContract(contract.get());
        contractFeedback.setDescription(updateContractFeedbackModel.getDescription());
        contractFeedback.setRating(ratingRepository.getById(updateContractFeedbackModel.getRatingID()));
        contractFeedbackRepository.save(contractFeedback);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteContract(String id) {
        Optional<ContractFeedback> checkExisted = contractFeedbackRepository.findById(id);
        if(checkExisted == null){
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
        if(ratingList == null){
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
