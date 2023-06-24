package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService{

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private StorePlantRecordRepository storePlantRecordRepository;
    @Autowired
    private Util util;

    @Override
    public String addStorePlant(AddStorePlantModel addStorePlantModel) {
        Optional<Store> store = storeRepository.findById(addStorePlantModel.getStoreID());
        if (store == null) {
            return "Không tồn tại Store với ID là " + addStorePlantModel.getStoreID() + "";
        }
        Optional<Plant> plant = plantRepository.findById(addStorePlantModel.getPlantID());
        if (plant == null) {
            return "Không tồn tại Plant với ID là " + addStorePlantModel.getPlantID() + "";
        }
        StorePlant storePlant = storePlantRepository.findByPlantIdAndStoreId(addStorePlantModel.getPlantID(), addStorePlantModel.getStoreID());
        if (storePlant == null) {
            StorePlant newPlant = new StorePlant();
            StorePlant lastStorePlant = storePlantRepository.findFirstByOrderByIdDesc();
            if (lastStorePlant == null) {
                newPlant.setId(util.createNewID("SP"));
            } else {
                newPlant.setId(util.createIDFromLastID("SP", 2, lastStorePlant.getId()));
            }
            newPlant.setQuantity(addStorePlantModel.getQuantity());
            newPlant.setStore(store.get());
            newPlant.setPlant(plant.get());
            storePlantRepository.save(newPlant);

            StorePlantRecord storePlantRecord = new StorePlantRecord();
            storePlantRecord.setId(util.createNewID("PR"));
            storePlantRecord.setAmount(addStorePlantModel.getQuantity());
            storePlantRecord.setImportDate(LocalDateTime.now());
            storePlantRecord.setStorePlant(newPlant);
            storePlantRecordRepository.save(storePlantRecord);
            return "Thêm thành công";
        }
        storePlant.setQuantity(storePlant.getQuantity() + addStorePlantModel.getQuantity());
        storePlantRepository.save(storePlant);

        StorePlantRecord storePlantRecord = new StorePlantRecord();
        storePlantRecord.setId(util.createNewID("PR"));
        storePlantRecord.setAmount(addStorePlantModel.getQuantity());
        storePlantRecord.setImportDate(LocalDateTime.now());
        storePlantRecord.setStorePlant(storePlant);
        storePlantRecordRepository.save(storePlantRecord);
        return "Thêm thành công";
    }

    @Override
    public List<ShowStoreModel> getAllStore() {
        return null;
    }

    @Override
    public List<ShowPlantModel> getStorePlantByStoreID(String storeID, Pageable pageable) {
        return null;
    }

    @Override
    public String addStoreEmployee(AddStoreEmployeeModel addStoreEmployeeModel) {
        return null;
    }
}
