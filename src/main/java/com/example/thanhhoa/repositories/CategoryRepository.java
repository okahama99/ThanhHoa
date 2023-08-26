package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findById(String categoryID);

    Optional<Category> findByIdAndStatus(String categoryID, Status status);

    Category findByName(String name);

    Category findFirstByOrderByIdDesc();
}
