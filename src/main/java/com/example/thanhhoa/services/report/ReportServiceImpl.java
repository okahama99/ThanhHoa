package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.entities.Report;
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
            model.setWorkingDateID(report.getWorkingDate().getId());
            model.setWorkingDate(report.getWorkingDate().getWorkingDate());
            model.setContractDetailID(report.getWorkingDate().getContractDetail().getId());
            model.setServiceTypeID(report.getWorkingDate().getContractDetail().getServiceType().getId());
            model.setServiceTypeName(report.getWorkingDate().getContractDetail().getServiceType().getName());
            model.setServiceID(report.getWorkingDate().getContractDetail().getServiceType().getService().getId());
            model.setServiceName(report.getWorkingDate().getContractDetail().getServiceType().getService().getName());
            model.setDescription(report.getDescription());
            model.setCustomerID(report.getCustomer().getId());
            model.setFullName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
            model.setReason(report.getReason());
            model.setStatus(report.getStatus());
            model.setStaffID(report.getWorkingDate().getStaff().getId());
            model.setStaffName(report.getWorkingDate().getStaff().getFullName());
            model.setContractID(report.getWorkingDate().getContractDetail().getContract().getId());
            model.setTimeWorking(report.getWorkingDate().getContractDetail().getTimeWorking());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowReportModel> getByWorkingDateID(String workingDateID) {
        List<Report> reportList = reportRepository.findByWorkingDate_IdAndStatus(workingDateID, Status.INACTIVE);
        if(reportList == null) {
            return null;
        }
        List<ShowReportModel> modelList = new ArrayList<>();
        for(Report report : reportList) {
            ShowReportModel model = new ShowReportModel();
            model.setId(report.getId());
            model.setCreatedDate(report.getCreatedDate());
            model.setWorkingDateID(report.getWorkingDate().getId());
            model.setWorkingDate(report.getWorkingDate().getWorkingDate());
            model.setContractDetailID(report.getWorkingDate().getContractDetail().getId());
            model.setServiceTypeID(report.getWorkingDate().getContractDetail().getServiceType().getId());
            model.setServiceTypeName(report.getWorkingDate().getContractDetail().getServiceType().getName());
            model.setServiceID(report.getWorkingDate().getContractDetail().getServiceType().getService().getId());
            model.setServiceName(report.getWorkingDate().getContractDetail().getServiceType().getService().getName());
            model.setDescription(report.getDescription());
            model.setCustomerID(report.getCustomer().getId());
            model.setFullName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
            model.setReason(report.getReason());
            model.setStatus(report.getStatus());
            model.setStaffID(report.getWorkingDate().getStaff().getId());
            model.setStaffName(report.getWorkingDate().getStaff().getFullName());
            model.setContractID(report.getWorkingDate().getContractDetail().getContract().getId());
            model.setTimeWorking(report.getWorkingDate().getContractDetail().getTimeWorking());
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
}
