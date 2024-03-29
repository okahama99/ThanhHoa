package com.example.thanhhoa.utils;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowPlantStatusIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServicePackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackIMGModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.NotificationModels.CreateNotificationModel;
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
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceIMGModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ServicePriceModels.ShowServicePriceModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantModel;
import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.ContractFeedback;
import com.example.thanhhoa.entities.ContractIMG;
import com.example.thanhhoa.entities.Notification;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.entities.PlantStatusIMG;
import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceIMG;
import com.example.thanhhoa.entities.ServicePack;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.Transaction;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractIMGRepository;
import com.example.thanhhoa.repositories.NotificationRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.ServiceIMGRepository;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.services.firebase.FirebaseMessagingService;
import com.google.common.base.Converter;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private NotificationRepository notificationRepository;

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
                    ContractDetail detail = workingDate.getContractDetail();
                    ShowWorkingDateModel model = new ShowWorkingDateModel();
                    model.setId(workingDate.getId());
                    model.setNoteWorkingDate(workingDate.getNote());
                    model.setWorkingDate(workingDate.getWorkingDate());
                    model.setIsReported(workingDate.getIsReported());
                    model.setStartWorking(workingDate.getStartWorking());
                    model.setEndWorking(workingDate.getEndWorking());
                    model.setStartWorkingIMG(workingDate.getStartWorkingIMG());
                    model.setEndWorkingIMG(workingDate.getEndWorkingIMG());
                    model.setStatus(workingDate.getStatus());
                    model.setContractDetailID(detail.getId());
                    model.setNote(detail.getNote());
                    model.setTimeWorking(detail.getTimeWorking());
                    model.setEndDate(detail.getEndDate());
                    model.setStartDate(detail.getStartDate());
                    model.setExpectedEndDate(detail.getExpectedEndDate());
                    model.setPlantStatus(detail.getPlantStatus());
                    model.setPlantName(detail.getPlantName());

                    List<ShowPlantStatusIMGModel> plantStatusIMGModelList = new ArrayList<>();
                    if(detail.getPlantStatusIMGList() != null && !detail.getPlantStatusIMGList().isEmpty()){
                        for(PlantStatusIMG plantStatusIMG : detail.getPlantStatusIMGList()){
                            ShowPlantStatusIMGModel plantStatusIMGModel = new ShowPlantStatusIMGModel();
                            plantStatusIMGModel.setId(plantStatusIMG.getId());
                            plantStatusIMGModel.setImgUrl(plantStatusIMG.getImgURL());
                            plantStatusIMGModelList.add(plantStatusIMGModel);
                        }
                    }
                    model.setPlantStatusIMGModelList(plantStatusIMGModelList);

                    model.setContractID(detail.getContract().getId());
                    model.setTitle(detail.getContract().getTitle());
                    model.setAddress(detail.getContract().getAddress());
                    model.setEmail(detail.getContract().getEmail());
                    model.setPhone(detail.getContract().getPhone());
                    model.setFullName(detail.getContract().getFullName());
                    model.setServiceID(detail.getServiceType().getService().getId());
                    model.setServiceName(detail.getServiceType().getService().getName());
                    model.setServiceTypeID(detail.getServiceType().getId());
                    model.setTypeName(detail.getServiceType().getName());
                    model.setTypeSize(detail.getServiceType().getSize());
                    model.setTypeUnit(detail.getServiceType().getUnit());
                    model.setTypePercentage(detail.getServiceType().getPercentage());
                    model.setTypeApplyDate(detail.getServiceType().getApplyDate());
                    model.setServicePackID(detail.getServicePack().getId());
                    model.setPackRange(detail.getServicePack().getRange());
                    model.setPackUnit(detail.getServicePack().getUnit());
                    model.setPackPercentage(detail.getServicePack().getPercentage());
                    model.setPackApplyDate(detail.getServicePack().getApplyDate());

                    ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(detail.getServiceType().getService().getId(), Status.ACTIVE);

