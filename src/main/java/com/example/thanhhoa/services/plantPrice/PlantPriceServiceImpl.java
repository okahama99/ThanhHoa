package com.example.thanhhoa.services.plantPrice;

import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.pagings.PlantPricePagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantPriceServiceImpl implements PlantPriceService{

    @Autowired
    private Util util;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private PlantPricePagingRepository plantPricePagingRepository;


    @Override
    public List<ShowPlantPriceModel> getAll(Pageable pageable) {
        Page<PlantPrice> pagingResult = plantPricePagingRepository.findAll(pageable);
        return util.plantPriceModelPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowPlantPriceModel> getAllByPlantID(String plantID, Pageable pageable) {
        Page<PlantPrice> pagingResult = plantPricePagingRepository.findByPlant_Id(plantID, pageable);
        return util.plantPriceModelPagingConverter(pagingResult, pageable);
    }

    @Override
    public ShowPlantPriceModel getByID(String plantPriceID) {
        Optional<PlantPrice> checkExisted = plantPriceRepository.findById(plantPriceID);
        if(checkExisted == null){
            return null;
        }
        PlantPrice plantPrice = checkExisted.get();
        ShowPlantPriceModel model = new ShowPlantPriceModel();
        model.setId(plantPrice.getId());
        model.setPrice(plantPrice.getPrice());
        model.setApplyDate(plantPrice.getApplyDate());
        model.setStatus(plantPrice.getStatus());
        return model;
    }
}
