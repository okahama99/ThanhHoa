package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.CreateStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.dtos.StoreModels.UpdateStoreModel;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StoreEmployeeId;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;

    @Override
    public String createStore(CreateStoreModel createStoreModel) {
        return null;
    }

    @Override
    public String updateStore(UpdateStoreModel updateStoremodel) {
        return null;
    }

    @Override
    public Boolean deleteStore(String storeID) {
        return null;
    }

    @Override
    public String addStorePlant(AddStorePlantModel addStorePlantModel) {
        Optional<Store> store = storeRepository.findById(addStorePlantModel.getStoreID());
        if (store == null) {
            return "Không tồn tại Store với ID là " + addStorePlantModel.getStoreID() + ".";
        }
        Optional<Plant> plant = plantRepository.findById(addStorePlantModel.getPlantID());
        if (plant == null) {
            return "Không tồn tại Plant với ID là " + addStorePlantModel.getPlantID() + ".";
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
            newPlant.setReason("Nhập thêm cây");
            newPlant.setStore(store.get());
            newPlant.setPlant(plant.get());
            storePlantRepository.save(newPlant);

            StorePlantRecord storePlantRecord = new StorePlantRecord();
            storePlantRecord.setId(util.createNewID("SPR"));
            storePlantRecord.setAmount(addStorePlantModel.getQuantity());
            storePlantRecord.setImportDate(LocalDateTime.now());
            storePlantRecord.setStorePlant(newPlant);
            storePlantRecordRepository.save(storePlantRecord);
            return "Thêm thành công.";
        }
        storePlant.setQuantity(storePlant.getQuantity() + addStorePlantModel.getQuantity());
        storePlant.setReason("Nhập thêm cây");
        storePlantRepository.save(storePlant);

        StorePlantRecord storePlantRecord = new StorePlantRecord();
        storePlantRecord.setId(util.createNewID("SPR"));
        storePlantRecord.setAmount(addStorePlantModel.getQuantity());
        storePlantRecord.setImportDate(LocalDateTime.now());
        storePlantRecord.setStorePlant(storePlant);
        storePlantRecordRepository.save(storePlantRecord);
        return "Thêm thành công.";
    }

    @Override
    public List<ShowStoreModel> getAllStore() {
        List<Store> getListStore = storeRepository.findAll();
        List<ShowStoreModel> storeModelList = new ArrayList<>();
        for (Store store : getListStore) {
            ShowStoreModel model = new ShowStoreModel();
            model.setId(store.getId());
            model.setStoreName(store.getStoreName());
            model.setAddress(store.getAddress());
            model.setDistrict(store.getDistrict().getDistrictName());
            model.setPhone(store.getPhone());
            storeModelList.add(model);
        }
        return storeModelList;
    }

    @Override
    public List<ShowPlantModel> getStorePlantByStoreID(String storeID, Pageable pageable) {
        List<StorePlant> listPlant = storePlantRepository.findByStore_Id(storeID);
        if(listPlant==null){
            return null;
        }
        List<Plant> plantList = new ArrayList<>();
        for (StorePlant storePlant : listPlant) {
            plantList.add(storePlant.getPlant());
        }
        Page<Plant> pagingResult = new PageImpl<>(plantList);
        return util.plantPagingConverter(pagingResult,pageable);
    }

    @Override
    public String addStoreEmployee(AddStoreEmployeeModel addStoreEmployeeModel) {
        Optional<Store> checkExistedStore = storeRepository.findById(addStoreEmployeeModel.getStoreID());
        if(checkExistedStore == null){
            return "Không tồn tại Store với ID là " + addStoreEmployeeModel.getStoreID() + ".";
        }
        if(addStoreEmployeeModel.getEmployeeIDList() == null){
            return "Phải có danh sách ID của Employee để thêm vào Store.";
        }
        Store store = checkExistedStore.get();
        for (String employeeUsername : addStoreEmployeeModel.getEmployeeIDList()) {
            tblAccount employeeAcc = userRepository.getByUsername(employeeUsername);

            StoreEmployeeId employeeId = new StoreEmployeeId();
            employeeId.setTblAccount_id(employeeAcc.getUsername());

            StoreEmployee storeEmployee = new StoreEmployee();
            storeEmployee.setId(employeeId);
            storeEmployee.setStore(store);
            storeEmployee.setAccount(employeeAcc);
            storeEmployeeRepository.save(storeEmployee);
        }
        return "Thêm thành công.";
    }
}
