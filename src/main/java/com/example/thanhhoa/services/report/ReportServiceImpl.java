package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.entities.Cart;
import com.example.thanhhoa.entities.ContractDetail;
import com.example.thanhhoa.entities.Report;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ReportRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private Util util;

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
            model.setCustomerName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
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
            model.setCustomerName(report.getCustomer().getFullName());
            model.setEmail(report.getCustomer().getEmail());
            model.setPhone(report.getCustomer().getPhone());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public String create(CreateReportModel createReportModel, Long userID) {
        Optional<ContractDetail> checkExisted = contractDetailRepository.findById(createReportModel.getContractDetailID());
        if(checkExisted == null){
            return "Không tìm thấy Chi tiết hợp đồng với ID là " + createReportModel.getContractDetailID() + ".";
        }
        Report report = new Report();
        Report lastReport = reportRepository.findFirstByOrderByIdDesc();
        if(lastReport == null) {
            report.setId(util.createNewID("RP"));
        } else {
            report.setId(util.createIDFromLastID("RP", 2, lastReport.getId()));
        }
        report.setCreatedDate(LocalDateTime.now());
        report.setStatus(Status.ACTIVE);
        report.setDescription(createReportModel.getDescription());
        report.setCustomer(userRepository.getById(userID));
        report.setContractDetail(checkExisted.get());
        reportRepository.save(report);
        return "Tạo thành công.";
    }

    @Override
    public String update(UpdateReportModel updateReportModel) {
        Optional<Report> checkExisted = reportRepository.findById(updateReportModel.getId());
        if(checkExisted == null){
            return "Không tìm thấy Báo cáo với ID là " + updateReportModel.getId() + ".";
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
    public String delete(String reportID) {
        Optional<Report> checkExisted = reportRepository.findById(reportID);
        if(checkExisted == null){
            return "Không tìm thấy Báo cáo với ID là " + reportID + ".";
        }
        Report report = checkExisted.get();
        report.setStatus(Status.INACTIVE);
        reportRepository.save(report);
        return "Xóa thành công.";
    }
}
