package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ReportRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.WorkingDateRepository;
import com.example.thanhhoa.repositories.pagings.ReportPagingRepository;
import com.example.thanhhoa.services.otp.OtpService;
import com.example.thanhhoa.utils.Util;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportPagingRepository reportPagingRepository;
    @Autowired
    private Util util;
    @Autowired
    private OtpService otpService;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    private WorkingDateRepository workingDateRepository;

    @Override
    public List<ShowReportModel> getByUserID(Long userID) {
        List<Report> reportList = reportRepository.findByCustomer_IdAndStatusNot(userID, Status.INACTIVE);
        if(reportList == null) {
            return null;
        }
        Collections.reverse(reportList);
        List<ShowReportModel> modelList = new ArrayList<>();
        for(Report report : reportList) {
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
            ShowWorkingDateModel showWorkingDateModel = new ShowWorkingDateModel();
            if(workingDate != null) {
                showWorkingDateModel.setId(workingDate.getId());
                showWorkingDateModel.setWorkingDate(workingDate.getWorkingDate());
                showWorkingDateModel.setIsReported(workingDate.getIsReported());
                showWorkingDateModel.setStatus(workingDate.getStatus());
            }

            ServiceType serviceType = report.getWorkingDate().getContractDetail().getServiceType();
            ShowServiceTypeModel showServiceTypeModel = new ShowServiceTypeModel();
            if(serviceType != null) {
                showServiceTypeModel.setId(serviceType.getId());
                showServiceTypeModel.setTypeName(serviceType.getName());
                showServiceTypeModel.setTypeSize(serviceType.getSize());
                showServiceTypeModel.setTypeUnit(serviceType.getUnit());
                showServiceTypeModel.setTypePercentage(serviceType.getPercentage());
                showServiceTypeModel.setTypeApplyDate(serviceType.getApplyDate());
            }

            com.example.thanhhoa.entities.Service service = report.getWorkingDate().getContractDetail().getServiceType().getService();
            ShowServiceModel showServiceModel = new ShowServiceModel();
            if(service != null) {
                showServiceModel.setId(service.getId());
                showServiceModel.setDescription(service.getDescription());
                showServiceModel.setName(service.getName());
                showServiceModel.setAtHome(service.getAtHome());
            }

            tblAccount customer = report.getCustomer();
            ShowCustomerModel showCustomerModel = new ShowCustomerModel();
            if(customer != null) {
                showCustomerModel.setId(customer.getId());
                showCustomerModel.setAddress(customer.getAddress());
                showCustomerModel.setEmail(customer.getEmail());
                showCustomerModel.setPhone(customer.getPhone());
                showCustomerModel.setFullName(customer.getFullName());
                showCustomerModel.setAvatar(customer.getAvatar());
            }

            tblAccount staff = report.getWorkingDate().getStaff();
            ShowStaffModel showStaffModel = new ShowStaffModel();
            if(staff != null) {
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
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowReportModel> getByWorkingDateID(String workingDateID) {
        List<Report> reportList = reportRepository.findByWorkingDate_Id(workingDateID);
        if(reportList == null) {
            return null;
        }
        List<ShowReportModel> modelList = new ArrayList<>();
        for(Report report : reportList) {
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
            ShowWorkingDateModel showWorkingDateModel = new ShowWorkingDateModel();
            if(workingDate != null) {
                showWorkingDateModel.setId(workingDate.getId());
                showWorkingDateModel.setWorkingDate(workingDate.getWorkingDate());
                showWorkingDateModel.setIsReported(workingDate.getIsReported());
                showWorkingDateModel.setStatus(workingDate.getStatus());
            }

            ServiceType serviceType = report.getWorkingDate().getContractDetail().getServiceType();
            ShowServiceTypeModel showServiceTypeModel = new ShowServiceTypeModel();
            if(serviceType != null) {
                showServiceTypeModel.setId(serviceType.getId());
                showServiceTypeModel.setTypeName(serviceType.getName());
                showServiceTypeModel.setTypeSize(serviceType.getSize());
                showServiceTypeModel.setTypeUnit(serviceType.getUnit());
                showServiceTypeModel.setTypePercentage(serviceType.getPercentage());
                showServiceTypeModel.setTypeApplyDate(serviceType.getApplyDate());
            }

            com.example.thanhhoa.entities.Service service = report.getWorkingDate().getContractDetail().getServiceType().getService();
            ShowServiceModel showServiceModel = new ShowServiceModel();
            if(service != null) {
                showServiceModel.setId(service.getId());
                showServiceModel.setDescription(service.getDescription());
                showServiceModel.setName(service.getName());
                showServiceModel.setAtHome(service.getAtHome());
            }

            tblAccount customer = report.getCustomer();
            ShowCustomerModel showCustomerModel = new ShowCustomerModel();
            if(customer != null) {
                showCustomerModel.setId(customer.getId());
                showCustomerModel.setAddress(customer.getAddress());
                showCustomerModel.setEmail(customer.getEmail());
                showCustomerModel.setPhone(customer.getPhone());
                showCustomerModel.setFullName(customer.getFullName());
                showCustomerModel.setAvatar(customer.getAvatar());
            }

            tblAccount staff = report.getWorkingDate().getStaff();
            ShowStaffModel showStaffModel = new ShowStaffModel();
            if(staff != null) {
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
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String create(CreateReportModel createReportModel, Long userID) throws MessagingException {
        Optional<WorkingDate> checkExisted = workingDateRepository.findById(createReportModel.getWorkingDateID());
        if(checkExisted == null) {
            return "Không tìm thấy WorkingDate với ID là " + createReportModel.getWorkingDateID() + ".";
        }

        tblAccount customer = userRepository.getById(userID);

        Report report = new Report();
        Report lastReport = reportRepository.findFirstByOrderByIdDesc();
        if(lastReport == null) {
            report.setId(util.createNewID("RP"));
        } else {
            report.setId(util.createIDFromLastID("RP", 2, lastReport.getId()));
        }
        report.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        report.setStatus(Status.NEW);
        report.setDescription(createReportModel.getDescription());
        report.setCustomer(customer);
        report.setWorkingDate(checkExisted.get());

        checkExisted.get().setIsReported(true);
        workingDateRepository.save(checkExisted.get());
        reportRepository.save(report);

        StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName("S002", "Manager");
        if(manager != null) {
            otpService.generateNotificationEmailForManager(manager.getAccount().getEmail(), "Báo cáo");
        }

        return "Tạo thành công.";
    }

    @Override
    public String update(UpdateReportModel updateReportModel) {
        Optional<Report> checkExisted = reportRepository.findByIdAndStatus(updateReportModel.getId(), Status.NEW);
        if(checkExisted == null) {
            return "Không tìm thấy Báo cáo với trạng thái NEW có ID là " + updateReportModel.getId() + ".";
        }
        Optional<WorkingDate> workingDate = workingDateRepository.findById(updateReportModel.getWorkingDateID());
        if(checkExisted == null) {
            return "Không tìm thấy WorkingDate với ID là " + updateReportModel.getWorkingDateID() + ".";
        }
        Report report = checkExisted.get();
        report.setDescription(updateReportModel.getDescription());
        report.setWorkingDate(workingDate.get());
        reportRepository.save(report);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String delete(String reportID) {
        Optional<Report> checkExisted = reportRepository.findByIdAndStatus(reportID, Status.NEW);
        if(checkExisted == null) {
            return "Không tìm thấy Báo cáo với trạng thái NEW có ID là " + reportID + ".";
        }
        Report report = checkExisted.get();
        report.setStatus(Status.INACTIVE);

        WorkingDate workingDate = report.getWorkingDate();
        workingDate.setIsReported(false);

        workingDateRepository.save(workingDate);
        reportRepository.save(report);
        return "Xóa thành công.";
    }

    @Override
    public String changeReportStatus(String reportID, String reason, Status status) throws FirebaseMessagingException {
        Optional<Report> checkExisted = reportRepository.findByIdAndStatus(reportID, Status.NEW);
        if(checkExisted == null) {
            return "Không tìm thấy Báo cáo với trạng thái NEW có ID là " + reportID + ".";
        }
        Report report = checkExisted.get();
        if(status.toString().equalsIgnoreCase("APPROVED")) {
            report.setStatus(status);
            reportRepository.save(report);

            util.createNotification("REPORT", report.getCustomer(), report.getId(), "quản lý duyệt");

            return "Chỉnh sửa thành công.";
        } else if(status.toString().equalsIgnoreCase("DENIED")) {
            if(reason == null) {
                return "Phải có Lí do khi từ chối báo cáo";
            }
            report.setStatus(status);
            report.setReason(reason);

            WorkingDate workingDate = report.getWorkingDate();
            workingDate.setIsReported(false);

            workingDateRepository.save(workingDate);
            reportRepository.save(report);

            util.createNotification("REPORT", report.getCustomer(), report.getId(), "từ chối");

            return "Chỉnh sửa thành công.";
        } else {
            return "Trạng thái phải là APPROVED hoặc DENIED";
        }
    }

    @Override
    public List<ShowReportModel> getAll(Pageable pageable) {
        Page<Report> pagingResult = reportPagingRepository.findAll(pageable);
        return util.reportPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowReportModel> getByContractDetailID(String contractDetailID, Pageable pageable) {
        Page<Report> pagingResult = reportPagingRepository.findByWorkingDate_ContractDetail_Id(contractDetailID, pageable);
        return util.reportPagingConverter(pagingResult, pageable);
    }
}
