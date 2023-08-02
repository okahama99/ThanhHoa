package com.example.thanhhoa.services.servicePrice;

import com.example.thanhhoa.dtos.ServicePriceModels.CreateServicePriceModel;
import com.example.thanhhoa.dtos.ServicePriceModels.ShowServicePriceModel;
import com.example.thanhhoa.dtos.ServicePriceModels.UpdateServicePriceModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicePriceService {

    List<ShowServicePriceModel> getAllServicePrice(Pageable pageable);

    List<ShowServicePriceModel> getAllServicePriceByServiceID(String serviceID, Pageable pageable);

    ShowServicePriceModel getByID(String servicePriceID);
}
