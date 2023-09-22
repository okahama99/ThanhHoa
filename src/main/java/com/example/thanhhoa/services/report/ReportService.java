package com.example.thanhhoa.services.report;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.enums.Status;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;

public interface ReportService {
    List<ShowReportModel> getByUserID(Long userID);

    List<ShowReportModel> getByWorkingDateID(String workingDateID);

    String create(CreateReportModel createReportModel, Long userID) throws MessagingException;

    String update(UpdateReportModel updateReportModel);

    String delete(String reportID);

    String changeReportStatus(String reportID, String reason, Status status) throws FirebaseMessagingException;

    List<ShowReportModel> getAll(Pageable pageable);
}
