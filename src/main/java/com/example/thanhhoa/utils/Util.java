package com.example.thanhhoa.utils;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class Util {

    @Autowired
    private PlantCategoryRepository plantCategoryRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    /**
     * Small Util to return {@link Pageable} to replace dup code in serviceImpl
     */
    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean sortTypeAsc) {
        if (sortTypeAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

    public List<ShowUserModel> userPagingConverter(Page<tblAccount> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowUserModel> modelResult = pagingResult.map(new Converter<tblAccount, ShowUserModel>() {
                @Override
                protected ShowUserModel doForward(tblAccount user) {
                    ShowUserModel model = new ShowUserModel();
                    model.setUserID(user.getId());
                    model.setUserName(user.getUsername());
                    model.setFullName(user.getFullName());
                    model.setEmail(user.getEmail());
                    model.setPhone(user.getPhone());
                    model.setAvatar(user.getAvatar());
                    model.setCreatedDate(user.getCreatedDate());
                    model.setAddress(user.getAddress());
                    model.setGender(user.getGender());
                    model.setRoleID(user.getRole().getId());
                    model.setRoleName(user.getRole().getRoleName());
                    model.setStatus(user.getStatus());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected tblAccount doBackward(ShowUserModel showUserModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowPlantModel> plantPagingConverter(Page<Plant> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<Plant, ShowPlantModel>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
                    for (PlantCategory plantCategory : plantCategoryList) {
                        ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                        showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                        showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                        showPlantCategoryList.add(showPlantCategory);
                    }
                    ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
                    showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
                    showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
                    showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

                    ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
                    showPlantPriceModel.setId(plant.getPlantPrice().getId());
                    showPlantPriceModel.setPrice(plant.getPlantPrice().getPrice());
                    showPlantPriceModel.setApplyDate(plant.getPlantPrice().getApplyDate());

                    ShowPlantModel model = new ShowPlantModel();
                    model.setPlantID(plant.getId());
                    model.setName(plant.getName());
                    model.setHeight(plant.getHeight());
                    model.setWithPot(plant.getWithPot());
                    model.setShowPlantShipPriceModel(showPlantShipPriceModel);
                    model.setPlantCategoryList(showPlantCategoryList);
                    model.setShowPlantPriceModel(showPlantPriceModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Plant doBackward(ShowPlantModel showPlantModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowCategoryModel> categoryPagingConverter(Page<Category> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowCategoryModel> modelResult = pagingResult.map(new Converter<Category, ShowCategoryModel>() {
                @Override
                protected ShowCategoryModel doForward(Category category) {
                    ShowCategoryModel model = new ShowCategoryModel();
                    model.setCategoryID(category.getId());
                    model.setCategoryName(category.getName());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Category doBackward(ShowCategoryModel showCategoryModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowOrderFeedbackModel> orderFeedbackPagingConverter(Page<OrderFeedback> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowOrderFeedbackModel> modelResult = pagingResult.map(new Converter<OrderFeedback, ShowOrderFeedbackModel>() {
                @Override
                protected ShowOrderFeedbackModel doForward(OrderFeedback orderFeedback) {
                    ShowRatingModel ratingModel = new ShowRatingModel();
                    ratingModel.setId(orderFeedback.getRating().getId());
                    ratingModel.setDescription(orderFeedback.getRating().getDescription());

                    List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
                    for (OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
                        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
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
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected OrderFeedback doBackward(ShowOrderFeedbackModel showOrderFeedback) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowContractFeedbackModel> contractFeedbackPagingConverter(Page<ContractFeedback> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowContractFeedbackModel> modelResult = pagingResult.map(new Converter<ContractFeedback, ShowContractFeedbackModel>() {
                @Override
                protected ShowContractFeedbackModel doForward(ContractFeedback contractFeedback) {
                    ShowRatingModel ratingModel = new ShowRatingModel();
                    ratingModel.setId(contractFeedback.getRating().getId());
                    ratingModel.setDescription(contractFeedback.getRating().getDescription());

                    ShowContractFeedbackModel model = new ShowContractFeedbackModel();
                    model.setContractFeedbackID(contractFeedback.getId());
                    model.setDescription(contractFeedback.getDescription());
                    model.setCreatedDate(contractFeedback.getDate());
                    model.setRatingModel(ratingModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected ContractFeedback doBackward(ShowContractFeedbackModel showContractFeedback) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowServiceModel> servicePagingConverter(Page<Service> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowServiceModel> modelResult = pagingResult.map(new Converter<Service, ShowServiceModel>() {
                @Override
                protected ShowServiceModel doForward(Service service) {
                    List<ShowServiceTypeModel> typeList = new ArrayList<>();
                    List<ServiceType> serviceTypeList = serviceTypeRepository.findByService_Id(service.getId());
                    if(serviceTypeList != null){
                        for (ServiceType serviceType : serviceTypeList) {
                            ShowServiceTypeModel typeModel = new ShowServiceTypeModel();
                            typeModel.setId(serviceType.getId());
                            typeModel.setName(serviceType.getName());
                            typeModel.setApplyDate(serviceType.getApplyDate());
                            typeModel.setSize(serviceType.getSize());
                            typeModel.setPercentage(serviceType.getPercentage());
                            typeModel.setServiceID(service.getId());
                            typeList.add(typeModel);
                        }
                    }

                    ShowServiceModel model = new ShowServiceModel();
                    model.setServiceID(service.getId());
                    model.setName(service.getName());
                    model.setPrice(service.getPrice());
                    model.setDescription(service.getDescription());
                    model.setTypeList(typeList);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Service doBackward(ShowServiceModel showServiceModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowOrderModel> orderPagingConverter(Page<tblOrder> pagingResult, Pageable paging) {
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowOrderModel> modelResult = pagingResult.map(new Converter<tblOrder, ShowOrderModel>() {
                @Override
                protected ShowOrderModel doForward(tblOrder order) {
                    ShowOrderModel model = new ShowOrderModel();
                    model.setId(order.getId());
                    model.setFullName(order.getFullName());
                    model.setAddress(order.getAddress());
                    model.setEmail(order.getEmail());
                    model.setPhone(order.getPhone());
                    model.setCreatedDate(order.getCreatedDate());
                    model.setPackageDate(order.getPackageDate());
                    model.setDeliveryDate(order.getDeliveryDate());
                    model.setReceivedDate(order.getReceivedDate());
                    model.setApproveDate(order.getApproveDate());
                    model.setRejectDate(order.getRejectDate());
                    model.setPaymentMethod(order.getPaymentMethod());
                    model.setProgressStatus(order.getProgressStatus());
                    model.setReason(order.getReason());
                    model.setStoreID(order.getStore().getId());
                    model.setStaffID(order.getStaff().getId());
                    model.setCustomerID(order.getCustomer().getId());
                    model.setDistancePriceID(order.getDistancePrice().getId());
                    model.setDistance(order.getDistance());
                    model.setTotalShipCost(order.getTotalShipCost());
                    model.setTotal(order.getTotal());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected tblOrder doBackward(ShowOrderModel showOrderModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public String createNewID(String chars) {
        return chars + String.format("%03d", 1);
    }

    public String createIDFromLastID(String chars, Integer index, String lastID){
        Integer IDNumber = Integer.parseInt(lastID.substring(index));
        IDNumber++;
        String newID = chars + String.format("%03d", IDNumber);
        return newID;
    }

//    class InvalidInput extends Exception {
//        public InvalidInput(String input) {
//            super(input);
//        }
//    }
}
