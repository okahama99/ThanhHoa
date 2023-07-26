package com.example.thanhhoa.services.plantShipPrice;

import com.example.thanhhoa.dtos.PlantShipPriceModels.CreatePlantShipPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.pagings.PlantShipPricePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class PlantShipPriceServiceImpl implements PlantShipPriceService {

    @Autowired
    private Util util;
    @Autowired
    private PlantShipPriceRepository plantShipPriceRepository;
    @Autowired
    private PlantShipPricePagingRepository plantShipPricePagingRepository;

    @Override
    public List<ShowPlantShipPriceModel> getAllPlantShipPrice(Pageable pageable) {
        Page<PlantShipPrice> pagingResult = plantShipPricePagingRepository.findByStatus(Status.ACTIVE, pageable);
        return util.pspPagingConverter(pagingResult, pageable);
    }

    @Override
    public String createPSP(CreatePlantShipPriceModel createPlantShipPriceModel) {
        PlantShipPrice plantShipPrice = new PlantShipPrice();
        PlantShipPrice lastPlantShipPrice = plantShipPriceRepository.findFirstByOrderByIdDesc();
        if(lastPlantShipPrice == null) {
            plantShipPrice.setId(util.createNewID("PSP"));
        } else {
            plantShipPrice.setId(util.createIDFromLastID("PSP", 3, lastPlantShipPrice.getId()));
        }
        plantShipPrice.setPricePerPlant(createPlantShipPriceModel.getPricePerPlant());
        plantShipPrice.setPotSize(createPlantShipPriceModel.getPotSize());
        plantShipPrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        plantShipPrice.setStatus(Status.ACTIVE);
        plantShipPriceRepository.save(plantShipPrice);
        return "Tạo thành công.";
    }

    @Override
    public String deletePSP(String plantShipPriceID) {
        Optional<PlantShipPrice> checkExisted = plantShipPriceRepository.findByIdAndStatus(plantShipPriceID, Status.ACTIVE);
        if(checkExisted == null){
            return "Không tìm thấy Giá cây với ID là " + plantShipPriceID + ".";
        }
        PlantShipPrice plantShipPrice = checkExisted.get();
        plantShipPrice.setStatus(Status.INACTIVE);
        plantShipPriceRepository.save(plantShipPrice);
        return "Xóa thành công.";
    }
}
