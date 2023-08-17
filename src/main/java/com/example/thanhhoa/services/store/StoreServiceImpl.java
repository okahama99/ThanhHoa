package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantIMGModel;
import com.example.thanhhoa.dtos.PlantModels.UpdateStorePlantModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.CreateStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowDistrictModel;
import com.example.thanhhoa.dtos.StoreModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.ShowProvinceModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantRecordModel;
import com.example.thanhhoa.dtos.StoreModels.UpdateStoreModel;
import com.example.thanhhoa.entities.District;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.Province;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StoreEmployeeId;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.DistrictRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.ProvinceRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.StoreEmployeePagingRepository;
import com.example.thanhhoa.repositories.pagings.StorePlantPagingRepository;
import com.example.thanhhoa.repositories.pagings.StorePlantRecordPagingRepository;
import com.example.thanhhoa.utils.Util;
import com.google.common.base.Converter;
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
    @Autowired
    private StorePlantRecordPagingRepository storePlantRecordPagingRepository;
    @Autowired
    private PlantIMGRepository plantIMGRepository;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private PlantCategoryRepository plantCategoryRepository;

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
    public String changeStoreStatus(String storeID, Status status) {
        Optional<Store> checkExisted = storeRepository.findById(storeID);
        if(checkExisted == null){
            return "Không tìm thấy Cửa hàng với tên là " + storeID + ".";
        }
        Store store = checkExisted.get();
        store.setStatus(status);
        storeRepository.save(store);
        return "Cập nhật thành công.";
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
        if(manager != null){
            model.setManagerID(manager.getAccount().getId());
            model.setManagerName(manager.getAccount().getFullName());
        }
        model.setId(store.getId());
        model.setStatus(store.getStatus());
        model.setStoreName(store.getStoreName());
        model.setAddress(store.getAddress());
        model.setDistrictID(store.getDistrict().getId());
        model.setDistrictName(store.getDistrict().getDistrictName());
        model.setPhone(store.getPhone());
        return model;
    }

    @Override
    public String addStorePlant(List<AddStorePlantModel> listModel) {
        for( AddStorePlantModel addStorePlantModel : listModel) {
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

                storePlantRepository.save(newPlant);
                storePlantRecordRepository.save(storePlantRecord);
                storePlantRepository.save(newPlant);
                return "Thêm thành công.";
            }
            storePlant.setQuantity(storePlant.getQuantity() + addStorePlantModel.getQuantity());

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

            storePlantRepository.save(storePlant);
            storePlantRecordRepository.save(storePlantRecord);
        }
        return "Thêm thành công.";
    }

    @Override
    public String updateStorePlant(UpdateStorePlantModel updateStorePlantModel, Long userID) throws Exception {
        Optional<StorePlant> checkExisted = storePlantRepository.findById(updateStorePlantModel.getStorePlantID());
        if(checkExisted == null){
            return "Không tìm thấy StorePlant với ID là " + updateStorePlantModel.getStorePlantID() + ".";
        }
        StorePlant storePlant = checkExisted.get();
        storePlant.setQuantity(storePlant.getQuantity() + updateStorePlantModel.getQuantity());

        StorePlantRecord storePlantRecord = new StorePlantRecord();
        StorePlantRecord lastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
        if (lastRecord == null) {
            storePlantRecord.setId(util.createNewID("SPR"));
        } else {
            storePlantRecord.setId(util.createIDFromLastID("SPR",3,lastRecord.getId()));
        }
        storePlantRecord.setAmount(updateStorePlantModel.getQuantity());
        storePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        storePlantRecord.setStorePlant(storePlant);
        storePlantRecord.setReason("ManagerID : " + userID + ", lí do :" + updateStorePlantModel.getReason());

        storePlantRepository.save(storePlant);
        storePlantRecordRepository.save(storePlantRecord);
        return "Cập nhật thành công.";
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
        List<Store> getListStore = storeRepository.findAllByOrderByIdDesc();
        List<ShowStoreModel> storeModelList = new ArrayList<>();
        for (Store store : getListStore) {
            StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
            ShowStoreModel model = new ShowStoreModel();
            model.setId(store.getId());
            model.setStatus(store.getStatus());
            model.setStoreName(store.getStoreName());
            model.setAddress(store.getAddress());
            model.setDistrictID(store.getDistrict().getId());
            model.setDistrictName(store.getDistrict().getDistrictName());
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

            StoreEmployee storeEmployee = storeEmployeeRepository.findByAccount_Id(employeeUserID);
            if(storeEmployee != null && storeEmployee.getStatus().toString().equalsIgnoreCase("INACTIVE")){
                storeEmployee.setStatus(Status.ACTIVE);
                storeEmployeeRepository.save(storeEmployee);
            }else if(storeEmployee != null && storeEmployee.getStatus().toString().equalsIgnoreCase("ACTIVE")){
                return "Tài khoản có ID là " + employeeUserID + "vẫn đang là nhân viên của cửa hàng.";
            }else{
                StoreEmployeeId employeeId = new StoreEmployeeId();
                employeeId.setTblAccount_id(employeeAcc.getId());

                StoreEmployee newEmployee = new StoreEmployee();
                newEmployee.setId(employeeId);
                newEmployee.setStore(store);
                newEmployee.setAccount(employeeAcc);
                newEmployee.setStatus(Status.ACTIVE);
                storeEmployeeRepository.save(newEmployee);
            }

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
    public List<ShowStorePlantRecordModel> getStorePlantRecordByPlantIDAndStoreID(String plantID, String storeID, Pageable pageable) {
        StorePlant storePlant = storePlantRepository.findByPlantIdAndStoreId(plantID, storeID);
        if(storePlant == null){
            return null;
        }
        Page<StorePlantRecord> pagingResult = storePlantRecordPagingRepository.findByStorePlant_Id(storePlant.getId(), pageable);
        if(pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / pageable.getPageSize());
            Page<ShowStorePlantRecordModel> modelResult = pagingResult.map(new Converter<StorePlantRecord, ShowStorePlantRecordModel>() {
                @Override
                protected ShowStorePlantRecordModel doForward(StorePlantRecord storePlantRecord) {
                    ShowStorePlantRecordModel model = new ShowStorePlantRecordModel();
                    model.setId(storePlantRecord.getId());
                    model.setAmount(storePlantRecord.getAmount());
                    model.setImportDate(storePlantRecord.getImportDate());
                    model.setReason(storePlantRecord.getReason());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected StorePlantRecord doBackward(ShowStorePlantRecordModel showStorePlantRecordModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ShowStaffModel> getStaffByStoreID(String storeID, Pageable pageable) {
        Page<StoreEmployee> pagingResult = storeEmployeePagingRepository.findByStore_IdAndStore_Status(storeID, Status.ACTIVE, pageable);
        return util.staffPagingConverter(pagingResult,pageable);
    }

    @Override
    public ShowStoreModel getStoreByStaffToken(Long staffID){
        StoreEmployee employee = storeEmployeeRepository.findByAccount_IdAndStatus(staffID, Status.ACTIVE);
        if(employee == null){
            return null;
        }
        StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(employee.getStore().getId(), "Manager");
        ShowStoreModel model = new ShowStoreModel();
        if(manager != null){
            model.setManagerID(manager.getAccount().getId());
            model.setManagerName(manager.getAccount().getFullName());
        }
        model.setId(employee.getStore().getId());
        model.setStatus(employee.getStore().getStatus());
        model.setStoreName(employee.getStore().getStoreName());
        model.setAddress(employee.getStore().getAddress());
        model.setDistrictID(employee.getStore().getDistrict().getId());
        model.setDistrictName(employee.getStore().getDistrict().getDistrictName());
        model.setPhone(employee.getStore().getPhone());
        return model;
    }

    @Override
    public List<ShowPlantModel> getStorePlantByPlantIDAndQuantity(String plantID, Integer quantity) {
        List<StorePlant> storePlantList = storePlantRepository.findByPlantIdAndQuantityGreaterThanEqual(plantID, quantity);
        if(storePlantList == null || storePlantList.isEmpty()){
            return null;
        }
        List<ShowPlantModel> modelList = new ArrayList<>();
        for(StorePlant storePlant : storePlantList) {
            Plant plant = storePlant.getPlant();
            List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
            List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
            for(PlantCategory plantCategory : plantCategoryList) {
                ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                showPlantCategoryList.add(showPlantCategory);
            }

            List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
            List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getId());
            if(plantIMGList != null) {
                for(PlantIMG img : plantIMGList) {
                    ShowPlantIMGModel model = new ShowPlantIMGModel();
                    model.setId(img.getId());
                    model.setUrl(img.getImgURL());
                    showPlantIMGList.add(model);
                }
            }

            ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
            showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
            showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
            showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

            PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
            ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
            showPlantPriceModel.setId(newestPrice.getId());
            showPlantPriceModel.setPrice(newestPrice.getPrice());
            showPlantPriceModel.setApplyDate(newestPrice.getApplyDate());
            showPlantPriceModel.setStatus(newestPrice.getStatus());

            ShowStorePlantModel storePlantModel = new ShowStorePlantModel();
            storePlantModel.setId(storePlant.getId());
            storePlantModel.setQuantity(storePlant.getQuantity());

            com.example.thanhhoa.dtos.StoreModels.ShowPlantModel model = new com.example.thanhhoa.dtos.StoreModels.ShowPlantModel();
            model.setPlantID(plant.getId());
            model.setName(plant.getName());
            model.setHeight(plant.getHeight());
            model.setWithPot(plant.getWithPot());
            model.setShowPlantShipPriceModel(showPlantShipPriceModel);
            model.setPlantCategoryList(showPlantCategoryList);
            model.setShowPlantPriceModel(showPlantPriceModel);
            model.setPlantIMGList(showPlantIMGList);
            model.setDescription(plant.getDescription());
            model.setCareNote(plant.getCareNote());
            model.setStatus(plant.getStatus());
            model.setShowStorePlantModel(storePlantModel);
            modelList.add(model);
        }
        return modelList;
    }
}
