package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePagingRepository extends PagingAndSortingRepository<Service, String> {
    Page<Service> findAllByStatus(Status status);
}
