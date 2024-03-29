package com.example.thanhhoa.services.service;


import com.example.thanhhoa.dtos.ServiceModels.CreateServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceModels.UpdateServiceModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceService {

    String createService(CreateServiceModel createServiceModel) throws Exception;

    String updateService(UpdateServiceModel updateServiceModel) throws Exception;

    String activeService(String serviceID) throws Exception;

    String deleteService(String serviceID);

    List<ShowServiceModel> getAllService(Pageable pageable);

    List<ShowServiceModel> getAllServiceForOwner(Pageable pageable);

    List<ShowServiceTypeModel> getServiceTypeByServiceID(String serviceID);
}
