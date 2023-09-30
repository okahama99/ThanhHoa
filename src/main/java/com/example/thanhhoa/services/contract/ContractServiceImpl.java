package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.CreateCustomerContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateManagerContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowPlantStatusIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServicePackModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.ContractIMG;
import com.example.thanhhoa.entities.PlantStatusIMG;
import com.example.thanhhoa.entities.ServicePack;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ContractIMGRepository;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.PlantStatusIMGRepository;
import com.example.thanhhoa.repositories.ServicePackRepository;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.repositories.pagings.ContractDetailPagingRepository;
import com.example.thanhhoa.repositories.pagings.ContractPagingRepository;
import com.example.thanhhoa.services.otp.OtpService;
import com.example.thanhhoa.services.workingDate.WorkingDateService;
import com.example.thanhhoa.utils.Util;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractIMGRepository contractIMGRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ServicePackRepository servicePackRepository;
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private WorkingDateRepository workingDateRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ContractPagingRepository contractPagingRepository;
    @Autowired
    private ContractDetailPagingRepository contractDetailPagingRepository;
    @Autowired
    private Util util;
    @Autowired
    private OtpService otpService;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    private WorkingDateService workingDateService;
    @Autowired
    private PlantStatusIMGRepository plantStatusIMGRepository;

    @Override
    public List<ShowContractModel> getAllContractByUserID(Long userID, String role, Pageable pageable) {
        Page<Contract> pagingResult;
        if(role.equalsIgnoreCase("Staff")) {
            pagingResult = contractPagingRepository.findByStaff_Id(userID, pageable);
        } else {
            pagingResult = contractPagingRepository.findByCustomer_Id(userID, pageable);
        }
        return util.contractPagingConverter(pagingResult, pageable);

    }

    @Override
    public List<ShowContractModel> getAllContractByUserIDAndStatus(Long userID, String role, Status status, Pageable pageable) {
        Page<Contract> pagingResult;
        if(role.equalsIgnoreCase("Staff")) {
            pagingResult = contractPagingRepository.findByStaff_IdAndStatus(userID, status, pageable);
        } else {
            pagingResult = contractPagingRepository.findByCustomer_IdAndStatus(userID, status, pageable);
        }
        return util.contractPagingConverter(pagingResult, pageable);

    }

    @Override
    public List<ShowContractModel> getAllContractByStatus(String type, Pageable pageable) {
        Page<Contract> pagingResult;
        if(type.equalsIgnoreCase("REQUEST")) {
            pagingResult = contractPagingRepository.findByStatusOrStatusOrStatus(Status.WAITING, Status.CONFIRMING, Status.APPROVED, pageable);
        } else if(type.equalsIgnoreCase("CANCEL")) {
            pagingResult = contractPagingRepository.findByStatusOrStatusOrStatus(Status.DENIED, Status.CUSTOMERCANCELED, Status.STAFFCANCELED, pageable);
        } else {
            pagingResult = contractPagingRepository.findByStatusNotAndStatusNotAndStatusNotAndStatusNotAndStatusNotAndStatusNot(Status.WAITING, Status.CONFIRMING, Status.APPROVED, Status.DENIED, Status.CUSTOMERCANCELED, Status.STAFFCANCELED, pageable);
        }
        return util.contractPagingConverter(pagingResult, pageable);

    }

    @Override
    public List<ShowContractModel> getAllContract(Pageable pageable) {
        Page<Contract> pagingResult = contractPagingRepository.findAll(pageable);
        return util.contractPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowContractDetailModel> getAllContractDetailByStaffID(Long userID) {
        List<Contract> contractList = contractRepository.findByStaff_Id(userID);
        if(contractList == null) {
            return null;
        }

        List<ShowContractDetailModel> modelList = new ArrayList<>();
        for(Contract contract : contractList) {
            for(ContractDetail detail : contract.getContractDetailList()) {
                List<WorkingDate> dateList = workingDateRepository.findByContractDetail_IdOrderByWorkingDateDesc(detail.getId());
                List<ShowWorkingDateModel> dateModelList = new ArrayList<>();
                for(WorkingDate workingDate : dateList) {
                    ShowWorkingDateModel model = new ShowWorkingDateModel();
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
                model.setStartDate(detail.getStartDate());
                model.setExpectedEndDate(detail.getExpectedEndDate());
                model.setPlantStatus(detail.getPlantStatus());
                model.setPlantName(detail.getPlantName());
                model.setPrice(detail.getPrice());

                List<ShowPlantStatusIMGModel> plantStatusIMGModelList = new ArrayList<>();
                if(detail.getPlantStatusIMGList() != null && !detail.getPlantStatusIMGList().isEmpty()) {
                    for(PlantStatusIMG plantStatusIMG : detail.getPlantStatusIMGList()) {
                        ShowPlantStatusIMGModel plantStatusIMGModel = new ShowPlantStatusIMGModel();
                        plantStatusIMGModel.setId(plantStatusIMG.getId());
                        plantStatusIMGModel.setImgUrl(plantStatusIMG.getImgURL());
                        plantStatusIMGModelList.add(plantStatusIMGModel);
                    }
                }
                model.setPlantStatusIMGModelList(plantStatusIMGModelList);


                Double price = detail.getPrice();
                Double typePercentage = detail.getServiceType().getPercentage().doubleValue();
                Double packPercentage = detail.getServicePack().getPercentage().doubleValue();
                Double months = util.getMonthsBetween(detail.getServicePack());
                Double totalPrice = (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
                model.setTotalPrice(totalPrice);
                model.setPrice(model.getPrice());

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
                contract.setIsPaid(detail.getContract().getIsPaid());

                //service type
                ShowServiceTypeModel serviceTypeModel = new ShowServiceTypeModel();
                serviceTypeModel.setId(detail.getServiceType().getId());
                serviceTypeModel.setTypeName(detail.getServiceType().getName());
                serviceTypeModel.setTypeSize(detail.getServiceType().getSize());
                serviceTypeModel.setTypePercentage(detail.getServiceType().getPercentage());
                serviceTypeModel.setTypeApplyDate(detail.getServiceType().getApplyDate());
                serviceTypeModel.setTypeUnit(detail.getServiceType().getUnit());

                //service pack
                ShowServicePackModel servicePackModel = new ShowServicePackModel();
                servicePackModel.setId(detail.getServicePack().getId());
                servicePackModel.setPackPercentage(detail.getServicePack().getPercentage());
                servicePackModel.setPackRange(detail.getServicePack().getRange());
                servicePackModel.setPackApplyDate(detail.getServicePack().getApplyDate());
                servicePackModel.setPackUnit(detail.getServicePack().getUnit());

                //service
                ShowServiceModel serviceModel = new ShowServiceModel();
                serviceModel.setId(detail.getServiceType().getService().getId());
                serviceModel.setDescription(detail.getServiceType().getService().getDescription());
                serviceModel.setName(detail.getServiceType().getService().getName());
                serviceModel.setAtHome(detail.getServiceType().getService().getAtHome());

                model.setShowContractModel(contractModel);
                model.setShowServiceModel(serviceModel);
                model.setShowServicePackModel(servicePackModel);
                model.setShowServiceTypeModel(serviceTypeModel);
                model.setWorkingDateList(dateModelList);
                modelList.add(model);
            }
        }
        return modelList;

    }

    @Override
    public List<ShowContractDetailModel> getAllContractDetailByCustomerID(Long userID, Pageable pageable) {
        Page<ContractDetail> pagingResult = contractDetailPagingRepository.findByContract_Customer_Id(userID, pageable);
        return util.contractDetailPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowContractDetailModel> getAllContractDetailByCustomerIDAndTimeWorking(Long userID, String timeWorking, Pageable pageable) {
        Page<ContractDetail> pagingResult = contractDetailPagingRepository.findByContract_Customer_IdAndTimeWorkingContaining(userID, timeWorking, pageable);
        return util.contractDetailPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowContractDetailModel> getContractDetailByContractID(String contractID, Pageable pageable) {
        Page<ContractDetail> pagingResult = contractDetailPagingRepository.findByContract_Id(contractID, pageable);
        return util.contractDetailPagingConverter(pagingResult, pageable);
    }

    @Override
    public String createContractCustomer(CreateCustomerContractModel createCustomerContractModel, Long userID) throws FirebaseMessagingException, MessagingException {
        Store store = storeRepository.getById(createCustomerContractModel.getStoreID());
        tblAccount customer = userRepository.getById(userID);
        if(createCustomerContractModel.getDetailModelList() == null) {
            return "Phải chọn ít nhất một dịch vụ để tạo hợp đồng.";
        }

        Contract contract = new Contract();
        Contract lastContract = contractRepository.findFirstByOrderByIdDesc();
        if(lastContract == null) {
            contract.setId(util.createNewID("CT"));
        } else {
            contract.setId(util.createIDFromLastID("CT", 2, lastContract.getId()));
        }
        contract.setAddress(createCustomerContractModel.getAddress());
        contract.setTitle(createCustomerContractModel.getTitle());
        contract.setFullName(createCustomerContractModel.getFullName());
        contract.setEmail(createCustomerContractModel.getEmail());
        contract.setPhone(createCustomerContractModel.getPhone());

        contract.setStore(store);
        contract.setCustomer(customer);
        contract.setStatus(Status.WAITING);
        contract.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        Double totalPrice = 0.0;
        List<LocalDateTime> startDateList = new ArrayList<>();
        List<LocalDateTime> endDateList = new ArrayList<>();
        for(CreateContractDetailModel model : createCustomerContractModel.getDetailModelList()) {
            ServicePack servicePack = servicePackRepository.findByIdAndStatus(model.getServicePackID(), Status.ACTIVE);
            if(servicePack == null) {
                return "Không tìm thấy ServicePack với ID là " + model.getServicePackID() + ".";
            }
            ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(model.getServiceTypeID(), Status.ACTIVE);
            if(serviceType == null) {
                return "Không tìm thấy ServiceType với ID là " + model.getServiceTypeID() + ".";
            }

            LocalDateTime startDate = isDateValid(model.getStartDate());
            LocalDateTime endDate = isDateValid(model.getEndDate());
            if(startDate == null || endDate == null) {
                return "Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21";
            }
            startDateList.add(startDate);
            endDateList.add(endDate);
            ContractDetail detail = new ContractDetail();
            ContractDetail lastContractDetail = contractDetailRepository.findFirstByOrderByIdDesc();
            if(lastContractDetail == null) {
                detail.setId(util.createNewID("CTD"));
            } else {
                detail.setId(util.createIDFromLastID("CTD", 3, lastContractDetail.getId()));
            }

            ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(serviceType.getService().getId(), Status.ACTIVE);

            Double months = util.getMonthsBetween(servicePack);
            Double price = newestPrice.getPrice();
            Double typePercentage = serviceType.getPercentage().doubleValue();
            Double packPercentage = servicePack.getPercentage().doubleValue();

            totalPrice += (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
            detail.setPrice(newestPrice.getPrice());
            detail.setStartDate(startDate);
            detail.setExpectedEndDate(endDate);
            detail.setNote(model.getNote());
            detail.setTimeWorking(model.getTimeWorking());
            detail.setContract(contract);
            detail.setServicePack(servicePack);
            detail.setServiceType(serviceType);
            detail.setPlantName(model.getPlantName());
            contractDetailRepository.save(detail);
        }

        LocalDateTime startDate = Collections.min(startDateList);
        LocalDateTime endDate = Collections.max(endDateList);
        contract.setStartedDate(startDate);
        contract.setExpectedEndedDate(endDate);
        contract.setTotal(totalPrice);
        contractRepository.save(contract);

        util.createNotification("CONTRACT", customer, contract.getId(), "tạo");

        StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
        if(manager != null) {
            otpService.generateNotificationEmailForManager(manager.getAccount().getEmail(), "Hợp đồng");
        }

        return "Tạo thành công.";
    }

    @Override
    public String createContractManager(CreateManagerContractModel createManagerContractModel) throws IOException, FirebaseMessagingException {
        Contract contract = new Contract();
        Store store = storeRepository.getById(createManagerContractModel.getStoreID());
        tblAccount staff = userRepository.getById(createManagerContractModel.getStaffID());
        if(createManagerContractModel.getDetailModelList() == null) {
            return "Phải chọn ít nhất một dịch vụ để tạo hợp đồng.";
        }

        Contract lastContract = contractRepository.findFirstByOrderByIdDesc();
        if(lastContract == null) {
            contract.setId(util.createNewID("CT"));
        } else {
            contract.setId(util.createIDFromLastID("CT", 2, lastContract.getId()));
        }
        contract.setAddress(createManagerContractModel.getAddress());
        contract.setTitle(createManagerContractModel.getTitle());
        contract.setFullName(createManagerContractModel.getFullName());
        contract.setEmail(createManagerContractModel.getEmail());
        contract.setPhone(createManagerContractModel.getPhone());
        contract.setPaymentMethod(createManagerContractModel.getPaymentMethod());

        contract.setStore(store);
        contract.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contract.setApprovedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contract.setStaff(staff);

        tblAccount customer;
        if(createManagerContractModel.getCustomerID() != null) {
            customer = userRepository.getById(createManagerContractModel.getCustomerID());
            contract.setCustomer(customer);

            util.createNotification("CONTRACT", customer, contract.getId(), "tạo");
        }

        Double totalPrice = 0.0;
        List<LocalDateTime> startDateList = new ArrayList<>();
        List<LocalDateTime> endDateList = new ArrayList<>();
        for(CreateContractDetailModel model : createManagerContractModel.getDetailModelList()) {
            ServicePack servicePack = servicePackRepository.findByIdAndStatus(model.getServicePackID(), Status.ACTIVE);
            if(servicePack == null) {
                return "Không tìm thấy ServicePack với ID là " + model.getServicePackID() + ".";
            }

            ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(model.getServiceTypeID(), Status.ACTIVE);
            if(serviceType == null) {
                return "Không tìm thấy ServiceType với ID là " + model.getServiceTypeID() + ".";
            }

            LocalDateTime startDate = isDateValid(model.getStartDate());
            LocalDateTime endDate = isDateValid(model.getEndDate());
            if(startDate == null || endDate == null) {
                return "Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21";
            }
            startDateList.add(startDate);
            endDateList.add(endDate);

            ContractDetail detail = new ContractDetail();
            ContractDetail lastContractDetail = contractDetailRepository.findFirstByOrderByIdDesc();
            if(lastContractDetail == null) {
                detail.setId(util.createNewID("CTD"));
            } else {
                detail.setId(util.createIDFromLastID("CTD", 3, lastContractDetail.getId()));
            }

            ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(serviceType.getService().getId(), Status.ACTIVE);

            Double months = util.getMonthsBetween(servicePack);
            Double price = newestPrice.getPrice();
            Double typePercentage = serviceType.getPercentage().doubleValue();
            Double packPercentage = servicePack.getPercentage().doubleValue();

            totalPrice += (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
            detail.setPrice(newestPrice.getPrice());
            detail.setStartDate(startDate);
            detail.setExpectedEndDate(endDate);
            detail.setNote(model.getNote());
            detail.setTimeWorking(model.getTimeWorking());
            detail.setContract(contract);
            detail.setServicePack(servicePack);
            detail.setServiceType(serviceType);
            detail.setPlantName(model.getPlantName());
            contractDetailRepository.save(detail);
        }

//        for(String imageURL : createManagerContractModel.getListURL()) {
//            ContractIMG contractIMG = new ContractIMG();
//            ContractIMG lastContractIMG = contractIMGRepository.findFirstByOrderByIdDesc();
//            if(lastContractIMG == null) {
//                contractIMG.setId(util.createNewID("CIMG"));
//            } else {
//                contractIMG.setId(util.createIDFromLastID("CIMG", 4, lastContractIMG.getId()));
//            }
//            contractIMG.setContract(contract);
//            contractIMG.setImgURL(imageURL);
//            contractIMGRepository.save(contractIMG);
//        }

        LocalDateTime startDate = Collections.min(startDateList);
        LocalDateTime endDate = Collections.max(endDateList);
        contract.setStartedDate(startDate);
        contract.setExpectedEndedDate(endDate);
        contract.setTotal(totalPrice);
        contract.setStatus(Status.CONFIRMING);
        contract.setIsSigned(false);
        userRepository.save(staff);
        contractRepository.save(contract);

        util.createNotification("CONTRACT", staff, contract.getId(), "giao cho bạn");
        return contract.getId();
    }

    @Override
    public String updateContractDetail(UpdateContractDetailModel updateContractDetailModel, Long userID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(updateContractDetailModel.getId());
        if(checkExisted == null || checkExisted.isEmpty()) {
            return "Không tìm thấy ContractDetail với ID là " + updateContractDetailModel.getId() + ".";
        }

        LocalDateTime startDate = util.isLocalDateTimeValid(updateContractDetailModel.getStartDate());
        if(startDate == null) {
            return "Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21";
        }

        LocalDateTime endDate = util.isLocalDateTimeValid(updateContractDetailModel.getEndDate());
        if(endDate == null) {
            return "Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21";
        }

        ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(updateContractDetailModel.getServiceTypeID(), Status.ACTIVE);
        if(serviceType == null) {
            return "Không tìm thấy ServiceType với ID là " + updateContractDetailModel.getServiceTypeID() + ".";
        }

        ServicePack servicePack = servicePackRepository.findByIdAndStatus(updateContractDetailModel.getServicePackID(), Status.ACTIVE);
        if(servicePack == null) {
            return "Không tìm thấy ServicePack với ID là " + updateContractDetailModel.getServicePackID() + ".";
        }

        if(updateContractDetailModel.getPlantIMG() == null || updateContractDetailModel.getPlantIMG().isEmpty()) {
            return "Phải có hình ảnh cây.";
        }


        ContractDetail detail = checkExisted.get();
        if(detail.getPlantStatusIMGList() != null) {
            for(PlantStatusIMG image : detail.getPlantStatusIMGList()) {
                image.setContractDetail(null);
                plantStatusIMGRepository.save(image);
            }
        }

        for(String imageURL : updateContractDetailModel.getPlantIMG()) {
            PlantStatusIMG plantStatusIMGIMG = new PlantStatusIMG();
            PlantStatusIMG lastPlantStatusIMGIMG = plantStatusIMGRepository.findFirstByOrderByIdDesc();
            if(lastPlantStatusIMGIMG == null) {
                plantStatusIMGIMG.setId(util.createNewID("PSIMG"));
            } else {
                plantStatusIMGIMG.setId(util.createIDFromLastID("PSIMG", 5, lastPlantStatusIMGIMG.getId()));
            }
            plantStatusIMGIMG.setContractDetail(detail);
            plantStatusIMGIMG.setImgURL(imageURL);
            plantStatusIMGRepository.save(plantStatusIMGIMG);
        }

        detail.setNote(updateContractDetailModel.getNote());
        detail.setTimeWorking(updateContractDetailModel.getTimeWorking());
        detail.setStartDate(startDate);
        detail.setExpectedEndDate(endDate);
        detail.setServiceType(serviceType);
        detail.setServicePack(servicePack);
        detail.setPlantStatus(updateContractDetailModel.getPlantStatus());
        detail.setPlantName(updateContractDetailModel.getPlantName());
        contractDetailRepository.save(detail);

        Contract contract = detail.getContract();
        List<LocalDateTime> startDateList = new ArrayList<>();
        List<LocalDateTime> endDateList = new ArrayList<>();
        Double totalPrice = 0.0;
        for(ContractDetail contractDetail : contract.getContractDetailList()) {
            startDateList.add(contractDetail.getStartDate());
            endDateList.add(contractDetail.getExpectedEndDate());

            Optional<ServiceType> type = serviceTypeRepository.findById(contractDetail.getServiceType().getId());
            if(type == null) {
                return "Không tìm thấy ServiceType của Service có ID là " + contractDetail.getServiceType().getId() + ".";
            }

            Optional<ServicePack> pack = servicePackRepository.findById(contractDetail.getServicePack().getId());
            if(pack == null) {
                return "Không tìm thấy ServicePack của Service có ID là " + contractDetail.getServicePack().getId() + ".";
            }

            Double months = util.getMonthsBetween(pack.get());
            Double price = contractDetail.getPrice();
            Double typePercentage = type.get().getPercentage().doubleValue();
            Double packPercentage = pack.get().getPercentage().doubleValue();

            totalPrice += (price * months) + ((price * typePercentage / 100) * months) - ((price * packPercentage / 100) * months);
        }

        LocalDateTime minStartDate = Collections.min(startDateList);
        LocalDateTime maxEndDate = Collections.max(endDateList);
        contract.setTotal(totalPrice);
        contract.setStartedDate(minStartDate);
        contract.setExpectedEndedDate(maxEndDate);
        contractRepository.save(contract);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String updateContractStaff(String contractID, Long staffID) throws FirebaseMessagingException {
        Optional<Contract> checkExisted = contractRepository.findById(contractID);
        if(checkExisted == null) {
            return "Không thể tìm thấy Contract với ID là " + contractID + ".";
        }
        Contract contract = checkExisted.get();
        tblAccount oldStaff = contract.getStaff();

        tblAccount newStaff = userRepository.findByIdAndStatus(staffID, Status.ACTIVE);
        if(newStaff == null) {
            return "Không thể tìm thấy Staff status ACTIVE với ID là " + staffID + ".";
        }

        contract.setStaff(newStaff);

        List<WorkingDate> workingDateList = new ArrayList<>();
        for(ContractDetail detail : contract.getContractDetailList()) {
            for(WorkingDate workingDate : detail.getWorkingDateList()) {
                workingDateList.add(workingDate);
            }
        }

        for(WorkingDate workingDate : workingDateList) {
            if(workingDate.getStatus().toString().equalsIgnoreCase("WAITING")) {
                workingDate.setStaff(newStaff);
                workingDateRepository.save(workingDate);
            }
        }


        contractRepository.save(contract);

        try {
            util.createNotification("CONTRACT", oldStaff, contract.getId(), "giao cho nhân viên khác");
            util.createNotification("CONTRACT", newStaff, contract.getId(), "giao cho bạn");
            if(contract.getCustomer() != null) {
                util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "giao cho nhân viên khác");
            }
        } catch(Exception e) {
            e.toString();
        }

        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteContract(String contractID, String reason, Status status) throws FirebaseMessagingException {
        Contract contract = contractRepository.findByIdAndStatus(contractID, Status.WAITING);
        if(contract == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + contractID + ".";
        }
        contract.setReason(reason);
        contract.setStatus(status);
        contract.setRejectedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contractRepository.save(contract);

        util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "xóa");

        return "Hủy thành công.";
    }

    @Override
    public String approveContract(ApproveContractModel approveContractModel) throws IOException, FirebaseMessagingException {
        Contract contract = contractRepository.findByIdAndStatus(approveContractModel.getContractID(), Status.WAITING);
        if(contract == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + approveContractModel.getContractID() + ".";
        }

        tblAccount staff = userRepository.getById(approveContractModel.getStaffID());

        contract.setPaymentMethod(approveContractModel.getPaymentMethod());
        contract.setStaff(staff);
        contract.setApprovedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contract.setStatus(Status.APPROVED);
        contractRepository.save(contract);

        if(contract.getCustomer() != null) {
            util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "quản lý duyệt");
        }
        util.createNotification("CONTRACT", contract.getStaff(), contract.getId(), "giao cho bạn");

        for(ContractDetail detail : contract.getContractDetailList()) {
            workingDateService.generateWorkingSchedule(detail.getId());
        }
        return contract.getId();
    }

    @Override
    public String addContractIMG(String contractID, List<String> listURL) throws FirebaseMessagingException {
        Contract contract = contractRepository.findByIdAndStatus(contractID, Status.CONFIRMING);
        if(contract == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái APPROVED với ID là " + contractID + ".";
        }
        if(listURL.isEmpty() || listURL == null) {
            return "Không tìm thấy URL trong List";
        }

        if(contract.getContractIMGList() != null) {
            for(ContractIMG image : contract.getContractIMGList()) {
                image.setContract(null);
                contractIMGRepository.save(image);
            }
        }


        for(String imageURL : listURL) {
            ContractIMG contractIMG = new ContractIMG();
            ContractIMG lastContractIMG = contractIMGRepository.findFirstByOrderByIdDesc();
            if(lastContractIMG == null) {
                contractIMG.setId(util.createNewID("CIMG"));
            } else {
                contractIMG.setId(util.createIDFromLastID("CIMG", 4, lastContractIMG.getId()));
            }

            contractIMG.setContract(contract);
            contractIMG.setImgURL(imageURL);
            contractIMGRepository.save(contractIMG);
        }

        contract.setStatus(Status.SIGNED);
        contract.setIsSigned(true);
//        contract.setStartedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        contract.setSignedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        contractRepository.save(contract);

        List<String> listID = new ArrayList<>();
        for(ContractDetail detail : contract.getContractDetailList()) {
            listID.add(detail.getId());
        }

        if(listID != null) {
            for(String id : listID) {
                workingDateService.generateWorkingSchedule(id);
            }
        }

        if(contract.getCustomer() != null) {
            util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "ký tên");
        }
        return "Thêm thành công.";
    }

    @Override
    public String changeContractStatus(String contractID, Long staffID, String reason, Status status) throws FirebaseMessagingException {
        Optional<Contract> checkExisted = contractRepository.findById(contractID);
        if(checkExisted == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + contractID + ".";
        }
        Contract contract = checkExisted.get();
        if(status.toString().equalsIgnoreCase("DENIED")) {
            contract.setStatus(status);
            contract.setRejectedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

            if(contract.getCustomer() != null) {
                util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "từ chối");
            }
        } else if(status.toString().equalsIgnoreCase("CUSTOMERCANCELED")) {
            contract.setStatus(status);
            contract.setRejectedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

            if(contract.getCustomer() != null) {
                util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "hủy");
            }
        } else if(status.toString().equalsIgnoreCase("STAFFCANCELED")) {
            contract.setStatus(status);
            contract.setRejectedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

            if(contract.getCustomer() != null) {
                util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "hủy");
            }
        } else if(status.toString().equalsIgnoreCase("CONFIRMING")) {
            contract.setApprovedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            contract.setConfirmedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            contract.setStatus(status);

            if(staffID == null) {
                return "StaffID đang bị trống";
            }
            StoreEmployee staff = storeEmployeeRepository.findByAccount_IdAndStatus(staffID, Status.ACTIVE);
            if(staff == null) {
                return "Không tìm thấy StoreEmployee với StaffID là " + staffID + ".";
            }
            contract.setStaff(staff.getAccount());

            util.createNotification("CONTRACT", staff.getAccount(), contract.getId(), "giao cho bạn");

            if(contract.getCustomer() != null) {
                util.createNotification("CONTRACT", contract.getCustomer(), contract.getId(), "hủy");
            }
        } else {
            contract.setStatus(status);
        }

        if(reason != null) {
            contract.setReason(reason);
        }

        contractRepository.save(contract);
        return "Chỉnh sửa thành công.";
    }


    @Override
    public List<ShowContractModel> getWaitingContract(Pageable pageable) {
        Page<Contract> pagingResult = contractPagingRepository.findByStatus(Status.WAITING, pageable);
        return util.contractPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowContractModel getByID(String contractID) {
        Optional<Contract> checkExisted = contractRepository.findById(contractID);
        if(checkExisted == null) {
            return null;
        }
        Contract contract = checkExisted.get();
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
        return model;
    }

    @Override
    public ShowContractModel getByContractDetailID(String contractDetailID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null) {
            return null;
        }
        Contract contract = checkExisted.get().getContract();
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
        return model;
    }

    @Override
    public List<GetStaffModel> getStaffForContract() {
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
    public List<ShowContractModel> getContractByStoreID(String storeID, Pageable pageable) {
        Page<Contract> pagingResult = contractPagingRepository.findByStore_Id(storeID, pageable);
        return util.contractPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowContractModel> getContractByStoreIDAndStatus(String storeID, Status status, Pageable pageable) {
        Page<Contract> pagingResult = contractPagingRepository.findByStore_IdAndStatus(storeID, status, pageable);
        return util.contractPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowContractDetailModel> getContractDetailByDateBetween(LocalDateTime from, LocalDateTime to, Long staffID, String role) {
        List<ContractDetail> contractDetailList = null;
        if(role.equalsIgnoreCase("Staff")) {
            contractDetailList =
                    contractDetailRepository.findByContract_Staff_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(staffID, from, to);
        }
        if(role.equalsIgnoreCase("Customer")) {
            contractDetailList =
                    contractDetailRepository.findByContract_Customer_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(staffID, from, to);
        }
        if(contractDetailList == null) {
            return null;
        }
        List<ShowContractDetailModel> modelList = new ArrayList<>();
        for(ContractDetail detail : contractDetailList) {
            List<WorkingDate> dateList = workingDateRepository.findByContractDetail_IdOrderByWorkingDateDesc(detail.getId());
            List<ShowWorkingDateModel> dateModelList = new ArrayList<>();
            for(WorkingDate workingDate : dateList) {
                ShowWorkingDateModel model = new ShowWorkingDateModel();
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
            model.setStartDate(detail.getStartDate());
            model.setExpectedEndDate(detail.getExpectedEndDate());
            model.setPlantName(detail.getPlantName());
            model.setPlantStatus(detail.getPlantStatus());
            model.setPrice(detail.getPrice());

            List<ShowPlantStatusIMGModel> plantStatusIMGModelList = new ArrayList<>();
            if(detail.getPlantStatusIMGList() != null && !detail.getPlantStatusIMGList().isEmpty()) {
                for(PlantStatusIMG plantStatusIMG : detail.getPlantStatusIMGList()) {
                    ShowPlantStatusIMGModel plantStatusIMGModel = new ShowPlantStatusIMGModel();
                    plantStatusIMGModel.setId(plantStatusIMG.getId());
                    plantStatusIMGModel.setImgUrl(plantStatusIMG.getImgURL());
                    plantStatusIMGModelList.add(plantStatusIMGModel);
                }
            }
            model.setPlantStatusIMGModelList(plantStatusIMGModelList);

            ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(detail.getServiceType().getService().getId(), Status.ACTIVE);

            Double months = util.getMonthsBetween(detail.getServicePack());
            Double price = newestPrice.getPrice();
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
            ShowServiceTypeModel serviceTypeModel = new ShowServiceTypeModel();
            serviceTypeModel.setId(detail.getServiceType().getId());
            serviceTypeModel.setTypeName(detail.getServiceType().getName());
            serviceTypeModel.setTypeSize(detail.getServiceType().getSize());
            serviceTypeModel.setTypePercentage(detail.getServiceType().getPercentage());
            serviceTypeModel.setTypeApplyDate(detail.getServiceType().getApplyDate());
            serviceTypeModel.setTypeUnit(detail.getServiceType().getUnit());

            //service pack
            ShowServicePackModel servicePackModel = new ShowServicePackModel();
            servicePackModel.setId(detail.getServicePack().getId());
            servicePackModel.setPackPercentage(detail.getServicePack().getPercentage());
            servicePackModel.setPackRange(detail.getServicePack().getRange());
            servicePackModel.setPackApplyDate(detail.getServicePack().getApplyDate());
            servicePackModel.setPackUnit(detail.getServicePack().getUnit());

            //service
            ShowServiceModel serviceModel = new ShowServiceModel();
            serviceModel.setId(detail.getServiceType().getService().getId());
            serviceModel.setDescription(detail.getServiceType().getService().getDescription());
            serviceModel.setName(detail.getServiceType().getService().getName());
            serviceModel.setAtHome(detail.getServiceType().getService().getAtHome());

            model.setShowContractModel(contractModel);
            model.setShowServiceModel(serviceModel);
            model.setShowServicePackModel(servicePackModel);
            model.setShowServiceTypeModel(serviceTypeModel);
            model.setWorkingDateList(dateModelList);
            modelList.add(model);
        }

        return modelList;
    }

    @Override
    public List<ShowContractDetailModel> getContractDetailByExactDate(LocalDateTime from, LocalDateTime to, Long staffID) {
        List<ContractDetail> contractDetailList =
                contractDetailRepository.findByContract_Staff_IdAndStartDateBetweenAndEndDateBetween(staffID, from, from.plusDays(1L), to, to.plusDays(1L));
        if(contractDetailList == null) {
            return null;
        }
        List<ShowContractDetailModel> modelList = new ArrayList<>();
        for(ContractDetail detail : contractDetailList) {
            List<WorkingDate> dateList = workingDateRepository.findByContractDetail_IdOrderByWorkingDateDesc(detail.getId());
            List<ShowWorkingDateModel> dateModelList = new ArrayList<>();
            for(WorkingDate workingDate : dateList) {
                ShowWorkingDateModel model = new ShowWorkingDateModel();
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
            model.setStartDate(detail.getStartDate());
            model.setExpectedEndDate(detail.getExpectedEndDate());
            model.setPlantName(detail.getPlantName());
            model.setPlantStatus(detail.getPlantStatus());
            model.setPrice(detail.getPrice());

            List<ShowPlantStatusIMGModel> plantStatusIMGModelList = new ArrayList<>();
            if(detail.getPlantStatusIMGList() != null && !detail.getPlantStatusIMGList().isEmpty()) {
                for(PlantStatusIMG plantStatusIMG : detail.getPlantStatusIMGList()) {
                    ShowPlantStatusIMGModel plantStatusIMGModel = new ShowPlantStatusIMGModel();
                    plantStatusIMGModel.setId(plantStatusIMG.getId());
                    plantStatusIMGModel.setImgUrl(plantStatusIMG.getImgURL());
                    plantStatusIMGModelList.add(plantStatusIMGModel);
                }
            }
            model.setPlantStatusIMGModelList(plantStatusIMGModelList);

            ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(detail.getServiceType().getService().getId(), Status.ACTIVE);

            Double months = util.getMonthsBetween(detail.getServicePack());
            Double price = newestPrice.getPrice();
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
            ShowServiceTypeModel serviceTypeModel = new ShowServiceTypeModel();
            serviceTypeModel.setId(detail.getServiceType().getId());
            serviceTypeModel.setTypeName(detail.getServiceType().getName());
            serviceTypeModel.setTypeSize(detail.getServiceType().getSize());
            serviceTypeModel.setTypePercentage(detail.getServiceType().getPercentage());
            serviceTypeModel.setTypeApplyDate(detail.getServiceType().getApplyDate());
            serviceTypeModel.setTypeUnit(detail.getServiceType().getUnit());

            //service pack
            ShowServicePackModel servicePackModel = new ShowServicePackModel();
            servicePackModel.setId(detail.getServicePack().getId());
            servicePackModel.setPackPercentage(detail.getServicePack().getPercentage());
            servicePackModel.setPackRange(detail.getServicePack().getRange());
            servicePackModel.setPackApplyDate(detail.getServicePack().getApplyDate());
            servicePackModel.setPackUnit(detail.getServicePack().getUnit());

            //service
            ShowServiceModel serviceModel = new ShowServiceModel();
            serviceModel.setId(detail.getServiceType().getService().getId());
            serviceModel.setDescription(detail.getServiceType().getService().getDescription());
            serviceModel.setName(detail.getServiceType().getService().getName());
            serviceModel.setAtHome(detail.getServiceType().getService().getAtHome());

            model.setShowContractModel(contractModel);
            model.setShowServiceModel(serviceModel);
            model.setShowServicePackModel(servicePackModel);
            model.setShowServiceTypeModel(serviceTypeModel);
            model.setWorkingDateList(dateModelList);
            modelList.add(model);
        }

        return modelList;
    }

    @Override
    public void checkStartDateEndDate() throws FirebaseMessagingException {
        List<Contract> workingList = contractRepository.findAllByStartedDateLessThanEqualAndStatus(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")), Status.SIGNED);
        if(workingList != null && !workingList.isEmpty()) {
            for(Contract contract : workingList) {
                contract.setStatus(Status.WORKING);
                contractRepository.save(contract);
                util.createNotification("CONTRACT", contract.getStaff(), contract.getId(), "bắt đầu");
            }
        }

        List<Contract> doneList = contractRepository.findAllByEndedDateLessThanEqualAndStatus(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")), Status.WORKING);
        if(doneList != null && !doneList.isEmpty()) {
            for(Contract contract : doneList) {
                contract.setStatus(Status.DONE);
                contract.setEndedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                contractRepository.save(contract);
                util.createNotification("CONTRACT", contract.getStaff(), contract.getId(), "kết thúc");
            }
        }
    }

    private LocalDateTime isDateValid(String date) {
        date += " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date, formatter);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
