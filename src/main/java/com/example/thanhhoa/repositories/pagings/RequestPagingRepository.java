//package com.example.thanhhoa.repositories.pagings;
//
//import com.example.thanhhoa.entities.StorePlantRequest;
//import com.example.thanhhoa.enums.Status;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface RequestPagingRepository extends PagingAndSortingRepository<StorePlantRequest, String> {
//    Page<StorePlantRequest> findAllByFromStore_Id(String storeID, Pageable pageable);
//
//    Page<StorePlantRequest> findAllByToStore_Id(String storeID, Pageable pageable);
//
//    Page<StorePlantRequest> findAllByFromStore_IdAndStatus(String storeID, Status status, Pageable pageable);
//
//    Page<StorePlantRequest> findAllByToStore_IdAndStatus(String storeID, Status status, Pageable pageable);
//
//    Page<StorePlantRequest> findAllByFromManager_IdAndStatus(Long userID, Status status, Pageable pageable);
//
//    Page<StorePlantRequest> findAllByToManager_IdAndStatus(Long userID, Status status, Pageable pageable);
//}
