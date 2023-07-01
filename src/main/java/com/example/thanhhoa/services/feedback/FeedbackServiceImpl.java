package com.example.thanhhoa.services.feedback;

import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractFeedbackRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.pagings.ContractFeedbackPagingRepository;
import com.example.thanhhoa.repositories.pagings.OrderFeedbackPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private Util util;


    @Override
    public List<ShowOrderFeedbackModel> getAllOrderFeedback(Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findAllByStatus(Status.ACTIVE, pageable);
        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByID(String orderFeedbackID) {
        Optional<OrderFeedback> searchResult = orderFeedbackRepository.findById(orderFeedbackID);
        if (searchResult == null) {
            return null;
        }
        OrderFeedback orderFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for (OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
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

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByUsername(String username) {
        Optional<OrderFeedback> searchResult = orderFeedbackRepository.findByCustomer_UsernameAndStatus(username, Status.ACTIVE);
        if (searchResult == null) {
            return null;
        }
        OrderFeedback orderFeedback = searchResult.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for (OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
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

    @Override
    public List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(String plantID, Pageable pageable) {
        Page<OrderFeedback> pagingResult = orderFeedbackPagingRepository.findByPlantId(plantID, pageable);
        return util.orderFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowOrderFeedbackModel getOrderFeedbackByOrderID(String orderID) {
        Optional<OrderFeedback> checkOrderFeedback = orderFeedbackRepository.findByTblOrder_Id(orderID);
        if(checkOrderFeedback == null){
            return null;
        }

        OrderFeedback orderFeedback = checkOrderFeedback.get();
        ShowRatingModel ratingModel = new ShowRatingModel();
        ratingModel.setId(orderFeedback.getRating().getId());
        ratingModel.setDescription(orderFeedback.getRating().getDescription());

        List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
        for (OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
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
    public List<ShowContractFeedbackModel> getAllContractFeedback(Pageable pageable) {
        Page<ContractFeedback> pagingResult = contractFeedbackPagingRepository.findAllByStatus(Status.ACTIVE, pageable);
        return util.contractFeedbackPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowContractFeedbackModel getContractFeedbackByID(String contractFeedbackID) {
        Optional<ContractFeedback> searchResult = contractFeedbackRepository.findById(contractFeedbackID);
        if(searchResult == null){
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
        if(searchResult == null){
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
}