//                    // calculate month from date range
//                    Long monthsBetween = ChronoUnit.MONTHS.between(
//                            detail.getStartDate().withDayOfMonth(1),
//                            detail.getExpectedEndDate().withDayOfMonth(1));

                    Double months = getMonthsBetween(detail.getServicePack());
                    Double price = newestPrice.getPrice();
                    Double typePercentage = detail.getServiceType().getPercentage().doubleValue();
                    Double packPercentage = detail.getServicePack().getPercentage().doubleValue();

                    Double totalPrice = (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
                    model.setTotalPrice(totalPrice);

                    //staff
                    ShowStaffModel staffModel = new ShowStaffModel();
                    if(workingDate.getStaff() != null) {
                        staffModel.setId(workingDate.getStaff().getId());
                        staffModel.setAddress(workingDate.getStaff().getAddress());
                        staffModel.setEmail(workingDate.getStaff().getEmail());
                        staffModel.setPhone(workingDate.getStaff().getPhone());
                        staffModel.setFullName(workingDate.getStaff().getFullName());
                        staffModel.setAvatar(workingDate.getStaff().getAvatar());
                    }

                    model.setShowStaffModel(staffModel);
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
                    showPlantPriceModel.setStatus(newestPrice.getStatus());

                    Integer totalPlant = storePlantRepository.sumQuantity(plant.getId());
                    Double totalRating = 0.0;
                    Double totalFeedback = 0.0;
                    Double avgRatingFeedback = 0.0;
                    List<OrderFeedback> list = orderFeedbackRepository.findAllByStatusAndPlant_Id(Status.ACTIVE, plant.getId());
                    if(list != null && !list.isEmpty()) {
                        for(OrderFeedback ofb : list) {
                            totalRating += Double.parseDouble(ofb.getRating().getDescription());
                        }
                        totalFeedback = Double.valueOf(list.size());
                    }

                    if(totalRating > 0.0 && totalFeedback > 0.0) {
                        avgRatingFeedback = totalRating / totalFeedback;
                    }

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
                    model.setTotalRating(totalRating);
                    model.setTotalFeedback(totalRating);
                    model.setAvgRatingFeedback(avgRatingFeedback);
                    model.setTotalPlant(totalPlant);
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
                    showPlantPriceModel.setStatus(newestPrice.getStatus());

                    ShowStorePlantModel storePlantModel = new ShowStorePlantModel();
                    storePlantModel.setId(storePlant.getId());
                    storePlantModel.setQuantity(storePlant.getQuantity());

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
                    model.setShowStorePlantModel(storePlantModel);
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

    public List<ShowPlantModel> plantPagingConverterV2(String storeID, Page<Plant> pagingResult, Pageable paging) {
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
                    showPlantPriceModel.setStatus(newestPrice.getStatus());

                    Integer totalPlant = storePlantRepository.sumQuantity(plant.getId());
                    Double totalRating = 0.0;
                    Double totalFeedback = 0.0;
                    Double avgRatingFeedback = 0.0;
                    List<OrderFeedback> list = orderFeedbackRepository.findAllByStatusAndPlant_Id(Status.ACTIVE, plant.getId());
                    if(list != null && !list.isEmpty()) {
                        for(OrderFeedback ofb : list) {
                            totalRating += Double.parseDouble(ofb.getRating().getDescription());
                        }
                        totalFeedback = Double.valueOf(list.size());
                    }

                    if(totalRating > 0.0 && totalFeedback > 0.0) {
                        avgRatingFeedback = totalRating / totalFeedback;
                    }

                    StorePlant storePlant = storePlantRepository.findByPlantIdAndStoreId(plant.getId(), storeID);
                    ShowStorePlantModel storePlantModel = new ShowStorePlantModel();
                    if(storePlant != null){
                        storePlantModel.setId(storePlant.getId());
                        storePlantModel.setQuantity(storePlant.getQuantity());
                    }

                    ShowPlantModel model = new ShowPlantModel();

                    model.setTotalRating(totalRating);
                    model.setTotalFeedback(totalRating);
                    model.setAvgRatingFeedback(avgRatingFeedback);
                    model.setTotalPlant(totalPlant);

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
                    model.setShowStorePlantModel(storePlantModel);
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
                        showPlantPriceModel.setStatus(plant.getStatus());

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
                    model.setStatus(category.getStatus());
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

    public List<ShowPlantPriceModel> plantPriceModelPagingConverter(Page<PlantPrice> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantPriceModel> modelResult = pagingResult.map(new Converter<PlantPrice, ShowPlantPriceModel>() {
                @Override
                protected ShowPlantPriceModel doForward(PlantPrice plantPrice) {
                    ShowPlantPriceModel model = new ShowPlantPriceModel();
                    model.setId(plantPrice.getId());
                    model.setPrice(plantPrice.getPrice());
                    model.setApplyDate(plantPrice.getApplyDate());
                    model.setStatus(plantPrice.getStatus());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected PlantPrice doBackward(ShowPlantPriceModel showPlantPriceModel) {
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
                    if(orderFeedback.getCustomer() != null) {
                        customerModel.setId(orderFeedback.getCustomer().getId());
                        customerModel.setAddress(orderFeedback.getCustomer().getAddress());
                        customerModel.setEmail(orderFeedback.getCustomer().getEmail());
                        customerModel.setPhone(orderFeedback.getCustomer().getPhone());
                        customerModel.setFullName(orderFeedback.getCustomer().getFullName());
                        customerModel.setAvatar(orderFeedback.getCustomer().getAvatar());
                    }

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
                    model.setTotalPage(totalPage);
                    model.setShowPlantModel(plantModel);
                    model.setShowOrderModel(orderModel);
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

    public List<com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel> serviceTypePagingConverter(Page<ServiceType> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel> modelResult = pagingResult.map(new Converter<ServiceType, com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel>() {
                @Override
                protected com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel doForward(ServiceType serviceType) {

                    // service
                    Service service = serviceType.getService();
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceModel serviceModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceModel();
                    serviceModel.setId(service.getId());
                    serviceModel.setName(service.getName());
                    serviceModel.setDescription(service.getDescription());
                    serviceModel.setAtHome(service.getAtHome());

                    com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel model = new com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel();
                    model.setId(serviceType.getId());
                    model.setName(serviceType.getName());
                    model.setApplyDate(serviceType.getApplyDate());
                    model.setSize(serviceType.getSize());
                    model.setUnit(serviceType.getUnit());
                    model.setPercentage(serviceType.getPercentage());
                    model.setStatus(serviceType.getStatus());
                    model.setShowServiceModel(serviceModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected ServiceType doBackward(com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel showServiceTypeModel) {
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
                            typeModel.setUnit(serviceType.getUnit());
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

                    ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(service.getId(), Status.ACTIVE);
                    ShowServiceModel model = new ShowServiceModel();
                    model.setServiceID(service.getId());
                    model.setName(service.getName());
                    model.setPriceID(newestPrice.getId());
                    model.setPrice(newestPrice.getPrice());
                    model.setDescription(service.getDescription());
                    model.setTypeList(typeList);
                    model.setImgList(imgList);
                    model.setStatus(service.getStatus());
                    model.setAtHome(service.getAtHome());
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

    public List<ShowServicePriceModel> servicePricePagingConverter(Page<ServicePrice> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowServicePriceModel> modelResult = pagingResult.map(new Converter<ServicePrice, ShowServicePriceModel>() {
                @Override
                protected ShowServicePriceModel doForward(ServicePrice servicePrice) {
                    ShowServicePriceModel model = new ShowServicePriceModel();

                    //service
                    Service service = servicePrice.getService();
                    ShowServiceModel serviceModel = new ShowServiceModel();
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
                            typeModel.setUnit(serviceType.getUnit());
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
                    serviceModel.setServiceID(service.getId());
                    serviceModel.setName(service.getName());
                    ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(service.getId(), Status.ACTIVE);
                    serviceModel.setPriceID(newestPrice.getId());
                    serviceModel.setPrice(newestPrice.getPrice());
                    serviceModel.setDescription(service.getDescription());
                    serviceModel.setTypeList(typeList);
                    serviceModel.setImgList(imgList);
                    serviceModel.setStatus(service.getStatus());
                    serviceModel.setAtHome(service.getAtHome());

                    model.setId(servicePrice.getId());
                    model.setApplyDate(servicePrice.getApplyDate());
                    model.setStatus(servicePrice.getStatus());
                    model.setPrice(servicePrice.getPrice());
                    model.setServiceModel(serviceModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected ServicePrice doBackward(ShowServicePriceModel showServicePriceModel) {
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
                    model.setIsPaid(order.getIsPaid());
                    model.setIsRefund(order.getIsRefund());
                    model.setReceiptIMG(order.getReceiptIMG());

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

                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    if(order.getCustomer() != null) {
                        customerModel.setId(order.getCustomer().getId());
                        customerModel.setAddress(order.getCustomer().getAddress());
                        customerModel.setEmail(order.getCustomer().getEmail());
                        customerModel.setPhone(order.getCustomer().getPhone());
                        customerModel.setFullName(order.getCustomer().getFullName());
                    }

                    //distance price
                    ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
                    distancePriceModel.setId(order.getDistancePrice().getId());
                    distancePriceModel.setApplyDate(order.getDistancePrice().getApplyDate());
                    distancePriceModel.setPricePerKm(order.getDistancePrice().getPricePerKm());

                    //plant
                    List<com.example.thanhhoa.dtos.OrderModels.ShowPlantModel> listPlantModel = new ArrayList<>();
                    for(OrderDetail detail : order.getOrderDetailList()) {
                        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
                        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(detail.getStorePlant().getPlant().getId(), Status.ACTIVE);
                        plantModel.setId(detail.getStorePlant().getPlant().getId());
                        if(detail.getStorePlant().getPlant().getPlantIMGList() != null && !detail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
                            plantModel.setImage(detail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
                        }
                        plantModel.setQuantity(detail.getQuantity());
                        plantModel.setPlantName(detail.getStorePlant().getPlant().getName());
                        plantModel.setPlantPriceID(newestPrice.getId());
                        plantModel.setPlantPrice(newestPrice.getPrice());
                        plantModel.setShipPrice(detail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());
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
                    model.setConfirmedDate(contract.getConfirmedDate());
                    model.setSignedDate(contract.getSignedDate());
                    model.setApprovedDate(contract.getApprovedDate());
                    model.setRejectedDate(contract.getRejectedDate());
                    model.setStartedDate(contract.getStartedDate());
                    model.setEndedDate(contract.getEndedDate());
                    model.setExpectedEndedDate(contract.getExpectedEndedDate());
                    model.setTotal(contract.getTotal());
                    model.setIsFeedback(contract.getIsFeedback());
                    model.setIsSigned(contract.getIsSigned());
                    model.setIsPaid(contract.getIsPaid());

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
                        staffModel.setAvatar(contract.getStaff().getAvatar());
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

                    model.setShowStaffModel(staffModel);
                    model.setShowCustomerModel(customerModel);
                    model.setShowStoreModel(storeModel);
                    model.setStatus(contract.getStatus());
                    model.setImgList(imgModelList);
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
                    List<WorkingDate> dateList = workingDateRepository.findByContractDetail_IdOrderByWorkingDateDesc(detail.getId());
                    List<com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel> dateModelList = new ArrayList<>();
                    for(WorkingDate workingDate : dateList) {
                        com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel model = new com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel();
                        model.setId(workingDate.getId());
                        model.setWorkingDate(workingDate.getWorkingDate());
                        model.setIsReported(workingDate.getIsReported());
                        model.setStatus(workingDate.getStatus());
                        dateModelList.add(model);
                    }
                    ShowContractDetailModel model = new ShowContractDetailModel();
                    model.setId(detail.getId());
                    model.setNote(detail.getNote());
                    model.setTimeWorking(detail.getTimeWorking());
                    model.setEndDate(detail.getEndDate());
                    model.setExpectedEndDate(detail.getExpectedEndDate());
                    model.setPlantStatus(detail.getPlantStatus());
                    model.setPlantName(detail.getPlantName());

                    List<ShowPlantStatusIMGModel> plantStatusIMGModelList = new ArrayList<>();
                    if(detail.getPlantStatusIMGList() != null && !detail.getPlantStatusIMGList().isEmpty()){
                        for(PlantStatusIMG plantStatusIMG : detail.getPlantStatusIMGList()){
                            ShowPlantStatusIMGModel plantStatusIMGModel = new ShowPlantStatusIMGModel();
                            plantStatusIMGModel.setId(plantStatusIMG.getId());
                            plantStatusIMGModel.setImgUrl(plantStatusIMG.getImgURL());
                            plantStatusIMGModelList.add(plantStatusIMGModel);
                        }
                    }
                    model.setPlantStatusIMGModelList(plantStatusIMGModelList);

                    model.setStartDate(detail.getStartDate());
                    model.setPrice(detail.getPrice());

//                    // calculate month from date range
//                    Long monthsBetween = ChronoUnit.MONTHS.between(
//                            detail.getStartDate().withDayOfMonth(1),
//                            detail.getExpectedEndDate().withDayOfMonth(1));

                    Double months = getMonthsBetween(detail.getServicePack());
                    Double price = detail.getPrice();
                    Double typePercentage = detail.getServiceType().getPercentage().doubleValue();
                    Double packPercentage = detail.getServicePack().getPercentage().doubleValue();

                    Double totalPrice = (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
                    model.setTotalPrice(totalPrice);

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
                    //store
                    ShowStoreModel storeModel = new ShowStoreModel();
                    storeModel.setId(detail.getContract().getStore().getId());
                    storeModel.setStoreName(detail.getContract().getStore().getStoreName());
                    storeModel.setAddress(detail.getContract().getStore().getAddress());
                    storeModel.setPhone(detail.getContract().getStore().getPhone());
                    //staff
                    ShowStaffModel staffModel = new ShowStaffModel();
                    if(detail.getContract().getStaff() != null) {
                        staffModel.setId(detail.getContract().getStaff().getId());
                        staffModel.setAddress(detail.getContract().getStaff().getAddress());
                        staffModel.setEmail(detail.getContract().getStaff().getEmail());
                        staffModel.setPhone(detail.getContract().getStaff().getPhone());
                        staffModel.setFullName(detail.getContract().getStaff().getFullName());
                        staffModel.setAvatar(detail.getContract().getStaff().getAvatar());
                    }
                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    if(detail.getContract().getCustomer() != null) {
                        customerModel.setId(detail.getContract().getCustomer().getId());
                        customerModel.setAddress(detail.getContract().getCustomer().getAddress());
                        customerModel.setEmail(detail.getContract().getCustomer().getEmail());
                        customerModel.setPhone(detail.getContract().getCustomer().getPhone());
                        customerModel.setFullName(detail.getContract().getCustomer().getFullName());
                        customerModel.setAvatar(detail.getContract().getCustomer().getAvatar());
                    }

                    contractModel.setShowCustomerModel(customerModel);
                    contractModel.setShowStaffModel(staffModel);
                    contractModel.setShowStoreModel(storeModel);
                    contractModel.setId(detail.getContract().getId());
                    contractModel.setAddress(detail.getContract().getAddress());
                    contractModel.setPhone(detail.getContract().getPhone());
                    contractModel.setFullName(detail.getContract().getFullName());
                    contractModel.setEmail(detail.getContract().getEmail());
                    contractModel.setTitle(detail.getContract().getTitle());
                    contractModel.setPaymentMethod(detail.getContract().getPaymentMethod());
                    contractModel.setCreatedDate(detail.getContract().getCreatedDate());
                    contractModel.setConfirmedDate(detail.getContract().getConfirmedDate());
                    contractModel.setSignedDate(detail.getContract().getSignedDate());
                    contractModel.setStartedDate(detail.getContract().getStartedDate());
                    contractModel.setApprovedDate(detail.getContract().getApprovedDate());
                    contractModel.setRejectedDate(detail.getContract().getRejectedDate());
                    contractModel.setEndedDate(detail.getContract().getEndedDate());
                    contractModel.setExpectedEndedDate(detail.getContract().getExpectedEndedDate());
                    contractModel.setTotal(detail.getContract().getTotal());
                    contractModel.setIsFeedback(detail.getContract().getIsFeedback());
                    contractModel.setIsSigned(detail.getContract().getIsSigned());
                    contractModel.setStatus(detail.getContract().getStatus());
                    contractModel.setReason(detail.getContract().getReason());
                    contractModel.setImgList(imgModelList);
                    contractModel.setIsPaid(detail.getContract().getIsPaid());

                    //service type
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel serviceTypeModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel();
                    serviceTypeModel.setId(detail.getServiceType().getId());
                    serviceTypeModel.setTypeName(detail.getServiceType().getName());
                    serviceTypeModel.setTypeSize(detail.getServiceType().getSize());
                    serviceTypeModel.setTypeUnit(detail.getServiceType().getUnit());
                    serviceTypeModel.setTypePercentage(detail.getServiceType().getPercentage());
                    serviceTypeModel.setTypeApplyDate(detail.getServiceType().getApplyDate());

                    //service pack
                    ShowServicePackModel servicePackModel = new ShowServicePackModel();
                    servicePackModel.setId(detail.getServicePack().getId());
                    servicePackModel.setPackPercentage(detail.getServicePack().getPercentage());
                    servicePackModel.setPackRange(detail.getServicePack().getRange());
                    servicePackModel.setPackApplyDate(detail.getServicePack().getApplyDate());
                    servicePackModel.setPackUnit(detail.getServicePack().getUnit());

                    //service
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceModel serviceModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceModel();
                    serviceModel.setId(detail.getServiceType().getService().getId());
                    serviceModel.setDescription(detail.getServiceType().getService().getDescription());
                    serviceModel.setName(detail.getServiceType().getService().getName());
                    serviceModel.setAtHome(detail.getServiceType().getService().getAtHome());

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

    public LocalDateTime isLocalDateTimeValid(String date) {
        date += " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date, formatter);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LocalDate isLocalDateValid(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
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
        orderModel.setIsPaid(orderDetail.getTblOrder().getIsPaid());
        orderModel.setIsRefund(orderDetail.getTblOrder().getIsRefund());
        orderModel.setReceiptIMG(orderDetail.getTblOrder().getReceiptIMG());

        //customer
        ShowCustomerModel customerModel = new ShowCustomerModel();
        if(orderDetail.getTblOrder().getCustomer() != null) {
            customerModel.setId(orderDetail.getTblOrder().getCustomer().getId());
            customerModel.setAddress(orderDetail.getTblOrder().getCustomer().getAddress());
            customerModel.setEmail(orderDetail.getTblOrder().getCustomer().getEmail());
            customerModel.setPhone(orderDetail.getTblOrder().getCustomer().getPhone());
            customerModel.setFullName(orderDetail.getTblOrder().getCustomer().getFullName());
            customerModel.setAvatar(orderDetail.getTblOrder().getCustomer().getAvatar());
        }

        //staff
        ShowStaffModel staffModel = new ShowStaffModel();
        if(orderDetail.getTblOrder().getStaff() != null) {
            staffModel.setId(orderDetail.getTblOrder().getStaff().getId());
            staffModel.setAddress(orderDetail.getTblOrder().getStaff().getAddress());
            staffModel.setEmail(orderDetail.getTblOrder().getStaff().getEmail());
            staffModel.setPhone(orderDetail.getTblOrder().getStaff().getPhone());
            staffModel.setFullName(orderDetail.getTblOrder().getStaff().getFullName());
            staffModel.setAvatar(orderDetail.getTblOrder().getStaff().getAvatar());
        }

        //distance price
        ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
        distancePriceModel.setId(orderDetail.getTblOrder().getDistancePrice().getId());
        distancePriceModel.setApplyDate(orderDetail.getTblOrder().getDistancePrice().getApplyDate());
        distancePriceModel.setPricePerKm(orderDetail.getTblOrder().getDistancePrice().getPricePerKm());

        //plant
        com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(orderDetail.getStorePlant().getPlant().getId(), Status.ACTIVE);
        plantModel.setId(orderDetail.getStorePlant().getPlant().getId());
        if(orderDetail.getStorePlant().getPlant().getPlantIMGList() != null && !orderDetail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
            plantModel.setImage(orderDetail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
        }
        plantModel.setQuantity(orderDetail.getQuantity());
        plantModel.setPlantName(orderDetail.getStorePlant().getPlant().getName());
        plantModel.setPlantPriceID(newestPrice.getId());
        plantModel.setPlantPrice(newestPrice.getPrice());
        plantModel.setShipPrice(orderDetail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());

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
                    orderModel.setIsPaid(orderDetail.getTblOrder().getIsPaid());
                    orderModel.setIsRefund(orderDetail.getTblOrder().getIsRefund());
                    orderModel.setReceiptIMG(orderDetail.getTblOrder().getReceiptIMG());

                    //customer
                    ShowCustomerModel customerModel = new ShowCustomerModel();
                    if(orderDetail.getTblOrder().getCustomer() != null) {
                        customerModel.setId(orderDetail.getTblOrder().getCustomer().getId());
                        customerModel.setAddress(orderDetail.getTblOrder().getCustomer().getAddress());
                        customerModel.setEmail(orderDetail.getTblOrder().getCustomer().getEmail());
                        customerModel.setPhone(orderDetail.getTblOrder().getCustomer().getPhone());
                        customerModel.setFullName(orderDetail.getTblOrder().getCustomer().getFullName());
                        customerModel.setAvatar(orderDetail.getTblOrder().getCustomer().getAvatar());
                    }

                    //staff
                    ShowStaffModel staffModel = new ShowStaffModel();
                    if(orderDetail.getTblOrder().getStaff() != null) {
                        staffModel.setId(orderDetail.getTblOrder().getStaff().getId());
                        staffModel.setAddress(orderDetail.getTblOrder().getStaff().getAddress());
                        staffModel.setEmail(orderDetail.getTblOrder().getStaff().getEmail());
                        staffModel.setPhone(orderDetail.getTblOrder().getStaff().getPhone());
                        staffModel.setFullName(orderDetail.getTblOrder().getStaff().getFullName());
                        staffModel.setAvatar(orderDetail.getTblOrder().getStaff().getAvatar());
                    }

                    //distance price
                    ShowDistancePriceModel distancePriceModel = new ShowDistancePriceModel();
                    distancePriceModel.setId(orderDetail.getTblOrder().getDistancePrice().getId());
                    distancePriceModel.setApplyDate(orderDetail.getTblOrder().getDistancePrice().getApplyDate());
                    distancePriceModel.setPricePerKm(orderDetail.getTblOrder().getDistancePrice().getPricePerKm());

                    //plant
                    com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
                    PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(orderDetail.getStorePlant().getPlant().getId(), Status.ACTIVE);
                    plantModel.setId(orderDetail.getStorePlant().getPlant().getId());
                    if(orderDetail.getStorePlant().getPlant().getPlantIMGList() != null && !orderDetail.getStorePlant().getPlant().getPlantIMGList().isEmpty()) {
                        plantModel.setImage(orderDetail.getStorePlant().getPlant().getPlantIMGList().get(0).getImgURL());
                    }
                    plantModel.setQuantity(orderDetail.getQuantity());
                    plantModel.setPlantName(orderDetail.getStorePlant().getPlant().getName());
                    plantModel.setPlantPriceID(newestPrice.getId());
                    plantModel.setPlantPrice(newestPrice.getPrice());
                    plantModel.setShipPrice(orderDetail.getStorePlant().getPlant().getPlantShipPrice().getPricePerPlant());

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
            model.setAvgRatingFeedback(Double.parseDouble(String.format("%.2f", avgRatingFeedback)));
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
        model.setAvgRatingFeedback(Double.parseDouble(String.format("%.2f", avgRatingFeedback)));

    }

    public List<ShowReportModel> reportPagingConverter(Page<Report> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowReportModel> modelResult = pagingResult.map(new Converter<Report, ShowReportModel>() {
                @Override
                protected ShowReportModel doForward(Report report) {
                    ShowReportModel model = new ShowReportModel();
                    model.setId(report.getId());
                    model.setCreatedDate(report.getCreatedDate());
                    model.setContractDetailID(report.getWorkingDate().getContractDetail().getId());
                    model.setDescription(report.getDescription());
                    model.setReason(report.getReason());
                    model.setStatus(report.getStatus());
                    model.setContractID(report.getWorkingDate().getContractDetail().getContract().getId());
                    model.setTimeWorking(report.getWorkingDate().getContractDetail().getTimeWorking());

                    WorkingDate workingDate = report.getWorkingDate();
                    com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel showWorkingDateModel = new com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel();
                    if(workingDate != null){
                        showWorkingDateModel.setId(workingDate.getId());
                        showWorkingDateModel.setWorkingDate(workingDate.getWorkingDate());
                        showWorkingDateModel.setIsReported(workingDate.getIsReported());
                        showWorkingDateModel.setStatus(workingDate.getStatus());
                    }

                    ServiceType serviceType = report.getWorkingDate().getContractDetail().getServiceType();
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel showServiceTypeModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel();
                    if(serviceType != null){
                        showServiceTypeModel.setId(serviceType.getId());
                        showServiceTypeModel.setTypeName(serviceType.getName());
                        showServiceTypeModel.setTypeSize(serviceType.getSize());
                        showServiceTypeModel.setTypeUnit(serviceType.getUnit());
                        showServiceTypeModel.setTypePercentage(serviceType.getPercentage());
                        showServiceTypeModel.setTypeApplyDate(serviceType.getApplyDate());
                    }

                    com.example.thanhhoa.entities.Service service = report.getWorkingDate().getContractDetail().getServiceType().getService();
                    com.example.thanhhoa.dtos.ContractModels.ShowServiceModel showServiceModel = new com.example.thanhhoa.dtos.ContractModels.ShowServiceModel();
                    if(service != null){
                        showServiceModel.setId(service.getId());
                        showServiceModel.setDescription(service.getDescription());
                        showServiceModel.setName(service.getName());
                        showServiceModel.setAtHome(service.getAtHome());
                    }

                    tblAccount customer = report.getCustomer();
                    ShowCustomerModel showCustomerModel = new ShowCustomerModel();
                    if(customer != null){
                        showCustomerModel.setId(customer.getId());
                        showCustomerModel.setAddress(customer.getAddress());
                        showCustomerModel.setEmail(customer.getEmail());
                        showCustomerModel.setPhone(customer.getPhone());
                        showCustomerModel.setFullName(customer.getFullName());
                        showCustomerModel.setAvatar(customer.getAvatar());
                    }

                    tblAccount staff = report.getWorkingDate().getStaff();
                    ShowStaffModel showStaffModel = new ShowStaffModel();
                    if(staff != null){
                        showStaffModel.setId(staff.getId());
                        showStaffModel.setAddress(staff.getAddress());
                        showStaffModel.setEmail(staff.getEmail());
                        showStaffModel.setPhone(staff.getPhone());
                        showStaffModel.setFullName(staff.getFullName());
                        showStaffModel.setAvatar(staff.getAvatar());
                    }

                    model.setShowServiceModel(showServiceModel);
                    model.setShowServiceTypeModel(showServiceTypeModel);
                    model.setShowWorkingDateModel(showWorkingDateModel);
                    model.setShowCustomerModel(showCustomerModel);
                    model.setShowStaffModel(showStaffModel);
                    return model;
                }

                @Override
                protected Report doBackward(ShowReportModel showReportModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

//    public List<ShowRequestModel> requestPagingConverter(Page<StorePlantRequest> pagingResult, Pageable paging) {
//        if(pagingResult.hasContent()) {
//            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
//            Page<ShowRequestModel> modelResult = pagingResult.map(new Converter<StorePlantRequest, ShowRequestModel>() {
//                @Override
//                protected ShowRequestModel doForward(StorePlantRequest request) {
//                    // from store
//                    ShowStoreModel fromStoreModel = new ShowStoreModel();
//                    fromStoreModel.setId(request.getFromStore().getId());
//                    fromStoreModel.setStoreName(request.getFromStore().getStoreName());
//                    fromStoreModel.setAddress(request.getFromStore().getAddress());
//                    fromStoreModel.setPhone(request.getFromStore().getPhone());
//
//                    // to store
//                    ShowStoreModel toStoreModel = new ShowStoreModel();
//                    toStoreModel.setId(request.getToStore().getId());
//                    toStoreModel.setStoreName(request.getToStore().getStoreName());
//                    toStoreModel.setAddress(request.getToStore().getAddress());
//                    toStoreModel.setPhone(request.getToStore().getPhone());
//
//                    // from manager
//                    ShowStaffModel fromManagerModel = new ShowStaffModel();
//                    fromManagerModel.setId(request.getFromManager().getAccount().getId());
//                    fromManagerModel.setFullName(request.getFromManager().getAccount().getFullName());
//                    fromManagerModel.setAddress(request.getFromManager().getAccount().getAddress());
//                    fromManagerModel.setAvatar(request.getFromManager().getAccount().getAvatar());
//                    fromManagerModel.setGender(request.getFromManager().getAccount().getGender());
//                    fromManagerModel.setPhone(request.getFromManager().getAccount().getPhone());
//                    fromManagerModel.setStatus(request.getFromManager().getStatus());
//                    fromManagerModel.setEmail(request.getFromManager().getAccount().getFullName());
//
//                    // to manager
//                    ShowStaffModel toManagerModel = new ShowStaffModel();
//                    toManagerModel.setId(request.getToManager().getAccount().getId());
//                    toManagerModel.setFullName(request.getToManager().getAccount().getFullName());
//                    toManagerModel.setAddress(request.getToManager().getAccount().getAddress());
//                    toManagerModel.setAvatar(request.getToManager().getAccount().getAvatar());
//                    toManagerModel.setGender(request.getToManager().getAccount().getGender());
//                    toManagerModel.setPhone(request.getToManager().getAccount().getPhone());
//                    toManagerModel.setStatus(request.getToManager().getStatus());
//                    toManagerModel.setEmail(request.getToManager().getAccount().getFullName());
//
//                    //plant
//                    com.example.thanhhoa.dtos.OrderModels.ShowPlantModel plantModel = new com.example.thanhhoa.dtos.OrderModels.ShowPlantModel();
//                    PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(request.getPlant().getId(), Status.ACTIVE);
//                    plantModel.setId(request.getPlant().getId());
//                    if(request.getPlant().getPlantIMGList() != null && !request.getPlant().getPlantIMGList().isEmpty()) {
//                        plantModel.setImage(request.getPlant().getPlantIMGList().get(0).getImgURL());
//                    }
//                    plantModel.setQuantity(request.getQuantity());
//                    plantModel.setPlantName(request.getPlant().getName());
//                    plantModel.setPlantPriceID(newestPrice.getId());
//                    plantModel.setPlantPrice(newestPrice.getPrice());
//                    plantModel.setShipPrice(request.getPlant().getPlantShipPrice().getPricePerPlant());
//
//                    ShowRequestModel model = new ShowRequestModel();
//                    model.setId(request.getId());
//                    model.setQuantity(request.getQuantity());
//                    model.setCreatedDate(request.getCreateDate());
//                    model.setUpdatedDate(request.getUpdateDate());
//                    model.setReason(request.getReason());
//                    model.setStatus(request.getStatus());
//                    model.setFromStoreModel(fromStoreModel);
//                    model.setToStoreModel(toStoreModel);
//                    model.setFromManagerModel(fromManagerModel);
//                    model.setToManagerModel(toManagerModel);
//                    model.setShowPlantModel(plantModel);
//                    model.setTotalPage(totalPage);
//                    return model;
//                }
//
//                @Override
//                protected StorePlantRequest doBackward(ShowRequestModel showRequestModel) {
//                    return null;
//                }
//            });
//            return modelResult.getContent();
//        } else {
//            return new ArrayList<>();
//        }
//    }

    public void createNotification(String entity, tblAccount account, String entityID, String action) throws FirebaseMessagingException {
        if(entity.equalsIgnoreCase("ORDER")) {
            if(account.getFcmToken() != null && !(account.getFcmToken().trim().isEmpty()) && account.getFcmToken().length() > 0) {
                CreateNotificationModel notificationModel = new CreateNotificationModel();
                notificationModel.setTitle("-- Thông báo từ ThanhHoa Gardens --");
                notificationModel.setUserID(account.getId());
                notificationModel.setContent("Đơn hàng có mã " + entityID + " của quý khách đã được " + action + ".");
                Map<String, String> map = new HashMap<String, String>();
                map.put("ORDER", entityID);
                notificationModel.setData(map);
                firebaseMessagingService.sendNotification(notificationModel);
            }
            Notification notification = new Notification();
            Notification lastNotification = notificationRepository.findFirstByOrderByIdDesc();
            if(lastNotification == null) {
                notification.setId(createNewID("N"));
            } else {
                notification.setId(createIDFromLastID("N", 1, lastNotification.getId()));
            }
            notification.setTblAccount(account);
            notification.setDescription("Đơn hàng có mã " + entityID + " của quý khách đã được " + action + ".");
            notification.setIsRead(false);
            notification.setLink("ORDER-" + entityID);
            notification.setDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            notification.setTitle("-- Thông báo từ ThanhHoa Gardens --");
            notificationRepository.saveAndFlush(notification);
        } else if(entity.equalsIgnoreCase("CONTRACT")) {
            if(account.getFcmToken() != null && !(account.getFcmToken().trim().isEmpty()) && account.getFcmToken().length() > 0) {
                CreateNotificationModel notificationModel = new CreateNotificationModel();
                notificationModel.setTitle("-- Thông báo từ ThanhHoa Gardens --");
                notificationModel.setUserID(account.getId());
                notificationModel.setContent("Hợp đồng có mã " + entityID + " của quý khách đã được " + action + ".");
                Map<String, String> map = new HashMap<String, String>();
                map.put("CONTRACT", entityID);
                notificationModel.setData(map);
                firebaseMessagingService.sendNotification(notificationModel);
            }
            Notification notification = new Notification();
            Notification lastNotification = notificationRepository.findFirstByOrderByIdDesc();
            if(lastNotification == null) {
                notification.setId(createNewID("N"));
            } else {
                notification.setId(createIDFromLastID("N", 1, lastNotification.getId()));
            }
            notification.setTblAccount(account);
            notification.setDescription("Hợp đồng có mã " + entityID + " của quý khách đã được " + action + ".");
            notification.setIsRead(false);
            notification.setLink("CONTRACT-" + entityID);
            notification.setDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            notification.setTitle("-- Thông báo từ ThanhHoa Gardens --");
            notificationRepository.saveAndFlush(notification);
        } else {
            if(account.getFcmToken() != null && !(account.getFcmToken().trim().isEmpty()) && account.getFcmToken().length() > 0) {
                CreateNotificationModel notificationModel = new CreateNotificationModel();
                notificationModel.setTitle("-- Thông báo từ ThanhHoa Gardens --");
                notificationModel.setUserID(account.getId());
                notificationModel.setContent("Báo cáo có mã " + entityID + " của quý khách đã được " + action + ".");
                Map<String, String> map = new HashMap<String, String>();
                map.put("REPORT", entityID);
                notificationModel.setData(map);
                firebaseMessagingService.sendNotification(notificationModel);
            }
            Notification notification = new Notification();
            Notification lastNotification = notificationRepository.findFirstByOrderByIdDesc();
            if(lastNotification == null) {
                notification.setId(createNewID("N"));
            } else {
                notification.setId(createIDFromLastID("N", 1, lastNotification.getId()));
            }
            notification.setTblAccount(account);
            notification.setDescription("Báo cáo có mã " + entityID + " của quý khách đã được " + action + ".");
            notification.setIsRead(false);
            notification.setLink("REPORT-" + entityID);
            notification.setDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            notification.setTitle("-- Thông báo từ ThanhHoa Gardens --");
            notificationRepository.saveAndFlush(notification);
        }

    }

    public List<ShowTransactionModel> transactionPagingConverter(Page<Transaction> pagingResult, Pageable paging) {
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowTransactionModel> modelResult = pagingResult.map(new Converter<Transaction, ShowTransactionModel>() {
                @Override
                protected ShowTransactionModel doForward(Transaction transaction) {
                    ShowTransactionModel model = new ShowTransactionModel();
                    model.setId(transaction.getId());
                    model.setAmount(transaction.getAmount());
                    model.setBankCode(transaction.getBankCode());
                    model.setBillNo(transaction.getBillNo());
                    model.setCardType(transaction.getCardType());
                    model.setCreatedDate(transaction.getCreateDate());
                    model.setCurrency(transaction.getCurrency());
                    model.setReason(transaction.getReason());
                    model.setTransNo(transaction.getTransNo());
                    model.setStatus(transaction.getStatus());
                    model.setBankAccount(transaction.getBankAccount());
                    model.setRefundBankCode(transaction.getRefundBankCode());
                    model.setBankAccountNo(transaction.getBankAccountNo());

                    // order
                    tblOrder order = transaction.getTblOrder();
                    ShowOrderModel orderModel = new ShowOrderModel();
                    if(order != null) {
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
                    }

                    //contract
                    Contract contract = transaction.getContract();
                    ShowContractModel contractModel = new ShowContractModel();
                    if(contract != null) {
                        contractModel.setId(contract.getId());
                        contractModel.setFullName(contract.getFullName());
                        contractModel.setAddress(contract.getAddress());
                        contractModel.setEmail(contract.getEmail());
                        contractModel.setPhone(contract.getPhone());
                        contractModel.setTitle(contract.getTitle());
                        contractModel.setPaymentMethod(contract.getPaymentMethod());
                        contractModel.setReason(contract.getReason());
                        contractModel.setCreatedDate(contract.getCreatedDate());
                        contractModel.setConfirmedDate(contract.getConfirmedDate());
                        contractModel.setSignedDate(contract.getSignedDate());
                        contractModel.setApprovedDate(contract.getApprovedDate());
                        contractModel.setRejectedDate(contract.getRejectedDate());
                        contractModel.setStartedDate(contract.getStartedDate());
                        contractModel.setEndedDate(contract.getEndedDate());
                        contractModel.setExpectedEndedDate(contract.getExpectedEndedDate());
                        contractModel.setTotal(contract.getTotal());
                        contractModel.setIsFeedback(contract.getIsFeedback());
                        contractModel.setIsSigned(contract.getIsSigned());
                        contractModel.setIsPaid(contract.getIsPaid());
                    }


                    // user
                    tblAccount user = transaction.getUser();
                    ShowStaffModel userModel = new ShowStaffModel();
                    userModel.setId(user.getId());
                    userModel.setAddress(user.getAddress());
                    userModel.setEmail(user.getEmail());
                    userModel.setPhone(user.getPhone());
                    userModel.setFullName(user.getFullName());
                    userModel.setAvatar(user.getAvatar());

                    model.setShowOrderModel(orderModel);
                    model.setShowContractModel(contractModel);
                    model.setShowStaffModel(userModel);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Transaction doBackward(ShowTransactionModel showTransactionModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Double getMonthsBetween(ServicePack pack) {
        switch(pack.getUnit().toLowerCase()) {
            case "tháng":
                return Double.parseDouble(pack.getRange());

            case "năm":
                return Double.parseDouble(pack.getRange()) * 12;
            default:
                return null;
        }
    }

//    class InvalidInput extends Exception {
//        public InvalidInput(String input) {
//            super(input);
//        }
//    }
}
