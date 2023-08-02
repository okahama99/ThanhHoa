package com.example.thanhhoa.services.workingDate;

import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.repositories.pagings.WorkingDatePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WorkingDateServiceImpl implements WorkingDateService {

    @Autowired
    private Util util;
    @Autowired
    private WorkingDateRepository workingDateRepository;
    @Autowired
    private WorkingDatePagingRepository workingDatePagingRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    @Override
    public String addWorkingDate(String contractDetailID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null) {
            return "Không tìm thấy Chi tiết hợp đồng với ID là " + contractDetailID + ".";
        }
        WorkingDate check = workingDateRepository.findFirstByContractDetail_IdOrderByWorkingDateDesc(contractDetailID);
        if(check != null){
            if((LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth() == check.getWorkingDate().getDayOfMonth()) &&
                    (LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).getMonth() == check.getWorkingDate().getMonth()) &&
                    (LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).getYear() == check.getWorkingDate().getYear())) {
                return "Mỗi ngày chỉ được điểm danh 1 lần.";
            }
        }

        WorkingDate workingDate = new WorkingDate();
        WorkingDate lastWorkingDate = workingDateRepository.findFirstByOrderByIdDesc();
        if(lastWorkingDate == null) {
            workingDate.setId(util.createNewID("WD"));
        } else {
            workingDate.setId(util.createIDFromLastID("WD", 2, lastWorkingDate.getId()));
        }
        workingDate.setWorkingDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        workingDate.setContractDetail(checkExisted.get());
        workingDateRepository.save(workingDate);
        return "Thêm thành công.";
    }

    @Override
    public List<ShowWorkingDateModel> getAllByContractDetailID(String contractDetailID, Pageable pageable) {
        Page<WorkingDate> pagingResult = workingDatePagingRepository.findByContractDetail_IdOrderByWorkingDateDesc(contractDetailID, pageable);
        return util.workingDatePagingConverter(pagingResult, pageable);
    }

    @Override
    public WorkingDate getByID(String workingDateID) {
        Optional<WorkingDate> workingDate = workingDateRepository.findById(workingDateID);
        if(workingDate == null) {
            return null;
        }
        return workingDate.get();
    }

    @Override
    public List<ShowWorkingDateModel> getWorkingDateByStaffID(Long staffID) {
        List<ContractDetail> contractDetailList = contractDetailRepository.findByContract_Staff_Id(staffID);
        if(contractDetailList == null) {
            return null;
        }
        List<ShowWorkingDateModel> modelList = new ArrayList<>();
        for(ContractDetail detail : contractDetailList) {
            List<WorkingDate> workingDateList = workingDateRepository.findByContractDetail_IdOrderByWorkingDateDesc(detail.getId());
            for(WorkingDate workingDate : workingDateList) {
                ShowWorkingDateModel model = new ShowWorkingDateModel();
                model.setId(workingDate.getId());
                model.setWorkingDate(workingDate.getWorkingDate());
                model.setContractDetailID(detail.getId());
                model.setNote(detail.getNote());
                model.setTimeWorking(detail.getTimeWorking());
                model.setEndDate(detail.getEndDate());
                model.setStartDate(detail.getStartDate());
                model.setTotalPrice(detail.getTotalPrice());
                model.setContractID(detail.getContract().getId());
                model.setAddress(detail.getContract().getAddress());
                model.setEmail(detail.getContract().getEmail());
                model.setPhone(detail.getContract().getPhone());
                model.setFullName(detail.getContract().getFullName());
                model.setServiceID(detail.getServiceType().getService().getId());
                model.setServiceName(detail.getServiceType().getService().getName());
                model.setServiceTypeID(detail.getServiceType().getId());
                model.setTypeName(detail.getServiceType().getName());
                model.setTypeSize(detail.getServiceType().getSize());
                model.setTypePercentage(detail.getServiceType().getPercentage());
                model.setTypeApplyDate(detail.getServiceType().getApplyDate());
                model.setServicePackID(detail.getServicePack().getId());
                model.setPackRange(detail.getServicePack().getRange());
                model.setPackPercentage(detail.getServicePack().getPercentage());
                model.setPackApplyDate(detail.getServicePack().getApplyDate());
                modelList.add(model);
                Collections.sort(modelList, Comparator.comparing(ShowWorkingDateModel ::getWorkingDate).reversed());
            }
        }
        return modelList;
    }

    @Override
    public ShowWorkingDateModel getByWorkingDate(String contractDetailID, LocalDateTime date) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null){
            return null;
        }
        ContractDetail detail = checkExisted.get();
        WorkingDate workingDate = workingDateRepository.findByContractDetail_IdAndWorkingDateBetween(contractDetailID, date, date.plusDays(1L));
        if(workingDate == null) {
            return null;
        }
        ShowWorkingDateModel model = new ShowWorkingDateModel();
        model.setId(workingDate.getId());
        model.setWorkingDate(workingDate.getWorkingDate());
        model.setContractDetailID(detail.getId());
        model.setNote(detail.getNote());
        model.setTimeWorking(detail.getTimeWorking());
        model.setEndDate(detail.getEndDate());
        model.setStartDate(detail.getStartDate());
        model.setTotalPrice(detail.getTotalPrice());
        model.setContractID(detail.getContract().getId());
        model.setAddress(detail.getContract().getAddress());
        model.setEmail(detail.getContract().getEmail());
        model.setPhone(detail.getContract().getPhone());
        model.setFullName(detail.getContract().getFullName());
        model.setServiceID(detail.getServiceType().getService().getId());
        model.setServiceName(detail.getServiceType().getService().getName());
        model.setServiceTypeID(detail.getServiceType().getId());
        model.setTypeName(detail.getServiceType().getName());
        model.setTypeSize(detail.getServiceType().getSize());
        model.setTypePercentage(detail.getServiceType().getPercentage());
        model.setTypeApplyDate(detail.getServiceType().getApplyDate());
        model.setServicePackID(detail.getServicePack().getId());
        model.setPackRange(detail.getServicePack().getRange());
        model.setPackPercentage(detail.getServicePack().getPercentage());
        model.setPackApplyDate(detail.getServicePack().getApplyDate());
        return model;
    }
}
