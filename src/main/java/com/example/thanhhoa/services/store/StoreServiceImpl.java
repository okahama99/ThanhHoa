package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.CreateStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowDistrictModel;
import com.example.thanhhoa.dtos.StoreModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.ShowProvinceModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantRecordModel;
import com.example.thanhhoa.dtos.StoreModels.UpdateStoreModel;
import com.example.thanhhoa.entities.District;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Province;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StoreEmployeeId;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.DistrictRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.ProvinceRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.StoreEmployeePagingRepository;
import com.example.thanhhoa.repositories.pagings.StorePlantPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private StorePlantPagingRepository storePlantPagingRepository;
    @Autowired
    private StoreEmployeePagingRepository storeEmployeePagingRepository;

    @Override
    public String createStore(CreateStoreModel createStoreModel) {
        Store checkDuplicate = storeRepository.findByStoreName(createStoreModel.getStoreName());
        if(checkDuplicate != null){
            return "Đã tồn tại Cửa hàng với tên là " + createStoreModel.getStoreName() + ".";
        }
        Store store = new Store();
        Store lastStore = storeRepository.findFirstByOrderByIdDesc();
        if (lastStore == null) {
            store.setId(util.createNewID("S"));
        } else {
            store.setId(util.createIDFromLastID("S", 1, lastStore.getId()));
        }
        store.setStoreName(createStoreModel.getStoreName());
        store.setAddress(createStoreModel.getAddress());
        store.setDistrict(districtRepository.getById(createStoreModel.getDistrictID()));
        store.setStatus(Status.ACTIVE);
        store.setPhone(createStoreModel.getPhone());
        storeRepository.save(store);
        return "Tạo thành công.";
    }

    @Override
    public String updateStore(UpdateStoreModel updateStoremodel) {
        Optional<Store> checkExisted = storeRepository.findById(updateStoremodel.getStoreID());
        if(checkExisted == null){
            return "Không tìm thấy Cửa hàng với tên là " + updateStoremodel.getStoreName() + ".";
        }
        Store store = checkExisted.get();
        store.setStoreName(updateStoremodel.getStoreName());
        store.setAddress(updateStoremodel.getAddress());
        store.setPhone(updateStoremodel.getPhone());
        store.setDistrict(districtRepository.getById(updateStoremodel.getDistrictID()));
        storeRepository.save(store);
        return "Cập nhật thành công.";
    }

    @Override
    public String deleteStore(String storeID) {
        Optional<Store> checkExisted = storeRepository.findById(storeID);
        if(checkExisted == null){
            return "Không tìm thấy Cửa hàng với tên là " + storeID + ".";
        }
        Store store = checkExisted.get();
        store.setStatus(Status.INACTIVE);
        storeRepository.save(store);
        return "Xóa thành công.";
    }

    @Override
    public ShowStoreModel getByID(String storeID) {
        Optional<Store> checkExisted = storeRepository.findById(storeID);
        if(checkExisted == null){
            return null;
        }
        Store store = checkExisted.get();
        StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
        ShowStoreModel model = new ShowStoreModel();
        model.setId(store.getId());
        model.setStatus(store.getStatus());
        model.setStoreName(store.getStoreName());
        model.setAddress(store.getAddress());
        model.setDistrict(store.getDistrict().getDistrictName());
        model.setPhone(store.getPhone());
        model.setManagerID(manager.getAccount().getId());
        model.setManagerName(manager.getAccount().getFullName());
        return model;
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
        StorePlant storePlant = storePlantRepository.findByPlantIdAndStoreIdAndPlant_Status
                (addStorePlantModel.getPlantID(), addStorePlantModel.getStoreID(), Status.ONSALE);
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
            StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
            if (lastRecord == null) {
                storePlantRecord.setId(util.createNewID("SPR"));
            } else {
                storePlantRecord.setId(util.createIDFromLastID("SPR",3,lastRecord.getId()));
            }
            storePlantRecord.setAmount(addStorePlantModel.getQuantity());
            storePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            storePlantRecord.setStorePlant(newPlant);
            storePlantRecord.setReason("Nhập thêm cây");
            storePlantRecordRepository.save(storePlantRecord);
            storePlantRepository.save(newPlant);
            return "Thêm thành công.";
        }
        storePlant.setQuantity(storePlant.getQuantity() + addStorePlantModel.getQuantity());

        storePlantRepository.save(storePlant);

        StorePlantRecord storePlantRecord = new StorePlantRecord();
        StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
        if (lastRecord == null) {
            storePlantRecord.setId(util.createNewID("SPR"));
        } else {
            storePlantRecord.setId(util.createIDFromLastID("SPR",3,lastRecord.getId()));
        }
        storePlantRecord.setAmount(addStorePlantModel.getQuantity());
        storePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        storePlantRecord.setStorePlant(storePlant);
        storePlantRecord.setReason("Nhập thêm cây");
        storePlantRecordRepository.save(storePlantRecord);
        return "Thêm thành công.";
    }

    @Override
    public String removeStorePlant(String storePlantID, Integer quantity, String reason, Long userID){
        Optional<StorePlant> checkExisted = storePlantRepository.findById(storePlantID);
        if(checkExisted == null || checkExisted.isEmpty()){
            return "Không tìm thấy StorePlant với ID là " + storePlantID + ".";
        }
        StorePlant storePlant = checkExisted.get();
        if(storePlant.getQuantity() < quantity){
            return "Số lượng cây hiện tại ( " + storePlant.getQuantity() + " ) ít hơn số cây yêu cầu : " + quantity + ".";
        }
        storePlant.setQuantity(storePlant.getQuantity() - quantity);

        StorePlantRecord storePlantRecord = new StorePlantRecord();
        StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
        if (lastRecord == null) {
            storePlantRecord.setId(util.createNewID("SPR"));
        } else {
            storePlantRecord.setId(util.createIDFromLastID("SPR",3,lastRecord.getId()));
        }
        storePlantRecord.setAmount(quantity);
        storePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        storePlantRecord.setStorePlant(storePlant);
        storePlantRecord.setReason("ManagerID : " + userID + ", lí do :" + reason);
        storePlantRecordRepository.save(storePlantRecord);
        storePlantRepository.save(storePlant);
        return "Xóa thành công.";
    }

    @Override
    public List<ShowStoreModel> getAllStore() {
        List<Store> getListStore = storeRepository.findAll();
        List<ShowStoreModel> storeModelList = new ArrayList<>();
        for (Store store : getListStore) {
            StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
            ShowStoreModel model = new ShowStoreModel();
            model.setId(store.getId());
            model.setStatus(store.getStatus());
            model.setStoreName(store.getStoreName());
            model.setAddress(store.getAddress());
            model.setDistrict(store.getDistrict().getDistrictName());
            model.setPhone(store.getPhone());
            if(manager != null){
                model.setManagerID(manager.getAccount().getId());
                model.setManagerName(manager.getAccount().getFullName());
            }
            storeModelList.add(model);
        }
        return storeModelList;
    }

    @Override
    public List<ShowPlantModel> getStorePlantByStoreID(String storeID, Pageable pageable) {
        Page<StorePlant> pagingResult = storePlantPagingRepository.findByStore_IdAndPlant_StatusAndStore_Status(storeID, Status.ONSALE, Status.ACTIVE, pageable);
        return util.storePlantPagingConverter(pagingResult,pageable);
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
        for (Long employeeUserID : addStoreEmployeeModel.getEmployeeIDList()) {
            tblAccount employeeAcc = userRepository.getById(employeeUserID);
            StoreEmployee check = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
            if(check != null){
                if(employeeAcc.getRole().getRoleName().equalsIgnoreCase("Manager")){
                    return "Store đã có Manager, 1 Store chỉ được 1 Manager quản lý.";
                }

            }

            StoreEmployeeId employeeId = new StoreEmployeeId();
            employeeId.setTblAccount_id(employeeAcc.getId());

            StoreEmployee storeEmployee = new StoreEmployee();
            storeEmployee.setId(employeeId);
            storeEmployee.setStore(store);
            storeEmployee.setAccount(employeeAcc);
            storeEmployee.setStatus(Status.ACTIVE);
            storeEmployeeRepository.save(storeEmployee);
            employeeAcc.setStatus(Status.AVAILABLE);
            userRepository.save(employeeAcc);
        }
        return "Thêm thành công.";
    }

    @Override
    public String removeStoreEmployee(Long employeeID) {
        StoreEmployee storeEmployee = storeEmployeeRepository.findByAccount_IdAndStatus(employeeID, Status.ACTIVE);
        if(storeEmployee != null){
            return "Không tìm thấy Nhân viên với ID là " + employeeID + " còn hoạt động trong cửa hàng.";
        }
        storeEmployee.setStatus(Status.INACTIVE);
        storeEmployeeRepository.save(storeEmployee);
        tblAccount account = storeEmployee.getAccount();
        account.setStatus(Status.ACTIVE);
        userRepository.save(account);
        return "Xóa thành công.";
    }

    @Override
    public List<ShowDistrictModel> getDistrictByProvinceID(String provinceID) {
        List<District> districtList = districtRepository.findByProvince_Id(provinceID);
        if(districtList == null){
            return null;
        }
        List<ShowDistrictModel> modelList = new ArrayList<>();
        for(District district : districtList) {
            ShowDistrictModel model = new ShowDistrictModel();
            model.setId(district.getId());
            model.setName(district.getDistrictName());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowProvinceModel> getAllProvince() {
        List<Province> provinceList = provinceRepository.findAll();
        if(provinceList == null){
            return null;
        }
        List<ShowProvinceModel> modelList = new ArrayList<>();
        for(Province province : provinceList) {
            ShowProvinceModel model = new ShowProvinceModel();
            model.setId(province.getId());
            model.setName(province.getProvinceName());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public List<ShowStorePlantRecordModel> getStorePlantRecordByStorePlantID(String storePlantID) {
        Optional<StorePlant> storePlant = storePlantRepository.findById(storePlantID);
        if(storePlant == null){
            return null;
        }
        List<ShowStorePlantRecordModel> listModel = new ArrayList<>();
        for(StorePlantRecord record : storePlant.get().getRecordList()) {
            ShowStorePlantRecordModel model = new ShowStorePlantRecordModel();
            model.setId(record.getId());
            model.setAmount(record.getAmount());
            model.setImportDate(record.getImportDate());
            model.setReason(record.getReason());
            listModel.add(model);
        }
        return listModel;
    }

    @Override
    public List<ShowStaffModel> getStaffByStoreID(String storeID, Pageable pageable) {
        Page<StoreEmployee> pagingResult = storeEmployeePagingRepository.findByStore_IdAndStore_Status(storeID, Status.ONSALE, pageable);
        return util.staffPagingConverter(pagingResult,pageable);
    }
}
