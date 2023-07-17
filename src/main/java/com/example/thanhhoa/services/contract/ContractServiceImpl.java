package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.CreateCustomerContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateManagerContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowPaymentTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServicePackModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.ContractIMG;
import com.example.thanhhoa.entities.PaymentType;
import com.example.thanhhoa.entities.ServicePack;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ContractIMGRepository;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.PaymentTypeRepository;
import com.example.thanhhoa.repositories.ServicePackRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.repositories.pagings.ContractDetailPagingRepository;
import com.example.thanhhoa.repositories.pagings.ContractPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private ServicePackRepository servicePackRepository;
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
    public List<ShowContractDetailModel> getAllContractDetailByUserID(Long userID) {
        List<Contract> contractList = contractRepository.findByStaff_Id(userID);
        if(contractList == null) {
            return null;
        }
        List<ShowContractDetailModel> modelList = new ArrayList<>();
        for(Contract contract : contractList) {
            for(ContractDetail detail : contract.getContractDetailList()) {
                List<WorkingDate> dateList = workingDateRepository.findByContractDetail_Id(detail.getId());
                List<ShowWorkingDateModel> dateModelList = new ArrayList<>();
                for(WorkingDate workingDate : dateList) {
                    ShowWorkingDateModel model = new ShowWorkingDateModel();
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
                ShowServiceTypeModel serviceTypeModel = new ShowServiceTypeModel();
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
                ShowServiceModel serviceModel = new ShowServiceModel();
                serviceModel.setId(detail.getServiceType().getService().getId());
                serviceModel.setDescription(detail.getServiceType().getService().getDescription());
                serviceModel.setPrice(detail.getServiceType().getService().getPrice());
                serviceModel.setName(detail.getServiceType().getService().getName());

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
    public List<ShowContractDetailModel> getContractDetailByContractID(String contractID, Pageable pageable) {
        Page<ContractDetail> pagingResult = contractDetailPagingRepository.findByContract_Id(contractID, pageable);
        return util.contractDetailPagingConverter(pagingResult, pageable);
    }

    @Override
    public String createContractCustomer(CreateCustomerContractModel createCustomerContractModel, Long userID) {
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
        contract.setCreatedDate(LocalDateTime.now());

        Double totalPrice = 0.0;
        List<LocalDateTime> startDateList = new ArrayList<>();
        List<LocalDateTime> endDateList = new ArrayList<>();
        for(CreateContractDetailModel model : createCustomerContractModel.getDetailModelList()) {
            ServicePack servicePack = new ServicePack();
            if(model.getServicePackID() != null) {
                servicePack = servicePackRepository.findByIdAndStatus(model.getServicePackID(), Status.ACTIVE);
                if(servicePack == null) {
                    return "Không tìm thấy ServicePack với ID là " + model.getServicePackID() + ".";
                }
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

            Double total = 0.0;
            ContractDetail detail = new ContractDetail();
            ContractDetail lastContractDetail = contractDetailRepository.findFirstByOrderByIdDesc();
            if(lastContractDetail == null) {
                detail.setId(util.createNewID("CTD"));
            } else {
                detail.setId(util.createIDFromLastID("CTD", 3, lastContractDetail.getId()));
            }

            total += servicePack.getPercentage() * (serviceType.getService().getPrice() + (serviceType.getPercentage() * serviceType.getService().getPrice()));
            totalPrice += total;

            detail.setStartDate(startDate);
            detail.setEndDate(endDate);
            detail.setNote(model.getNote());
            detail.setTimeWorking(model.getTimeWorking());
            detail.setTotalPrice(model.getTotalPrice());
            detail.setContract(contract);
            detail.setServicePack(servicePack);
            detail.setServiceType(serviceType);
            detail.setTotalPrice(total);
            contractDetailRepository.save(detail);
        }

        LocalDateTime startDate = Collections.min(startDateList);
        LocalDateTime endDate = Collections.max(endDateList);
        contract.setStartedDate(startDate);
        contract.setEndedDate(endDate);
        contract.setTotal(totalPrice);
        contractRepository.save(contract);
        return "Tạo thành công.";
    }

    @Override
    public String createContractManager(CreateManagerContractModel createManagerContractModel) throws IOException {
        Contract contract = new Contract();
        tblAccount customer;
        if(createManagerContractModel.getCustomerID() != null) {
            customer = userRepository.getById(createManagerContractModel.getCustomerID());
            contract.setCustomer(customer);

        }
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

        PaymentType paymentType = paymentTypeRepository.getById(createManagerContractModel.getPaymentTypeID());
        contract.setDeposit(createManagerContractModel.getDeposit());
        contract.setPaymentMethod(createManagerContractModel.getPaymentMethod());
        contract.setPaymentType(paymentType);
        contract.setStaff(staff);
        contract.setStartedDate(LocalDateTime.now());
        contract.setStatus(Status.SIGNED);

        contract.setStore(store);
        contract.setCreatedDate(LocalDateTime.now());

        Double totalPrice = 0.0;
        List<LocalDateTime> startDateList = new ArrayList<>();
        List<LocalDateTime> endDateList = new ArrayList<>();
        for(CreateContractDetailModel model : createManagerContractModel.getDetailModelList()) {
            ServicePack servicePack = new ServicePack();
            if(model.getServicePackID() != null) {
                servicePack = servicePackRepository.findByIdAndStatus(model.getServicePackID(), Status.ACTIVE);
                if(servicePack == null) {
                    return "Không tìm thấy ServicePack với ID là " + model.getServicePackID() + ".";
                }
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

            Double total = 0.0;
            ContractDetail detail = new ContractDetail();
            ContractDetail lastContractDetail = contractDetailRepository.findFirstByOrderByIdDesc();
            if(lastContractDetail == null) {
                detail.setId(util.createNewID("CTD"));
            } else {
                detail.setId(util.createIDFromLastID("CTD", 3, lastContractDetail.getId()));
            }

            total += servicePack.getPercentage() * (serviceType.getService().getPrice() + (serviceType.getPercentage() * serviceType.getService().getPrice()));
            totalPrice += total;

            detail.setStartDate(startDate);
            detail.setEndDate(endDate);
            detail.setNote(model.getNote());
            detail.setTimeWorking(model.getTimeWorking());
            detail.setTotalPrice(model.getTotalPrice());
            detail.setContract(contract);
            detail.setServicePack(servicePack);
            detail.setServiceType(serviceType);
            detail.setTotalPrice(total);
            contractDetailRepository.save(detail);
        }

        LocalDateTime startDate = Collections.min(startDateList);
        LocalDateTime endDate = Collections.max(endDateList);
        contract.setStartedDate(startDate);
        contract.setEndedDate(endDate);
        contract.setTotal(totalPrice);
        contractRepository.save(contract);
        return contract.getId();
    }

    @Override
    public String updateContract(UpdateContractModel updateContractModel, Long userID) {
        Contract contract = contractRepository.findByIdAndStatus(updateContractModel.getId(), Status.SIGNED);
        if(contract == null) {
            return "Không tìm thấy Hợp đồng với ID là " + updateContractModel.getId() + " có trạng thái là đã ký tên.";
        }
        Store store = storeRepository.getById(updateContractModel.getStoreID());
        tblAccount staff = userRepository.getById(updateContractModel.getStaffID());
        contract.setAddress(updateContractModel.getAddress());
        contract.setTitle(updateContractModel.getTitle());
        contract.setFullName(updateContractModel.getFullName());
        contract.setEmail(updateContractModel.getEmail());
        contract.setPhone(updateContractModel.getPhone());
        contract.setTotal(updateContractModel.getTotal());
        contract.setStore(store);
        contract.setStaff(staff);
        contractRepository.save(contract);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteContract(String contractID, String reason, Status status) {
        Contract contract = contractRepository.findByIdAndStatus(contractID, Status.WAITING);
        if(contract == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + contractID + ".";
        }
        contract.setReason(reason);
        contract.setStatus(status);
        contract.setRejectedDate(LocalDateTime.now());
        contractRepository.save(contract);
        return "Hủy thành công.";
    }

    @Override
    public String approveContract(ApproveContractModel approveContractModel) throws IOException {
        Contract contract = contractRepository.findByIdAndStatus(approveContractModel.getContractID(), Status.WAITING);
        if(contract == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + approveContractModel.getContractID() + ".";
        }
        tblAccount staff = userRepository.getById(approveContractModel.getStaffID());

        PaymentType paymentType = paymentTypeRepository.getById(approveContractModel.getPaymentTypeID());
        contract.setDeposit(approveContractModel.getDeposit());
        contract.setPaymentMethod(approveContractModel.getPaymentMethod());
        contract.setPaymentType(paymentType);
        contract.setStaff(staff);
        contract.setStartedDate(LocalDateTime.now());
        contract.setApprovedDate(LocalDateTime.now());
        contract.setStatus(Status.SIGNED);
        contractRepository.save(contract);
        return contract.getId();
    }

    @Override
    public String changeContractStatus(String contractID, Status status) {
        Optional<Contract> checkExisted = contractRepository.findById(contractID);
        if(checkExisted == null) {
            return "Không thể tìm thấy Hợp đồng có trạng thái WAITING với ID là " + contractID + ".";
        }
        Contract contract = checkExisted.get();
        contract.setStatus(status);
        contractRepository.save(contract);
        return "Chỉnh sửa thành công.";
    }


    @Override
    public List<ShowContractModel> getWaitingContract(Pageable pageable) {
        Page<Contract> pagingResult = contractPagingRepository.findByStatus(Status.WAITING, pageable);
        return util.contractPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<GetStaffModel> getStaffForContract() {
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
    public List<ShowPaymentTypeModel> getPaymentType() {
        List<PaymentType> paymentTypeList = paymentTypeRepository.findAll();
        if(paymentTypeList == null) {
            return null;
        }
        List<ShowPaymentTypeModel> listModel = new ArrayList<>();
        for(PaymentType paymentType : paymentTypeList) {
            ShowPaymentTypeModel model = new ShowPaymentTypeModel();
            model.setId(paymentType.getId());
            model.setName(paymentType.getName());
            model.setValue(paymentType.getValue());
            listModel.add(model);
        }
        return listModel;
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
