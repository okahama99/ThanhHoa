package com.example.thanhhoa.services.workingDate;

import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.WorkingDate;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkingDateService {

    String addStartWorkingDate(String workingDateID, String startWorkingIMG, Long staffID);

    String addEndWorkingDate(String workingDateID, String endWorkingIMG, Long staffID);

    List<ShowWorkingDateModel> getAllByContractDetailID(String contractDetailID, Pageable pageable);

    WorkingDate getByID(String workingDateID);

    List<ShowWorkingDateModel> getWorkingDateByStaffID(Long staffID);

    List<ShowWorkingDateModel> getByWorkingDate(String contractDetailID, LocalDateTime from, LocalDateTime to);

    String generateWorkingSchedule(String contractDetailID);
}
