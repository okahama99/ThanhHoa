package com.example.thanhhoa.services.serviceType;

import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceTypeModels.CreateServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.ServiceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.pagings.ServiceTypePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Autowired
    private Util util;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private ServiceTypePagingRepository serviceTypePagingRepository;

    @Override
    public String create(CreateServiceTypeModel createServiceTypeModel) {
        Service service = serviceRepository.findByIdAndStatus(createServiceTypeModel.getServiceID(), Status.ACTIVE);
        if(service == null){
            return "Không tìm thấy Dịch vụ đang hoạt động với ID là " + createServiceTypeModel.getServiceID() + ".";
        }
        ServiceType serviceType = new ServiceType();
        ServiceType lastServiceType = serviceTypeRepository.findFirstByOrderByIdDesc();
        if(lastServiceType == null) {
            serviceType.setId(util.createNewID("ST"));
        } else {
            serviceType.setId(util.createIDFromLastID("ST", 2, lastServiceType.getId()));
        }
        serviceType.setName(createServiceTypeModel.getName());
        serviceType.setSize(createServiceTypeModel.getSize());
        serviceType.setUnit(createServiceTypeModel.getUnit());
        serviceType.setPercentage(createServiceTypeModel.getPercentage());
        serviceType.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        serviceType.setStatus(Status.ACTIVE);
        serviceType.setService(service);
        serviceTypeRepository.save(serviceType);
        return "Tạo thành công.";
    }

    @Override
    public String delete(String serviceTypeID) {
        ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(serviceTypeID, Status.ACTIVE);
        if(serviceType == null){
            return "Không tìm thấy Kiểu dịch vụ đang hoạt động với ID là " + serviceTypeID + ".";
        }
        serviceType.setStatus(Status.INACTIVE);
        serviceTypeRepository.save(serviceType);
        return "Xóa thành công.";
    }

    @Override
    public ShowServiceTypeModel getByID(String serviceTypeID) {
        ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(serviceTypeID, Status.ACTIVE);
        if(serviceType == null){
            return null;
        }
        // service
        ShowServiceModel serviceModel = new ShowServiceModel();
        Service service = serviceType.getService();
        serviceModel.setId(service.getId());
        serviceModel.setName(service.getName());
        serviceModel.setDescription(service.getDescription());
        serviceModel.setAtHome(service.getAtHome());
        ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(service.getId(), Status.ACTIVE);
        serviceModel.setPriceID(newestPrice.getId());
        serviceModel.setPrice(newestPrice.getPrice());

        ShowServiceTypeModel model = new ShowServiceTypeModel();
        model.setId(serviceType.getId());
        model.setName(serviceType.getName());
        model.setSize(serviceType.getSize());
        model.setUnit(serviceType.getUnit());
        model.setPercentage(serviceType.getPercentage());
        model.setApplyDate(serviceType.getApplyDate());
        model.setStatus(serviceType.getStatus());
        model.setShowServiceModel(serviceModel);
        return model;
    }

    @Override
    public List<ShowServiceTypeModel> getAll(Pageable pageable) {
        Page<ServiceType> pagingResult = serviceTypePagingRepository.findAll(pageable);
        return util.serviceTypePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowServiceTypeModel> getByServiceID(String serviceID, Pageable pageable) {
        Page<ServiceType> pagingResult = serviceTypePagingRepository.findByService_Id(serviceID, pageable);
        return util.serviceTypePagingConverter(pagingResult, pageable);
    }
}
