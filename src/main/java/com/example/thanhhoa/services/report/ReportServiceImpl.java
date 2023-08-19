package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.entities.Cart;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ReportRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.UserRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;
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

    @Override
    public List<ShowReportModel> getByUserID(Long userID) {
        List<Report> reportList = reportRepository.findByCustomer_IdAndStatusNot(userID, Status.INACTIVE);
        if(reportList == null){
            return null;
        }
        List<ShowReportModel> modelList = new ArrayList<>();
        for(Report report : reportList) {
            ShowReportModel model = new ShowReportModel();
            model.setId(report.getId());
            model.setCreatedDate(report.getCreatedDate());
            model.setContractDetailID(report.getContractDetail().getId());
            model.setServiceTypeID(report.getContractDetail().getServiceType().getId());
            model.setServiceTypeName(report.getContractDetail().getServiceType().getName());
            model.setServiceID(report.getContractDetail().getServiceType().getService().getId());
            model.setServiceName(report.getContractDetail().getServiceType().getService().getName());
            model.setDescription(report.getDescription());
            model.setCustomerID(report.getCustomer().getId());
            model.setFullName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
            model.setReason(report.getReason());
            model.setStatus(report.getStatus());
            model.setStaffID(report.getContractDetail().getContract().getStaff().getId());
            model.setStaffName(report.getContractDetail().getContract().getStaff().getFullName());
            model.setContractID(report.getContractDetail().getContract().getId());
            model.setTimeWorking(report.getContractDetail().getTimeWorking());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowReportModel> getByContractDetailID(String contractDetailID) {
        List<Report> reportList = reportRepository.findByContractDetail_IdAndStatusNot(contractDetailID, Status.INACTIVE);
        if(reportList == null){
            return null;
        }
        List<ShowReportModel> modelList = new ArrayList<>();
        for(Report report : reportList) {
            ShowReportModel model = new ShowReportModel();
            model.setId(report.getId());
            model.setCreatedDate(report.getCreatedDate());
            model.setContractDetailID(report.getContractDetail().getId());
            model.setServiceTypeID(report.getContractDetail().getServiceType().getId());
            model.setServiceTypeName(report.getContractDetail().getServiceType().getName());
            model.setServiceID(report.getContractDetail().getServiceType().getService().getId());
            model.setServiceName(report.getContractDetail().getServiceType().getService().getName());
            model.setDescription(report.getDescription());
            model.setCustomerID(report.getCustomer().getId());
            model.setFullName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
            model.setReason(report.getReason());
            model.setStatus(report.getStatus());
            model.setStaffID(report.getContractDetail().getContract().getStaff().getId());
            model.setStaffName(report.getContractDetail().getContract().getStaff().getFullName());
            model.setContractID(report.getContractDetail().getContract().getId());
            model.setTimeWorking(report.getContractDetail().getTimeWorking());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String create(CreateReportModel createReportModel, Long userID) throws MessagingException {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(createReportModel.getContractDetailID());
        if(checkExisted == null){
            return "Không tìm thấy Chi tiết hợp đồng với ID là " + createReportModel.getContractDetailID() + ".";
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
        report.setContractDetail(checkExisted.get());
        reportRepository.save(report);

        StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(checkExisted.get().getContract().getStore().getId(), "Manager");
        if(manager != null){
            otpService.generateNotificationEmailForManager(manager.getAccount().getEmail(),"Báo cáo");
        }

        return "Tạo thành công.";
    }

    @Override
    public String update(UpdateReportModel updateReportModel) {
        Optional<Report> checkExisted = reportRepository.findByIdAndStatus(updateReportModel.getId(), Status.NEW);
        if(checkExisted == null){
            return "Không tìm thấy Báo cáo với trạng thái NEW có ID là " + updateReportModel.getId() + ".";
        }
        Optional<ContractDetail> contractDetail = contractDetailRepository.findById(updateReportModel.getContractDetailID());
        if(contractDetail == null){
            return "Không tìm thấy Chi tiết hợp đồng với ID là " + updateReportModel.getContractDetailID() + ".";
        }
        Report report = checkExisted.get();
        report.setDescription(updateReportModel.getDescription());
        report.setContractDetail(contractDetail.get());
        reportRepository.save(report);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String delete(String reportID){
        Optional<Report> checkExisted = reportRepository.findByIdAndStatus(reportID, Status.NEW);
        if(checkExisted == null){
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
        if(checkExisted == null){
            return "Không tìm thấy Báo cáo với trạng thái NEW có ID là " + reportID + ".";
        }
        Report report = checkExisted.get();
        if(status.toString().equalsIgnoreCase("APPROVED")){
            report.setStatus(status);
            reportRepository.save(report);

            util.createNotification("REPORT", report.getCustomer(), report.getId(), "quản lý duyệt");

            return "Chỉnh sửa thành công.";
        }else if(status.toString().equalsIgnoreCase("DENIED")){
            if(reason == null){
                return "Phải có Lí do khi từ chối báo cáo";
            }
            report.setStatus(status);
            report.setReason(reason);
            reportRepository.save(report);

            util.createNotification("REPORT", report.getCustomer(), report.getId(), "từ chối");

            return "Chỉnh sửa thành công.";
        }else{
            return "Trạng thái phải là APPROVED hoặc DENIED";
        }
    }

    @Override
    public List<ShowReportModel> getAllNewReport(Pageable pageable) {
        Page<Report> pagingResult = reportPagingRepository.findAllByStatus(Status.NEW, pageable);
        return util.reportPagingConverter(pagingResult, pageable);
    }
}
