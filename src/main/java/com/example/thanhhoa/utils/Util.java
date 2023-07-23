package com.example.thanhhoa.utils;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowPaymentTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServicePackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowDistancePriceModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantIMGModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceIMGModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.ContractIMG;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceIMG;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractIMGRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.ServiceIMGRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    @Autowired
    private PlantCategoryRepository plantCategoryRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ServiceIMGRepository serviceIMGRepository;
    @Autowired
    private PlantIMGRepository plantIMGRepository;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private ContractIMGRepository contractIMGRepository;
    @Autowired
    private WorkingDateRepository workingDateRepository;
    @Autowired
    private OrderFeedbackRepository orderFeedbackRepository;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;

    /**
     * Small Util to return {@link Pageable} to replace dup code in serviceImpl
     */
    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean sortTypeAsc) {
        if(sortTypeAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

    public List<ShowUserModel> userPagingConverter(Page<tblAccount> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
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
                    StoreEmployee storeEmployee = storeEmployeeRepository.findByAccount_IdAndStatus(user.getId(), Status.ACTIVE);
                    if((user.getRole().getRoleName().equalsIgnoreCase("Manager") || user.getRole().getRoleName().equalsIgnoreCase("Staff")) && storeEmployee != null) {
                        model.setStoreID(storeEmployee.getStore().getId());
                        model.setStoreName(storeEmployee.getStore().getStoreName());
                    }
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

    public List<ShowWorkingDateModel> workingDatePagingConverter(Page<WorkingDate> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowWorkingDateModel> modelResult = pagingResult.map(new Converter<WorkingDate, ShowWorkingDateModel>() {
                @Override
                protected ShowWorkingDateModel doForward(WorkingDate workingDate) {
                    ShowWorkingDateModel model = new ShowWorkingDateModel();
                    model.setId(workingDate.getId());
                    model.setWorkingDate(workingDate.getWorkingDate());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected WorkingDate doBackward(ShowWorkingDateModel showWorkingDateModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowPlantShipPriceModel> pspPagingConverter(Page<PlantShipPrice> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantShipPriceModel> modelResult = pagingResult.map(new Converter<PlantShipPrice, ShowPlantShipPriceModel>() {
                @Override
                protected ShowPlantShipPriceModel doForward(PlantShipPrice plantShipPrice) {
                    ShowPlantShipPriceModel model = new ShowPlantShipPriceModel();
                    model.setId(plantShipPrice.getId());
                    model.setPotSize(plantShipPrice.getPotSize());
                    model.setPricePerPlant(plantShipPrice.getPricePerPlant());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected PlantShipPrice doBackward(ShowPlantShipPriceModel showPlantShipPriceModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowPlantModel> plantPagingConverter(Page<Plant> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<Plant, ShowPlantModel>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {


                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
                    for(PlantCategory plantCategory : plantCategoryList) {
                        ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                        showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                        showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                        showPlantCategoryList.add(showPlantCategory);
                    }

                    List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
                    List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getId());
                    if(plantIMGList != null) {
                        for(PlantIMG img : plantIMGList) {
                            ShowPlantIMGModel model = new ShowPlantIMGModel();
                            model.setId(img.getId());
                            model.setUrl(img.getImgURL());
                            showPlantIMGList.add(model);
                        }
                    }

                    ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
                    showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
                    showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
                    showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

                    PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
                    ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
                    showPlantPriceModel.setId(newestPrice.getId());
                    showPlantPriceModel.setPrice(newestPrice.getPrice());
                    showPlantPriceModel.setApplyDate(newestPrice.getApplyDate());

                    ShowPlantModel model = new ShowPlantModel();
                    model.setPlantID(plant.getId());
                    model.setName(plant.getName());
                    model.setHeight(plant.getHeight());
                    model.setWithPot(plant.getWithPot());
                    model.setShowPlantShipPriceModel(showPlantShipPriceModel);
                    model.setPlantCategoryList(showPlantCategoryList);
                    model.setShowPlantPriceModel(showPlantPriceModel);
                    model.setPlantIMGList(showPlantIMGList);
                    model.setDescription(plant.getDescription());
                    model.setCareNote(plant.getCareNote());
                    model.setStatus(plant.getStatus());
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

    public List<com.example.thanhhoa.dtos.StoreModels.ShowPlantModel> storePlantPagingConverter(Page<StorePlant> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<com.example.thanhhoa.dtos.StoreModels.ShowPlantModel> modelResult = pagingResult.map(new Converter<StorePlant, com.example.thanhhoa.dtos.StoreModels.ShowPlantModel>() {
                @Override
                protected com.example.thanhhoa.dtos.StoreModels.ShowPlantModel doForward(StorePlant storePlant) {
                    Plant plant = storePlant.getPlant();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
                    for(PlantCategory plantCategory : plantCategoryList) {
                        ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                        showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                        showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                        showPlantCategoryList.add(showPlantCategory);
                    }

                    List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
                    List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getId());
                    if(plantIMGList != null) {
                        for(PlantIMG img : plantIMGList) {
                            ShowPlantIMGModel model = new ShowPlantIMGModel();
                            model.setId(img.getId());
                            model.setUrl(img.getImgURL());
                            showPlantIMGList.add(model);
                        }
                    }

                    ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
                    showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
                    showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
                    showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

                    PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
                    ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
                    showPlantPriceModel.setId(newestPrice.getId());
                    showPlantPriceModel.setPrice(newestPrice.getPrice());
                    showPlantPriceModel.setApplyDate(newestPrice.getApplyDate());

                    com.example.thanhhoa.dtos.StoreModels.ShowPlantModel model = new com.example.thanhhoa.dtos.StoreModels.ShowPlantModel();
                    model.setPlantID(plant.getId());
                    model.setName(plant.getName());
                    model.setHeight(plant.getHeight());
                    model.setWithPot(plant.getWithPot());
                    model.setShowPlantShipPriceModel(showPlantShipPriceModel);
                    model.setPlantCategoryList(showPlantCategoryList);
                    model.setShowPlantPriceModel(showPlantPriceModel);
                    model.setPlantIMGList(showPlantIMGList);
                    model.setDescription(plant.getDescription());
                    model.setCareNote(plant.getCareNote());
                    model.setStatus(plant.getStatus());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected StorePlant doBackward(com.example.thanhhoa.dtos.StoreModels.ShowPlantModel showPlantModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowPlantModel> plantPricePagingConverter(Double fromPrice, Double toPrice, Page<Plant> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<Plant, ShowPlantModel>() {
                @Override
                protected ShowPlantModel doForward(Plant resultPlant) {
                    PlantPrice plant = plantPriceRepository.findByPlant_IdAndPriceBetweenAndStatus(resultPlant.getId(), fromPrice, toPrice, Status.ACTIVE);
                    if(plant != null) {
                        List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getPlant().getId(), Status.ACTIVE);
                        List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
                        for(PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            showPlantCategoryList.add(showPlantCategory);
                        }

                        List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
                        List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getPlant().getId());
                        if(plantIMGList != null) {
                            for(PlantIMG img : plantIMGList) {
                                ShowPlantIMGModel model = new ShowPlantIMGModel();
                                model.setId(img.getId());
                                model.setUrl(img.getImgURL());
                                showPlantIMGList.add(model);
                            }
                        }

                        ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
                        showPlantShipPriceModel.setId(plant.getPlant().getPlantShipPrice().getId());
                        showPlantShipPriceModel.setPotSize(plant.getPlant().getPlantShipPrice().getPotSize());
                        showPlantShipPriceModel.setPricePerPlant(plant.getPlant().getPlantShipPrice().getPricePerPlant());

                        ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
                        showPlantPriceModel.setId(plant.getId());
                        showPlantPriceModel.setPrice(plant.getPrice());
                        showPlantPriceModel.setApplyDate(plant.getApplyDate());

                        ShowPlantModel model = new ShowPlantModel();
                        model.setPlantID(plant.getPlant().getId());
                        model.setName(plant.getPlant().getName());
                        model.setHeight(plant.getPlant().getHeight());
                        model.setWithPot(plant.getPlant().getWithPot());
                        model.setShowPlantShipPriceModel(showPlantShipPriceModel);
                        model.setPlantCategoryList(showPlantCategoryList);
                        model.setShowPlantPriceModel(showPlantPriceModel);
                        model.setPlantIMGList(showPlantIMGList);
                        model.setDescription(plant.getPlant().getDescription());
                        model.setCareNote(plant.getPlant().getCareNote());
                        model.setStatus(plant.getPlant().getStatus());
                        model.setTotalPage(totalPage);
                        return model;
                    }
                    return null;
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
        if(pagingResult.hasContent()) {
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
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowOrderFeedbackModel> modelResult = pagingResult.map(new Converter<OrderFeedback, ShowOrderFeedbackModel>() {
                @Override
                protected ShowOrderFeedbackModel doForward(OrderFeedback orderFeedback) {
                    ShowRatingModel ratingModel = new ShowRatingModel();
                    ratingModel.setId(orderFeedback.getRating().getId());
                    ratingModel.setDescription(orderFeedback.getRating().getDescription());

                    List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
                    for(OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
                        ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
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

                    ShowOrderFeedbackModel model = new ShowOrderFeedbackModel();
                    model.setShowCustomerModel(customerModel);
                    model.setOrderFeedbackID(orderFeedback.getId());
                    model.setDescription(orderFeedback.getDescription());
                    model.setCreatedDate(orderFeedback.getCreatedDate());
                    model.setRatingModel(ratingModel);
                    model.setImgList(imgModelList);
                    model.setStatus(orderFeedback.getStatus());
                    model.setTotalPage(totalPage);
                    model.setShowPlantModel(plantModel);
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
        if(pagingResult.hasContent()) {
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
                    model.setStatus(contractFeedback.getStatus());
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

    public List<ShowStaffModel> staffPagingConverter(Page<StoreEmployee> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowStaffModel> modelResult = pagingResult.map(new Converter<StoreEmployee, ShowStaffModel>() {
                @Override
                protected ShowStaffModel doForward(StoreEmployee storeEmployee) {
                    ShowStaffModel model = new ShowStaffModel();
                    model.setId(storeEmployee.getId().getTblAccount_id());
                    model.setFullName(storeEmployee.getAccount().getFullName());
                    model.setAddress(storeEmployee.getAccount().getAddress());
                    model.setAvatar(storeEmployee.getAccount().getAvatar());
                    model.setGender(storeEmployee.getAccount().getGender());
                    model.setPhone(storeEmployee.getAccount().getPhone());
                    model.setStatus(storeEmployee.getAccount().getStatus());
                    model.setEmail(storeEmployee.getAccount().getFullName());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected StoreEmployee doBackward(ShowStaffModel showStaffModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowServiceModel> servicePagingConverter(Page<Service> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowServiceModel> modelResult = pagingResult.map(new Converter<Service, ShowServiceModel>() {
                @Override
                protected ShowServiceModel doForward(Service service) {
                    List<ShowServiceTypeModel> typeList = new ArrayList<>();
                    List<ServiceType> serviceTypeList = serviceTypeRepository.findByService_IdAndStatus(service.getId(), Status.ACTIVE);
                    if(serviceTypeList != null) {
                        for(ServiceType serviceType : serviceTypeList) {
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

                    List<ShowServiceIMGModel> imgList = new ArrayList<>();
                    List<ServiceIMG> serviceIMGList = serviceIMGRepository.findByService_Id(service.getId());
                    if(serviceIMGList != null) {
                        for(ServiceIMG img : serviceIMGList) {
                            ShowServiceIMGModel imgModel = new ShowServiceIMGModel();
                            imgModel.setId(img.getId());
                            imgModel.setUrl(img.getImgURL());
                            imgList.add(imgModel);
                        }
                    }

                    ShowServiceModel model = new ShowServiceModel();
                    model.setServiceID(service.getId());
                    model.setName(service.getName());
                    model.setPrice(service.getPrice());
                    model.setDescription(service.getDescription());
                    model.setTypeList(typeList);
                    model.setImgList(imgList);
                    model.setStatus(service.getStatus());
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
        if(pagingResult.hasContent()) {
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
                    model.setLatLong(order.getLatLong());
                    model.setDistance(order.getDistance());
                    model.setTotalShipCost(order.getTotalShipCost());
                    model.setTotal(order.getTotal());

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
                    }

                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    customerModel.setId(order.getCustomer().getId());
                    customerModel.setAddress(order.getCustomer().getAddress());
                    customerModel.setEmail(order.getCustomer().getEmail());
                    customerModel.setPhone(order.getCustomer().getPhone());
                    customerModel.setFullName(order.getCustomer().getFullName());

                    //distance price
                    ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
                    distancePriceModel.setId(order.getDistancePrice().getId());
                    distancePriceModel.setApplyDate(order.getDistancePrice().getApplyDate());
                    distancePriceModel.setPricePerKm(order.getDistancePrice().getPricePerKm());

                    //plant
                    List<com.example.thanhhoa.dtos.OrderModels.ShowPlantModel> listPlantModel = new ArrayList<>();
                    for(OrderDetail detail : order.getOrderDetailList()) {
                        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
                        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(detail.getPlant().getId(), Status.ACTIVE);
                        plantModel.setId(detail.getPlant().getId());
                        if(detail.getPlant().getPlantIMGList() != null && !detail.getPlant().getPlantIMGList().isEmpty()) {
                            plantModel.setImage(detail.getPlant().getPlantIMGList().get(0).getImgURL());
                        }
                        plantModel.setQuantity(detail.getQuantity());
                        plantModel.setPlantName(detail.getPlant().getName());
                        plantModel.setPlantPrice(newestPrice.getPrice());
                        plantModel.setShipPrice(detail.getPlant().getPlantShipPrice().getPricePerPlant());
                        listPlantModel.add(plantModel);
                    }

                    model.setShowPlantModel(listPlantModel);
                    model.setShowStaffModel(staffModel);
                    model.setShowStoreModel(storeModel);
                    model.setShowCustomerModel(customerModel);
                    model.setShowDistancePriceModel(distancePriceModel);
                    model.setNumOfPlant(order.getOrderDetailList().size());
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

    public List<ShowContractModel> contractPagingConverter(Page<Contract> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowContractModel> modelResult = pagingResult.map(new Converter<Contract, ShowContractModel>() {
                @Override
                protected ShowContractModel doForward(Contract contract) {
                    List<ContractIMG> imgList = contractIMGRepository.findByContract_Id(contract.getId());
                    List<ShowContractIMGModel> imgModelList = new ArrayList<>();
                    if(imgList != null) {
                        for(ContractIMG img : imgList) {
                            ShowContractIMGModel imgModel = new ShowContractIMGModel();
                            imgModel.setId(img.getId());
                            imgModel.setImgUrl(img.getImgURL());
                            imgModelList.add(imgModel);
                        }
                    }
                    ShowContractModel model = new ShowContractModel();
                    model.setId(contract.getId());
                    model.setFullName(contract.getFullName());
                    model.setAddress(contract.getAddress());
                    model.setEmail(contract.getEmail());
                    model.setPhone(contract.getPhone());
                    model.setTitle(contract.getTitle());
                    model.setPaymentMethod(contract.getPaymentMethod());
                    model.setReason(contract.getReason());
                    model.setCreatedDate(contract.getCreatedDate());
                    model.setApprovedDate(contract.getApprovedDate());
                    model.setRejectedDate(contract.getRejectedDate());
                    model.setStartedDate(contract.getStartedDate());
                    model.setEndedDate(contract.getEndedDate());
                    model.setDeposit(contract.getDeposit());
                    model.setTotal(contract.getTotal());
                    model.setIsFeedback(contract.getIsFeedback());
                    model.setIsSigned(contract.getIsSigned());

                    //store
                    ShowStoreModel storeModel = new ShowStoreModel();
                    storeModel.setId(contract.getStore().getId());
                    storeModel.setStoreName(contract.getStore().getStoreName());
                    storeModel.setAddress(contract.getStore().getAddress());
                    storeModel.setPhone(contract.getStore().getPhone());

                    //staff
                    ShowStaffModel staffModel = new ShowStaffModel();
                    if(contract.getStaff() != null) {
                        staffModel.setId(contract.getStaff().getId());
                        staffModel.setAddress(contract.getStaff().getAddress());
                        staffModel.setEmail(contract.getStaff().getEmail());
                        staffModel.setPhone(contract.getStaff().getPhone());
                        staffModel.setFullName(contract.getStaff().getFullName());
                    }

                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    if(contract.getCustomer() != null) {
                        customerModel.setId(contract.getCustomer().getId());
                        customerModel.setAddress(contract.getCustomer().getAddress());
                        customerModel.setEmail(contract.getCustomer().getEmail());
                        customerModel.setPhone(contract.getCustomer().getPhone());
                        customerModel.setFullName(contract.getCustomer().getFullName());
                    }

                    //payment type
                    ShowPaymentTypeModel paymentTypeModel = new ShowPaymentTypeModel();
                    if(contract.getPaymentType() != null) {
                        paymentTypeModel.setId(contract.getPaymentType().getId());
                        paymentTypeModel.setName(contract.getPaymentType().getName());
                        paymentTypeModel.setValue(contract.getPaymentType().getValue());
                    }

                    model.setShowStaffModel(staffModel);
                    model.setShowCustomerModel(customerModel);
                    model.setShowStoreModel(storeModel);
                    model.setShowPaymentTypeModel(paymentTypeModel);
                    model.setStatus(contract.getStatus());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Contract doBackward(ShowContractModel showContractModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowContractDetailModel> contractDetailPagingConverter(Page<ContractDetail> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowContractDetailModel> modelResult = pagingResult.map(new Converter<ContractDetail, ShowContractDetailModel>() {
                @Override
                protected ShowContractDetailModel doForward(ContractDetail detail) {
                    List<WorkingDate> dateList = workingDateRepository.findByContractDetail_Id(detail.getId());
                    List<com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel> dateModelList = new ArrayList<>();
                    for(WorkingDate workingDate : dateList) {
                        com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel model = new com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel();
                        model.setId(workingDate.getId());
                        model.setWorkingDate(workingDate.getWorkingDate());
                        dateModelList.add(model);
                    }
                    ShowContractDetailModel model = new ShowContractDetailModel();
                    model.setId(detail.getId());
                    model.setNote(detail.getNote());
                    model.setTimeWorking(detail.getTimeWorking());
                    model.setEndDate(detail.getEndDate());
                    model.setStartDate(detail.getStartDate());
                    model.setTotalPrice(detail.getTotalPrice());

                    //contract
                    List<ContractIMG> imgList = contractIMGRepository.findByContract_Id(detail.getContract().getId());
                    List<ShowContractIMGModel> imgModelList = new ArrayList<>();
                    if(imgList != null) {
                        for(ContractIMG img : imgList) {
                            ShowContractIMGModel imgModel = new ShowContractIMGModel();
                            imgModel.setId(img.getId());
                            imgModel.setImgUrl(img.getImgURL());
                            imgModelList.add(imgModel);
                        }
                    }
                    ShowContractModel contractModel = new ShowContractModel();
                    contractModel.setId(detail.getContract().getId());
                    contractModel.setAddress(detail.getContract().getAddress());
                    contractModel.setPhone(detail.getContract().getPhone());
                    contractModel.setFullName(detail.getContract().getFullName());
                    contractModel.setEmail(detail.getContract().getEmail());
                    contractModel.setTitle(detail.getContract().getTitle());
                    contractModel.setPaymentMethod(detail.getContract().getPaymentMethod());
                    contractModel.setCreatedDate(detail.getContract().getCreatedDate());
                    contractModel.setStartedDate(detail.getContract().getStartedDate());
                    contractModel.setApprovedDate(detail.getContract().getApprovedDate());
                    contractModel.setRejectedDate(detail.getContract().getRejectedDate());
                    contractModel.setEndedDate(detail.getContract().getEndedDate());
                    contractModel.setDeposit(detail.getContract().getDeposit());
                    contractModel.setTotal(detail.getContract().getTotal());
                    contractModel.setIsFeedback(detail.getContract().getIsFeedback());
                    contractModel.setIsSigned(detail.getContract().getIsSigned());
                    contractModel.setStatus(detail.getContract().getStatus());
                    contractModel.setReason(detail.getContract().getReason());
                    contractModel.setImgList(imgModelList);

                    //service type
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel serviceTypeModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel();
                    serviceTypeModel.setId(detail.getServiceType().getId());
                    serviceTypeModel.setTypeName(detail.getServiceType().getName());
                    serviceTypeModel.setTypeSize(detail.getServiceType().getSize());
                    serviceTypeModel.setTypePercentage(detail.getServiceType().getPercentage());
                    serviceTypeModel.setTypeApplyDate(detail.getServiceType().getApplyDate());

                    //service pack
                    ShowServicePackModel servicePackModel = new ShowServicePackModel();
                    servicePackModel.setId(detail.getServicePack().getId());
                    servicePackModel.setPackPercentage(detail.getServicePack().getPercentage());
                    servicePackModel.setPackRange(detail.getServicePack().getRange());
                    servicePackModel.setPackApplyDate(detail.getServicePack().getApplyDate());

                    //service
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceModel serviceModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceModel();
                    serviceModel.setId(detail.getServiceType().getService().getId());
                    serviceModel.setDescription(detail.getServiceType().getService().getDescription());
                    serviceModel.setPrice(detail.getServiceType().getService().getPrice());
                    serviceModel.setName(detail.getServiceType().getService().getName());

                    model.setShowContractModel(contractModel);
                    model.setShowServiceModel(serviceModel);
                    model.setShowServicePackModel(servicePackModel);
                    model.setShowServiceTypeModel(serviceTypeModel);
                    model.setWorkingDateList(dateModelList);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected ContractDetail doBackward(ShowContractDetailModel showContractDetailModel) {
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

    public String createIDFromLastID(String chars, Integer index, String lastID) {
        Integer IDNumber = Integer.parseInt(lastID.substring(index));
        IDNumber++;
        String newID = chars + String.format("%03d", IDNumber);
        return newID;
    }

    public LocalDateTime isDateValid(String date) {
        date += " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date, formatter);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ShowOrderDetailModel returnOrderDetailModelList(OrderDetail orderDetail) {
        ShowOrderDetailModel model = new ShowOrderDetailModel();
        //detail
        model.setId(orderDetail.getId());
        model.setPrice(orderDetail.getPrice());
        model.setQuantity(orderDetail.getQuantity());
        model.setIsFeedback(orderDetail.getIsFeedback());

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

        //customer
        ShowCustomerModel customerModel = new ShowCustomerModel();
        customerModel.setId(orderDetail.getTblOrder().getCustomer().getId());
        customerModel.setAddress(orderDetail.getTblOrder().getCustomer().getAddress());
        customerModel.setEmail(orderDetail.getTblOrder().getCustomer().getEmail());
        customerModel.setPhone(orderDetail.getTblOrder().getCustomer().getPhone());
        customerModel.setFullName(orderDetail.getTblOrder().getCustomer().getFullName());
        customerModel.setAvatar(orderDetail.getTblOrder().getCustomer().getAvatar());

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
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(orderDetail.getPlant().getId(), Status.ACTIVE);
        plantModel.setId(orderDetail.getPlant().getId());
        if(orderDetail.getPlant().getPlantIMGList() != null && !orderDetail.getPlant().getPlantIMGList().isEmpty()) {
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
        return model;
    }

    public List<ShowOrderDetailModel> orderDetailPagingConverter(Page<OrderDetail> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowOrderDetailModel> modelResult = pagingResult.map(new Converter<OrderDetail, ShowOrderDetailModel>() {
                @Override
                protected ShowOrderDetailModel doForward(OrderDetail orderDetail) {
                    ShowOrderDetailModel model = new ShowOrderDetailModel();
                    ShowOrderFeedbackModel feedbackModel = new ShowOrderFeedbackModel();
                    //feedback
                    if(orderDetail.getIsFeedback() != null) {
                        model.setIsFeedback(true);
                        if(orderDetail.getIsFeedback() == true) {
                            OrderFeedback orderFeedback = orderFeedbackRepository.findByOrderDetail_Id(orderDetail.getId());
                            ShowRatingModel ratingModel = new ShowRatingModel();
                            ratingModel.setId(orderFeedback.getRating().getId());
                            ratingModel.setDescription(orderFeedback.getRating().getDescription());

                            List<ShowOrderFeedbackIMGModel> imgModelList = new ArrayList<>();
                            for(OrderFeedbackIMG img : orderFeedback.getOrderFeedbackIMGList()) {
                                ShowOrderFeedbackIMGModel imgModel = new ShowOrderFeedbackIMGModel();
                                imgModel.setId(img.getId());
                                imgModel.setUrl(img.getImgURL());
                                imgModelList.add(imgModel);
                            }

                            //plant
                            com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
                            plantModel.setId(orderFeedback.getPlant().getId());
                            if(orderFeedback.getPlant().getPlantIMGList() != null && !orderFeedback.getPlant().getPlantIMGList().isEmpty()) {
                                plantModel.setImage(orderFeedback.getPlant().getPlantIMGList().get(0).getImgURL());
                            }
                            plantModel.setPlantName(orderFeedback.getPlant().getName());

                            feedbackModel.setOrderFeedbackID(orderFeedback.getId());
                            feedbackModel.setDescription(orderFeedback.getDescription());
                            feedbackModel.setCreatedDate(orderFeedback.getCreatedDate());
                            feedbackModel.setRatingModel(ratingModel);
                            feedbackModel.setImgList(imgModelList);
                            feedbackModel.setStatus(orderFeedback.getStatus());
                            feedbackModel.setShowPlantModel(plantModel);
                        }
                    } else {
                        model.setIsFeedback(false);
                    }

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

                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    customerModel.setId(orderDetail.getTblOrder().getCustomer().getId());
                    customerModel.setAddress(orderDetail.getTblOrder().getCustomer().getAddress());
                    customerModel.setEmail(orderDetail.getTblOrder().getCustomer().getEmail());
                    customerModel.setPhone(orderDetail.getTblOrder().getCustomer().getPhone());
                    customerModel.setFullName(orderDetail.getTblOrder().getCustomer().getFullName());
                    customerModel.setAvatar(orderDetail.getTblOrder().getCustomer().getAvatar());

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
                    com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
                    PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(orderDetail.getPlant().getId(), Status.ACTIVE);
                    plantModel.setId(orderDetail.getPlant().getId());
                    if(orderDetail.getPlant().getPlantIMGList() != null && !orderDetail.getPlant().getPlantIMGList().isEmpty()) {
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
                    model.setShowOrderFeedbackModel(feedbackModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected OrderDetail doBackward(ShowOrderDetailModel showOrderDetailModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public void getSetRatingFeedbackForModelList(List<ShowOrderFeedbackModel> modelList) {
        Double totalRating = 0.0;
        Double totalFeedback = 0.0;
        Double avgRatingFeedback = 0.0;
        if(modelList != null && !modelList.isEmpty()) {
            List<OrderFeedback> list = orderFeedbackRepository.findAllByStatusAndPlant_Id(Status.ACTIVE, modelList.get(0).getShowPlantModel().getId());
            for(OrderFeedback orderFeedback : list) {
                totalRating += Double.parseDouble(orderFeedback.getRating().getDescription());
            }
            totalFeedback = Double.valueOf(list.size());
        }

        if(totalRating > 0.0 && totalFeedback > 0.0) {
            avgRatingFeedback = totalRating / totalFeedback;
        }

        for(ShowOrderFeedbackModel model : modelList) {
            model.setTotalFeedback(totalFeedback);
            model.setTotalRating(totalRating);
            model.setAvgRatingFeedback(avgRatingFeedback);
        }
    }

    public void getSetRatingFeedbackForModel(OrderFeedback orderFeedback, ShowOrderFeedbackModel model) {
        Double totalRating = 0.0;
        Double totalFeedback = 0.0;
        Double avgRatingFeedback = 0.0;
        List<OrderFeedback> list = orderFeedbackRepository.findAllByStatusAndPlant_Id(Status.ACTIVE, orderFeedback.getPlant().getId());
        if(list != null && !list.isEmpty()) {
            for(OrderFeedback ofb : list) {
                totalRating += Double.parseDouble(ofb.getRating().getDescription());
            }
            totalFeedback = Double.valueOf(list.size());
        }

        if(totalRating > 0.0 && totalFeedback > 0.0) {
            avgRatingFeedback = totalRating / totalFeedback;
        }

        model.setTotalFeedback(totalFeedback);
        model.setTotalRating(totalRating);
        model.setAvgRatingFeedback(avgRatingFeedback);

    }

//    class InvalidInput extends Exception {
//        public InvalidInput(String input) {
//            super(input);
//        }
//    }
}
