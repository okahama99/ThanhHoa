package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.CreateContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractIMGModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
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
import com.example.thanhhoa.repositories.pagings.ContractPagingRepository;
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
    private Util util;

    @Override
    public List<ShowContractModel> getAllContractByUserID(Long customerID) {
        List<Contract> contractList = contractRepository.findByCustomer_Id(customerID);
        if(contractList == null) {
            return null;
        }
        List<ShowContractModel> modelList = new ArrayList<>();
        for(Contract contract : contractList) {
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
            model.setCreatedDate(contract.getCreatedDate());
            model.setStartedDate(contract.getStartedDate());
            model.setApprovedDate(contract.getApprovedDate());
            model.setRejectedDate(contract.getRejectedDate());
            model.setEndedDate(contract.getEndedDate());
            model.setDeposit(contract.getDeposit());
            model.setTotal(contract.getTotal());
            model.setIsFeedback(contract.getIsFeedback());
            model.setIsSigned(contract.getIsSigned());
            model.setStoreID(contract.getStore().getId());
            model.setStoreName(contract.getStore().getStoreName());
            model.setStaffID(contract.getStaff().getId());
            model.setStaffName(contract.getStaff().getFullName());
            model.setCustomerID(contract.getCustomer().getId());
            model.setPaymentTypeID(contract.getPaymentType().getId());
            model.setStatus(contract.getStatus());
            model.setReason(contract.getReason());
            model.setImgList(imgModelList);
            modelList.add(model);
        }
        return modelList;

    }

    @Override
    public List<ShowContractDetailModel> getContractDetailByContractID(String contractID) {
        List<ContractDetail> detailList = contractDetailRepository.findByContract_Id(contractID);
        if(detailList == null) {
            return null;
        }
        List<ShowContractDetailModel> modelList = new ArrayList<>();
        for(ContractDetail detail : detailList) {
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
            model.setContractID(detail.getId());
            model.setServiceTypeID(detail.getServiceType().getId());
            model.setServicePackID(detail.getServicePack().getId());
            model.setWorkingDateList(dateModelList);
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String createContract(CreateContractModel createContractModel, Long userID) {
        Store store = storeRepository.getById(createContractModel.getStoreID());
        tblAccount customer = userRepository.getById(userID);
        if(createContractModel.getDetailModelList() == null) {
            return "Phải chọn ít nhất một dịch vụ để tạo hợp đồng.";
        }

        Contract contract = new Contract();
        Contract lastContract = contractRepository.findFirstByOrderByIdDesc();
        if(lastContract == null) {
            contract.setId(util.createNewID("CT"));
        } else {
            contract.setId(util.createIDFromLastID("CT", 2, lastContract.getId()));
        }
        contract.setAddress(createContractModel.getAddress());
        contract.setTitle(createContractModel.getTitle());
        contract.setFullName(createContractModel.getFullName());
        contract.setEmail(createContractModel.getEmail());
        contract.setPhone(createContractModel.getPhone());

        contract.setStore(store);
        contract.setCustomer(customer);
        contract.setStatus(Status.WAITING);
        contract.setCreatedDate(LocalDateTime.now());

        Double totalPrice = 0.0;
        for(CreateContractDetailModel model : createContractModel.getDetailModelList()) {
            ServicePack servicePack = new ServicePack();
            if(model.getServicePackID() != null) {
                servicePack = servicePackRepository.getById(model.getServicePackID());
            }
            ServiceType serviceType = serviceTypeRepository.getById(model.getServiceTypeID());

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

            detail.setNote(model.getNote());
            detail.setTimeWorking(model.getTimeWorking());
            detail.setTotalPrice(model.getTotalPrice());
            detail.setContract(contract);
            detail.setServicePack(servicePack);
            detail.setServiceType(serviceType);
            detail.setTotalPrice(total);
            contractDetailRepository.save(detail);
        }

        contract.setTotal(totalPrice);
        contractRepository.save(contract);
        return "Tạo thành công.";
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
    public String approveContract(ApproveContractModel approveContractModel) {
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
        contract.setRejectedDate(LocalDateTime.now());
        contract.setStatus(Status.SIGNED);
        contractRepository.save(contract);
        return "Duyệt thành công.";
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
    public String addWorkingDate(String contractDetailID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null) {
            return "Không tìm thấy Chi tiết hợp đồng với ID là " + contractDetailID + ".";
        }
        WorkingDate check = workingDateRepository.findFirstByOrderByWorkingDateDesc();
        if((LocalDateTime.now().getDayOfMonth() == check.getWorkingDate().getDayOfMonth()) &&
                (LocalDateTime.now().getMonth() == check.getWorkingDate().getMonth()) &&
                (LocalDateTime.now().getYear() == check.getWorkingDate().getYear())){
            return "Mỗi ngày chỉ được điểm danh 1 lần.";
        }
        WorkingDate workingDate = new WorkingDate();
        WorkingDate lastWorkingDate = workingDateRepository.findFirstByOrderByIdDesc();
        if(lastWorkingDate == null) {
            workingDate.setId(util.createNewID("WD"));
        } else {
            workingDate.setId(util.createIDFromLastID("WD", 2, lastWorkingDate.getId()));
        }
        workingDate.setWorkingDate(LocalDateTime.now());
        workingDate.setContractDetail(checkExisted.get());
        workingDateRepository.save(workingDate);
        return "Thêm thành công.";
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
}
