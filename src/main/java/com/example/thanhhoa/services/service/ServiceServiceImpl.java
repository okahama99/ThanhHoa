package com.example.thanhhoa.services.service;

import com.example.thanhhoa.dtos.ServiceModels.CreateServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceModels.UpdateServiceModel;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceIMG;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractDetailRepository;
import com.example.thanhhoa.repositories.ServiceIMGRepository;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.ServiceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.pagings.ServicePagingRepository;
import com.example.thanhhoa.services.firebaseIMG.FirebaseImageService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServicePagingRepository servicePagingRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private Util util;
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private ServiceIMGRepository serviceIMGRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    @Override
    public String createService(CreateServiceModel createServiceModel) throws Exception {
        Service checkExisted = serviceRepository.findByName(createServiceModel.getName());
        if(checkExisted != null) {
            return "Service với tên là " + createServiceModel.getName() + " đã tồn tại.";
        }
        if(createServiceModel.getTypeIDList() == null) {
            return "Service phải có ít nhất 1 ServiceType.";
        }

        Service service = new Service();
        Service getLastService = serviceRepository.findFirstByOrderByIdDesc();
        if(getLastService != null) {
            service.setId(util.createIDFromLastID("SE", 2, getLastService.getId()));
        } else {
            service.setId(util.createNewID("SE"));
        }

        // service type
        List<ServiceType> serviceTypeList = new ArrayList<>();
        for(String serviceTypeID : createServiceModel.getTypeIDList()) {
            ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(serviceTypeID, Status.ACTIVE);
            if(serviceType == null) {
                return "Không tìm thấy ServiceType với ID là " + serviceTypeID + ".";
            }
            serviceTypeList.add(serviceType);
        }

        //service price
        ServicePrice servicePrice = new ServicePrice();
        ServicePrice lastServicePrice = servicePriceRepository.findFirstByOrderByIdDesc();
        if(lastServicePrice == null) {
            servicePrice.setId(util.createNewID("SEP"));
        } else {
            servicePrice.setId(util.createIDFromLastID("SEP", 3, lastServicePrice.getId()));
        }
        servicePrice.setPrice(createServiceModel.getPrice());
        servicePrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        servicePrice.setService(service);
        servicePrice.setStatus(Status.ACTIVE);

        service.setName(createServiceModel.getName());
        service.setDescription(createServiceModel.getDescription());
        service.setServiceTypeList(serviceTypeList);
        service.setStatus(Status.ACTIVE);
        service.setAtHome(createServiceModel.getAtHome());

        // service img
        for(String imageURL : createServiceModel.getListURL()) {
            ServiceIMG serviceIMG = new ServiceIMG();
            ServiceIMG lastServiceIMG = serviceIMGRepository.findFirstByOrderByIdDesc();
            if(lastServiceIMG == null) {
                serviceIMG.setId(util.createNewID("SIMG"));
            } else {
                serviceIMG.setId(util.createIDFromLastID("SIMG", 4, lastServiceIMG.getId()));
            }
            serviceIMG.setService(service);
            serviceIMG.setImgURL(imageURL);
            serviceIMGRepository.save(serviceIMG);
        }
        servicePriceRepository.save(servicePrice);
        serviceRepository.save(service);
        return "Tạo thành công.";
    }

    @Override
    public String updateService(UpdateServiceModel updateServiceModel) throws Exception {
        Optional<Service> checkExisted = serviceRepository.findById(updateServiceModel.getServiceID());
        if(checkExisted == null) {
            return "Không tìm thấy dữ liệu với ServiceID = " + updateServiceModel.getServiceID() + ".";
        }
        if(updateServiceModel.getTypeIDList() == null) {
            return "Service phải có ít nhất 1 ServiceType.";
        }
        List<ServiceType> serviceTypeList = new ArrayList<>();
        for(String serviceTypeID : updateServiceModel.getTypeIDList()) {
            ServiceType serviceType = serviceTypeRepository.findByIdAndStatus(serviceTypeID, Status.ACTIVE);
            if(serviceType == null) {
                return "Không tìm thấy ServiceType với ID là " + serviceTypeID + ".";
            }
            serviceTypeList.add(serviceType);
        }
        Service service = checkExisted.get();
        service.setName(updateServiceModel.getName());
        service.setDescription(updateServiceModel.getDescription());
        service.setServiceTypeList(serviceTypeList);
        service.setAtHome(updateServiceModel.getAtHome());

        //service price
        ServicePrice servicePrice = new ServicePrice();
        List<ServicePrice> checkExistedPrice = servicePriceRepository.findByService_IdAndStatus(service.getId(), Status.ACTIVE);
        if(checkExistedPrice != null) {
            for(ServicePrice sPrice : checkExistedPrice) {
                sPrice.setStatus(Status.INACTIVE);
                servicePriceRepository.save(sPrice);
            }
        }
        ServicePrice lastServicePrice = servicePriceRepository.findFirstByOrderByIdDesc();
        if(lastServicePrice == null) {
            servicePrice.setId(util.createNewID("SEP"));
        } else {
            servicePrice.setId(util.createIDFromLastID("SEP", 3, lastServicePrice.getId()));
        }
        servicePrice.setPrice(updateServiceModel.getPrice());
        servicePrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        servicePrice.setService(service);
        servicePrice.setStatus(Status.ACTIVE);

        for(String imageURL : updateServiceModel.getListURL()) {
            ServiceIMG serviceIMG = new ServiceIMG();
            ServiceIMG lastServiceIMG = serviceIMGRepository.findFirstByOrderByIdDesc();
            if(lastServiceIMG == null) {
                serviceIMG.setId(util.createNewID("SIMG"));
            } else {
                serviceIMG.setId(util.createIDFromLastID("SIMG", 4, lastServiceIMG.getId()));
            }
            serviceIMG.setService(service);
            serviceIMG.setImgURL(imageURL);
            serviceIMGRepository.save(serviceIMG);
        }
        serviceRepository.save(service);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String deleteService(String serviceID) {
        Optional<Service> checkExisted = serviceRepository.findById(serviceID);
        if(checkExisted != null) {
            Service service = checkExisted.get();

            for(ServiceType type : service.getServiceTypeList()) {
                if(contractDetailRepository.findByServiceType_IdAndContract_Status(type.getId(), Status.WAITING) != null) {
                    return "Không thể xóa dịch vụ đang được sử dụng.";
                }
            }

            for(ServiceType type : service.getServiceTypeList()) {
                type.setStatus(Status.INACTIVE);
                serviceTypeRepository.save(type);
            }

            service.setStatus(Status.INACTIVE);
            serviceRepository.save(service);
            return "Xóa thành công.";
        }
        return "Không tìm thấy dịch vụ có ID là " + serviceID + ".";
    }

    @Override
    public List<ShowServiceModel> getAllService(Pageable pageable) {
        Page<Service> pagingResult = servicePagingRepository.findAllByStatus(Status.ACTIVE, pageable);
        return util.servicePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowServiceModel> getAllServiceForOwner(Pageable pageable) {
        Page<Service> pagingResult = servicePagingRepository.findAll(pageable);
        return util.servicePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowServiceTypeModel> getServiceTypeByServiceID(String serviceID) {
        List<ServiceType> serviceTypeList = serviceTypeRepository.findByService_IdAndStatus(serviceID, Status.ACTIVE);
        if(serviceTypeList == null) {
            return null;
        }
        List<ShowServiceTypeModel> typeList = new ArrayList<>();
        for(ServiceType serviceType : serviceTypeList) {
            ShowServiceTypeModel typeModel = new ShowServiceTypeModel();
            typeModel.setId(serviceType.getId());
            typeModel.setName(serviceType.getName());
            typeModel.setApplyDate(serviceType.getApplyDate());
            typeModel.setSize(serviceType.getSize());
            typeModel.setPercentage(serviceType.getPercentage());
            typeModel.setServiceID(serviceID);
            typeModel.setUnit(serviceType.getUnit());
            typeList.add(typeModel);
        }
        return typeList;
    }
}
