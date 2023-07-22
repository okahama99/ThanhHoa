package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findById(String categoryID);

    Category findByName(String name);

    Category findFirstByOrderByIdDesc();
}
