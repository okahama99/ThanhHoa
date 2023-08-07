package com.example.thanhhoa.services.storePlantRequest;

import com.example.thanhhoa.dtos.StorePlantRequestModels.CreateRequestModel;
import com.example.thanhhoa.dtos.StorePlantRequestModels.ShowRequestModel;
import com.example.thanhhoa.dtos.StorePlantRequestModels.UpdateRequestModel;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.entities.StorePlantRecord;
import com.example.thanhhoa.entities.StorePlantRequest;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.RequestRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.pagings.RequestPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private Util util;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private StorePlantRecordRepository storePlantRecordRepository;
    @Autowired
    private RequestPagingRepository requestPagingRepository;

    @Override
    public String create(CreateRequestModel createRequestModel) {
        Store store = storeRepository.findByIdAndStatus(createRequestModel.getToStoreID(), Status.ACTIVE);
        if(store == null) {
            return "Không tìm thấy Store với ID là " + createRequestModel.getToStoreID() + ".";
        }
        StoreEmployee manager = storeEmployeeRepository.findByAccount_IdAndStatus(createRequestModel.getManagerID(), Status.ACTIVE);
        if(manager == null){
            return "Không tìm thấy Manager với ID là " + createRequestModel.getManagerID() + ".";
        }
        StoreEmployee toManager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
        if(toManager == null){
            return "Không tìm thấy Manager với StoreID là " + store.getId() + ".";
        }
        Plant plant = plantRepository.findByIdAndStatus(createRequestModel.getPlantID(), Status.ONSALE);
        if(plant == null){
            return "Không tìm thấy Plant với ID là " + createRequestModel.getPlantID() + ".";
        }

        StorePlantRequest request = new StorePlantRequest();
        StorePlantRequest lastStorePlantRequest = requestRepository.findFirstByOrderByIdDesc();
        if(lastStorePlantRequest == null) {
            request.setId(util.createNewID("RQ"));
        } else {
            request.setId(util.createIDFromLastID("RQ", 2, lastStorePlantRequest.getId()));
        }
        request.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        request.setReason(createRequestModel.getReason());
        request.setQuantity(createRequestModel.getQuantity());
        request.setStatus(Status.WAITING);
        request.setFromStore(manager.getStore());
        request.setFromManager(manager);
        request.setToStore(store);
        request.setToManager(toManager);
        request.setPlant(plant);
        requestRepository.save(request);
        return "Tạo thành công.";
    }

    @Override
    public String update(UpdateRequestModel updateRequestModel) {
        StorePlantRequest request = requestRepository.findByIdAndStatus(updateRequestModel.getRequestID(), Status.WAITING);
        if(request == null) {
            return "Không tìm thấy Request có Status WAITING với ID là " + updateRequestModel.getRequestID() + ".";
        }
        Store store = storeRepository.findByIdAndStatus(updateRequestModel.getToStoreID(), Status.ACTIVE);
        if(store == null) {
            return "Không tìm thấy Store với ID là " + updateRequestModel.getToStoreID() + ".";
        }
        StoreEmployee manager = storeEmployeeRepository.findByAccount_IdAndStatus(updateRequestModel.getManagerID(), Status.ACTIVE);
        if(manager == null){
            return "Không tìm thấy Manager với ID là " + updateRequestModel.getManagerID() + ".";
        }
        StoreEmployee toManager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(store.getId(), "Manager");
        if(toManager == null){
            return "Không tìm thấy Manager với StoreID là " + store.getId() + ".";
        }
        Plant plant = plantRepository.findByIdAndStatus(updateRequestModel.getPlantID(), Status.ONSALE);
        if(plant == null){
            return "Không tìm thấy Plant với ID là " + updateRequestModel.getPlantID() + ".";
        }

        request.setReason(updateRequestModel.getReason());
        request.setQuantity(updateRequestModel.getQuantity());
        request.setFromStore(manager.getStore());
        request.setFromManager(manager);
        request.setToStore(store);
        request.setToManager(toManager);
        request.setPlant(plant);
        requestRepository.save(request);
        return "Cập nhật thành công.";
    }

    @Override
    public String delete(String requestID, String reason) {
        StorePlantRequest request = requestRepository.findByIdAndStatus(requestID, Status.WAITING);
        if(request == null) {
            return "Không tìm thấy Request có Status WAITING với ID là " + requestID + ".";
        }
        request.setReason(reason);
        request.setStatus(Status.INACTIVE);
        request.setUpdateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        requestRepository.save(request);
        return "Xóa thành công.";
    }

    @Override
    public String changeRequestStatus(String requestID, String reason, Status status) {
        StorePlantRequest request = requestRepository.findByIdAndStatus(requestID, Status.WAITING);
        if(request == null) {
            return "Không tìm thấy Request có Status WAITING với ID là " + requestID + ".";
        }

        if(status.toString().equalsIgnoreCase("APPROVED")){
            // From
            StorePlant fStorePlant = storePlantRepository.findByPlantIdAndStoreId(request.getPlant().getId(), request.getFromStore().getId());
            if(fStorePlant.getQuantity() < request.getQuantity()){
                return "Số lượng cây trong StoreID " + request.getFromStore().getId() + " ( " + fStorePlant.getQuantity() + " ) ít hơn số cây yêu cầu ( " + request.getQuantity() + " ).";
            }
            fStorePlant.setQuantity(fStorePlant.getQuantity() - request.getQuantity());

            StorePlantRecord fStorePlantRecord = new StorePlantRecord();
            StorePlantRecord fLastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
            if (fLastRecord == null) {
                fStorePlantRecord.setId(util.createNewID("SPR"));
            } else {
                fStorePlantRecord.setId(util.createIDFromLastID("SPR",3,fLastRecord.getId()));
            }
            fStorePlantRecord.setAmount(request.getQuantity());
            fStorePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            fStorePlantRecord.setStorePlant(fStorePlant);
            fStorePlantRecord.setReason("ManagerID : " + request.getFromManager().getId() + ", lí do :" + reason);

            // To
            StorePlant tStorePlant = storePlantRepository.findByPlantIdAndStoreId(request.getPlant().getId(), request.getToStore().getId());
            tStorePlant.setQuantity(tStorePlant.getQuantity() + request.getQuantity());

            StorePlantRecord tStorePlantRecord = new StorePlantRecord();
            StorePlantRecord tLastRecord = storePlantRecordRepository.findFirstByOrderByIdDesc();
            if (tLastRecord == null) {
                tStorePlantRecord.setId(util.createNewID("SPR"));
            } else {
                tStorePlantRecord.setId(util.createIDFromLastID("SPR",3,tLastRecord.getId()));
            }
            tStorePlantRecord.setAmount(request.getQuantity());
            tStorePlantRecord.setImportDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            tStorePlantRecord.setStorePlant(tStorePlant);
            tStorePlantRecord.setReason("ManagerID : " + request.getToManager().getId() + ", lí do :" + reason);

            storePlantRecordRepository.save(fStorePlantRecord);
            storePlantRecordRepository.save(tStorePlantRecord);
            storePlantRepository.save(fStorePlant);
            storePlantRepository.save(tStorePlant);
        }
        request.setReason(reason);
        request.setStatus(status);
        request.setUpdateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        requestRepository.save(request);
        return "Cập nhật thành công.";
    }

    @Override
    public List<ShowRequestModel> getByStoreIDFrom(String storeID, Pageable pageable) {
        Page<StorePlantRequest> pagingResult = requestPagingRepository.findAllByFromStore_Id(storeID, pageable);
        return util.requestPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowRequestModel> getByStoreIDTo(String storeID, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowRequestModel> getByManagerIDFrom(String storeID, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowRequestModel> getByManagerIDTo(String storeID, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowRequestModel> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowRequestModel> getWaitingRequestByStoreFrom(String storeID, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowRequestModel> getWaitingRequestByStoreTo(String storeID, Pageable pageable) {
        return null;
    }
}
