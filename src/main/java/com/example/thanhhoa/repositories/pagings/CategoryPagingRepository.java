package com.example.thanhhoa.repositories.pagings;

import com.example.thanhhoa.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPagingRepository  extends PagingAndSortingRepository<Category, String> {
    Page<Category> findAll(Pageable paging);
}
