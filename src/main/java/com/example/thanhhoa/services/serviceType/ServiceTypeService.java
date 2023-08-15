package com.example.thanhhoa.services.serviceType;


import com.example.thanhhoa.dtos.ServiceTypeModels.CreateServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceTypeService {

    String create(CreateServiceTypeModel createServiceTypeModel);

    String delete(String serviceTypeID);

    ShowServiceTypeModel getByID(String serviceTypeID);

    List<ShowServiceTypeModel> getAll(Pageable pageable);

    List<ShowServiceTypeModel> getByServiceID(String serviceID, Pageable pageable);
}
