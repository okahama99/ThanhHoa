package com.example.thanhhoa.services.service;

import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ServiceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.pagings.ServicePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService{

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServicePagingRepository servicePagingRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private Util util;

    @Override
    public List<ShowServiceModel> getAllService(Pageable pageable) {
        Page<Service> pagingResult = servicePagingRepository.findAllByStatus(Status.ACTIVE, pageable);
        return util.servicePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowServiceTypeModel> getServiceTypeByServiceID(String serviceID) {
        List<ServiceType> serviceTypeList = serviceTypeRepository.findByService_Id(serviceID);
        if(serviceTypeList == null){
            return null;
        }
        List<ShowServiceTypeModel> typeList = new ArrayList<>();
        for (ServiceType serviceType : serviceTypeList) {
            ShowServiceTypeModel typeModel = new ShowServiceTypeModel();
            typeModel.setId(serviceType.getId());
            typeModel.setName(serviceType.getName());
            typeModel.setApplyDate(serviceType.getApplyDate());
            typeModel.setSize(serviceType.getSize());
            typeModel.setPercentage(serviceType.getPercentage());
            typeModel.setServiceID(serviceID);
            typeList.add(typeModel);
        }
        return typeList;
    }
}
