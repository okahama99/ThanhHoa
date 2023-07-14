package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;

import java.util.List;

public interface ReportService {
    List<ShowReportModel> getByUserID(Long userID);

    List<ShowReportModel> getByContractDetailID(String contractDetailID);

    String create(CreateReportModel createReportModel, Long userID);

    String update(UpdateReportModel updateReportModel);

    String delete(String reportID);
}
