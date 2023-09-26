package com.example.thanhhoa.services.workingDate;

import com.example.thanhhoa.dtos.ContractModels.ShowPlantStatusIMGModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.PlantStatusIMG;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.repositories.pagings.WorkingDatePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServicePriceRepository servicePriceRepository;

    @Override
    public String addStartWorkingDate(String workingDateID, String startWorkingIMG, Long staffID) {
        Optional<WorkingDate> checkExisted = workingDateRepository.findById(workingDateID);
        if(checkExisted == null) {
            return "Không tìm thấy WorkingDate với ID là " + workingDateID + ".";
        }
        WorkingDate workingDate = checkExisted.get();
        if(workingDate.getStartWorking() != null) {
            return "Ngày " + workingDate.getWorkingDate() + " đã có StartWorking Date.";
        }

        workingDate.setStaff(userRepository.getById(staffID));
        workingDate.setStartWorkingIMG(startWorkingIMG);
        workingDate.setStartWorking(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        workingDate.setStatus(Status.WORKING);
        workingDateRepository.save(workingDate);
        return "Thêm thành công.";
    }

    @Override
    public String addEndWorkingDate(String workingDateID, String endWorkingIMG, Long staffID) {
        Optional<WorkingDate> checkExisted = workingDateRepository.findById(workingDateID);
        if(checkExisted == null) {
            return "Không tìm thấy WorkingDate với ID là " + workingDateID + ".";
        }
        WorkingDate workingDate = checkExisted.get();
        if(workingDate.getEndWorking() != null) {
            return "Ngày " + workingDate.getWorkingDate() + " đã có EndWorking Date.";
        }

        workingDate.setStaff(userRepository.getById(staffID));
        workingDate.setEndWorkingIMG(endWorkingIMG);
        workingDate.setEndWorking(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        workingDate.setStatus(Status.DONE);
        workingDateRepository.save(workingDate);
        return "Thêm thành công.";
    }

    @Override
    public String updateWorkingDateStaffID(String workingDateID, String note, Long staffID) {
        Optional<WorkingDate> checkExisted = workingDateRepository.findById(workingDateID);
        if(checkExisted == null) {
            return "Không tìm thấy WorkingDate với ID là " + workingDateID + ".";
        }
        WorkingDate workingDate = checkExisted.get();

        if(!workingDate.getStatus().toString().equalsIgnoreCase("WAITING") &&
                !workingDate.getStatus().toString().equalsIgnoreCase("WORKING")) {
            return " Chỉ có thể thay đổi WorkingDate với Status là WAITING hoặc WORKING.";
        }

        workingDate.setStaff(userRepository.getById(staffID));
        workingDate.setNote(note);
        workingDateRepository.save(workingDate);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public List<ShowWorkingDateModel> getAllByContractDetailID(String contractDetailID, Pageable pageable) {
        Page<WorkingDate> pagingResult = workingDatePagingRepository.findByContractDetail_IdOrderByWorkingDateDesc(contractDetailID, pageable);
        return util.workingDatePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowWorkingDateModel> getAllByContractID(String contractID, Pageable pageable) {
        Page<WorkingDate> pagingResult = workingDatePagingRepository.findByContractDetail_Contract_IdOrderByWorkingDateDesc(contractID, pageable);
        return util.workingDatePagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowWorkingDateModel getByID(String workingDateID) {
        Optional<WorkingDate> checkExisted = workingDateRepository.findById(workingDateID);
        if(checkExisted == null) {
            return null;
        }
        WorkingDate workingDate = checkExisted.get();
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

        Double months = util.getMonthsBetween(detail.getServicePack());
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
        return model;
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
                model.setNoteWorkingDate(workingDate.getNote());
                model.setWorkingDate(workingDate.getWorkingDate());
                model.setIsReported(workingDate.getIsReported());
                model.setStatus(workingDate.getStatus());
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

                Double months = util.getMonthsBetween(detail.getServicePack());
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
                modelList.add(model);
                Collections.sort(modelList, Comparator.comparing(ShowWorkingDateModel::getWorkingDate).reversed());
            }
        }
        return modelList;
    }

    @Override
    public List<ShowWorkingDateModel> getByWorkingDate(String contractDetailID, LocalDateTime from, LocalDateTime to) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null) {
            return null;
        }
        ContractDetail detail = checkExisted.get();
        List<WorkingDate> workingDateList = workingDateRepository.findByContractDetail_IdAndWorkingDateBetween(contractDetailID, from, to);
        if(workingDateList == null) {
            return null;
        }
        List<ShowWorkingDateModel> modelList = new ArrayList<>();
        for(WorkingDate workingDate : workingDateList) {
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

//            // calculate month from date range
//            Long monthsBetween = ChronoUnit.MONTHS.between(
//                    detail.getStartDate().withDayOfMonth(1),
//                    detail.getExpectedEndDate().withDayOfMonth(1));

            Double months = util.getMonthsBetween(detail.getServicePack());
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
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowWorkingDateModel> getByWorkingDateInRange(Long userID, LocalDateTime from, LocalDateTime to, String roleName) {
        List<ContractDetail> detailList = null;
        if(roleName.equalsIgnoreCase("CUSTOMER")) {
            detailList = contractDetailRepository.findByContract_Customer_Id(userID);
        }
        if(roleName.equalsIgnoreCase("STAFF")) {
            detailList = contractDetailRepository.findByContract_Staff_Id(userID);
        }

        if(detailList == null || detailList.isEmpty()) {
            return null;
        }

        List<ShowWorkingDateModel> modelList = new ArrayList<>();
        for(ContractDetail detail : detailList) {
            List<WorkingDate> workingDateList = workingDateRepository.findByContractDetail_IdAndWorkingDateBetween(detail.getId(), from, to);
            if(workingDateList == null) {
                return null;
            }

            for(WorkingDate workingDate : workingDateList) {
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

//                // calculate month from date range
//                Long monthsBetween = ChronoUnit.MONTHS.between(
//                        detail.getStartDate().withDayOfMonth(1),
//                        detail.getExpectedEndDate().withDayOfMonth(1));

                Double months = util.getMonthsBetween(detail.getServicePack());
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
                modelList.add(model);
            }
        }
        return modelList;
    }

    @Override
    public List<ShowWorkingDateModel> getByStaffID(Long userID) {
        List<ShowWorkingDateModel> modelList = new ArrayList<>();
        List<WorkingDate> workingDateList = workingDateRepository.findByStaff_Id(userID);
        if(workingDateList == null) {
            return null;
        }

        for(WorkingDate workingDate : workingDateList) {
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

//                // calculate month from date range
//                Long monthsBetween = ChronoUnit.MONTHS.between(
//                        detail.getStartDate().withDayOfMonth(1),
//                        detail.getExpectedEndDate().withDayOfMonth(1));

            Double months = util.getMonthsBetween(detail.getServicePack());
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
            modelList.add(model);
        }

        return modelList;
    }

    @Override
    public String generateWorkingSchedule(String contractDetailID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(contractDetailID);
        if(checkExisted == null) {
            return "Không tìm thấy ContractDetail với ID là " + contractDetailID + ".";
        }

        String timeWorking = checkExisted.get().getTimeWorking();
        if(timeWorking == null) {
            return "ContractDetail không có timeWorking";
        }

        LocalDate from = checkExisted.get().getStartDate().toLocalDate();
        LocalDate to = checkExisted.get().getExpectedEndDate().toLocalDate();

        List<LocalDateTime> dateTimeList = new ArrayList<>();
        String[] timeWorkingArr;
        timeWorkingArr = timeWorking.split("-");


        for(String dayInWeek : timeWorkingArr) {
            LocalDate fromDate = from;
            while(fromDate.isBefore(to)) {
                DayOfWeek dow = fromDate.getDayOfWeek();
                switch(dayInWeek.trim().toLowerCase()) {
                    case "thứ 2":
                        if(dow.name().equalsIgnoreCase("MONDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "thứ 3":
                        if(dow.name().equalsIgnoreCase("TUESDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "thứ 4":
                        if(dow.name().equalsIgnoreCase("WEDNESDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "thứ 5":
                        if(dow.name().equalsIgnoreCase("THURSDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "thứ 6":
                        if(dow.name().equalsIgnoreCase("FRIDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "thứ 7":
                        if(dow.name().equalsIgnoreCase("SATURDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    case "chủ nhật":
                        if(dow.name().equalsIgnoreCase("SUNDAY")) {
                            dateTimeList.add(fromDate.atTime(00, 00, 00));
                        }
                        break;

                    default:
                        // Ignore any out of range
                        break;
                }
                // Set-up the next loop. Increment by one day at a time.
                fromDate = fromDate.plusDays(1);
            }
        }

        Collections.sort(dateTimeList);
        for(LocalDateTime date : dateTimeList) {
            WorkingDate workingDate = new WorkingDate();
            WorkingDate lastWorkingDate = workingDateRepository.findFirstByOrderByIdDesc();
            if(lastWorkingDate == null) {
                workingDate.setId(util.createNewID("WD"));
            } else {
                workingDate.setId(util.createIDFromLastID("WD", 2, lastWorkingDate.getId()));
            }
            workingDate.setWorkingDate(date);
            workingDate.setStatus(Status.WAITING);
            workingDate.setIsReported(false);
            workingDate.setContractDetail(checkExisted.get());
            workingDate.setStaff(checkExisted.get().getContract().getStaff());
            workingDateRepository.save(workingDate);
        }
        return "Tạo thành công.";
    }
}
