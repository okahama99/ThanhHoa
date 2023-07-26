package com.example.thanhhoa.services.distancePrice;

import com.example.thanhhoa.dtos.DistancePriceModels.ShowDistancePriceModel;
import com.example.thanhhoa.entities.DistancePrice;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.DistancePriceRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class DistancePriceServiceImpl implements DistancePriceService{

    @Autowired
    private Util util;
    @Autowired
    private DistancePriceRepository distancePriceRepository;

    @Override
    public String create(Double pricePerKm) {
        DistancePrice distancePrice = new DistancePrice();
        DistancePrice lastDistancePrice = distancePriceRepository.findFirstByOrderByIdDesc();
        if(lastDistancePrice == null) {
            distancePrice.setId(util.createNewID("DP"));
        } else {
            distancePrice.setId(util.createIDFromLastID("DP", 2, lastDistancePrice.getId()));
            lastDistancePrice.setStatus(Status.INACTIVE);
            distancePriceRepository.save(lastDistancePrice);
        }
        distancePrice.setPricePerKm(pricePerKm);
        distancePrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        distancePrice.setStatus(Status.ACTIVE);
        distancePriceRepository.save(distancePrice);
        return "Tạo thành công.";
    }

    @Override
    public ShowDistancePriceModel getNewestDistancePrice() {
        DistancePrice lastDistancePrice = distancePriceRepository.findByStatus(Status.ACTIVE);
        ShowDistancePriceModel model = new ShowDistancePriceModel();
        model.setDistancePriceID(lastDistancePrice.getId());
        model.setApplyDate(lastDistancePrice.getApplyDate());
        model.setPricePerKm(lastDistancePrice.getPricePerKm());
        model.setStatus(lastDistancePrice.getStatus());
        return model;
    }
}
