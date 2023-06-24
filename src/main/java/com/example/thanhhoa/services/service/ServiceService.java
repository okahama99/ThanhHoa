package com.example.thanhhoa.services.service;


import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceService {
    List<ShowServiceModel> getAllService(Pageable pageable);

    List<ShowServiceTypeModel> getServiceTypeByServiceID(String serviceID);
}
