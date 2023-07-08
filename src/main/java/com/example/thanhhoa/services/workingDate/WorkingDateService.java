package com.example.thanhhoa.services.workingDate;

import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.WorkingDate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkingDateService {

    String addWorkingDate(String contractDetailID);

    List<ShowWorkingDateModel> getAllByContractDetailID(String contractDetailID, Pageable pageable);

    WorkingDate getByID(String workingDateID);
}
