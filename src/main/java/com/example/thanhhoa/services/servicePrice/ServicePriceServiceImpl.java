package com.example.thanhhoa.services.servicePrice;

import com.example.thanhhoa.dtos.ServiceModels.ShowServiceIMGModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ServicePriceModels.ShowServicePriceModel;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceIMG;
import com.example.thanhhoa.entities.ServicePrice;
import com.example.thanhhoa.entities.ServiceType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ServiceIMGRepository;
import com.example.thanhhoa.repositories.ServicePriceRepository;
import com.example.thanhhoa.repositories.ServiceTypeRepository;
import com.example.thanhhoa.repositories.pagings.ServicePricePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicePriceServiceImpl implements ServicePriceService {

    @Autowired
    private Util util;
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private ServicePricePagingRepository servicePricePagingRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ServiceIMGRepository serviceIMGRepository;

    @Override
    public List<ShowServicePriceModel> getAllServicePrice(Pageable pageable) {
        Page<ServicePrice> pagingResult = servicePricePagingRepository.findAll(pageable);
        return util.servicePricePagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowServicePriceModel> getAllServicePriceByServiceID(String serviceID, Pageable pageable) {
        Page<ServicePrice> pagingResult = servicePricePagingRepository.findAllByService_Id(serviceID, pageable);
        return util.servicePricePagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowServicePriceModel getByID(String servicePriceID) {
        Optional<ServicePrice> checkExisted = servicePriceRepository.findById(servicePriceID);
        if(checkExisted == null) {
            return null;
        }
        ServicePrice servicePrice = checkExisted.get();
        ShowServicePriceModel model = new ShowServicePriceModel();

        //service
        Service service = servicePrice.getService();
        ShowServiceModel serviceModel = new ShowServiceModel();
        List<ShowServiceTypeModel> typeList = new ArrayList<>();
        List<ServiceType> serviceTypeList = serviceTypeRepository.findByService_IdAndStatus(service.getId(), Status.ACTIVE);
        if(serviceTypeList != null) {
            for(ServiceType serviceType : serviceTypeList) {
                ShowServiceTypeModel typeModel = new ShowServiceTypeModel();
                typeModel.setId(serviceType.getId());
                typeModel.setName(serviceType.getName());
                typeModel.setApplyDate(serviceType.getApplyDate());
                typeModel.setSize(serviceType.getSize());
                typeModel.setUnit(serviceType.getUnit());
                typeModel.setPercentage(serviceType.getPercentage());
                typeModel.setServiceID(service.getId());
                typeList.add(typeModel);
            }
        }
        List<ShowServiceIMGModel> imgList = new ArrayList<>();
        List<ServiceIMG> serviceIMGList = serviceIMGRepository.findByService_Id(service.getId());
        if(serviceIMGList != null) {
            for(ServiceIMG img : serviceIMGList) {
                ShowServiceIMGModel imgModel = new ShowServiceIMGModel();
                imgModel.setId(img.getId());
                imgModel.setUrl(img.getImgURL());
                imgList.add(imgModel);
            }
        }
        serviceModel.setServiceID(service.getId());
        serviceModel.setName(service.getName());
        ServicePrice newestPrice = servicePriceRepository.findFirstByService_IdAndStatusOrderByApplyDateDesc(service.getId(), Status.ACTIVE);
        serviceModel.setPriceID(newestPrice.getId());
        serviceModel.setPrice(newestPrice.getPrice());
        serviceModel.setDescription(service.getDescription());
        serviceModel.setTypeList(typeList);
        serviceModel.setImgList(imgList);
        serviceModel.setStatus(service.getStatus());
        serviceModel.setAtHome(service.getAtHome());

        model.setId(servicePrice.getId());
        model.setApplyDate(servicePrice.getApplyDate());
        model.setStatus(servicePrice.getStatus());
        model.setPrice(servicePrice.getPrice());
        model.setServiceModel(serviceModel);
        return model;
    }
}
