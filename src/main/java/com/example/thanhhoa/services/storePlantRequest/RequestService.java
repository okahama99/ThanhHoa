//package com.example.thanhhoa.services.storePlantRequest;
//
//import com.example.thanhhoa.dtos.StorePlantRequestModels.CreateRequestModel;
//import com.example.thanhhoa.dtos.StorePlantRequestModels.ShowRequestModel;
//import com.example.thanhhoa.dtos.StorePlantRequestModels.UpdateRequestModel;
//import com.example.thanhhoa.enums.Status;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//public interface RequestService {
//
//    String create(CreateRequestModel createRequestModel);
//
//    String update(UpdateRequestModel updateRequestModel);
//
//    String delete(String requestID, String reason);
//
//    String changeRequestStatus(String requestID, String reason, Status status);
//
//    List<ShowRequestModel> getByStoreIDFrom(String storeID, Pageable pageable);
//
//    List<ShowRequestModel> getByStoreIDTo(String storeID, Pageable pageable);
//
//    List<ShowRequestModel> getByManagerIDFrom(String storeID, Pageable pageable);
//
//    List<ShowRequestModel> getByManagerIDTo(String storeID, Pageable pageable);
//
//    List<ShowRequestModel> getAll(Pageable pageable);
//
//    List<ShowRequestModel> getWaitingRequestByStoreFrom(String storeID, Pageable pageable);
//
//    List<ShowRequestModel> getWaitingRequestByStoreTo(String storeID, Pageable pageable);
//}
