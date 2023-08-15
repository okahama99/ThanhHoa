package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypePagingRepository extends PagingAndSortingRepository<ServiceType, String> {
    Page<ServiceType> findByService_Id(String serviceID, Pageable pageable);
}
