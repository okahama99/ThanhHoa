package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.ServicePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePricePagingRepository extends PagingAndSortingRepository<ServicePrice, String> {
    Page<ServicePrice> findAll(Pageable pageable);

    Page<ServicePrice> findAllByService_Id(String serviceID, Pageable pageable);
}
