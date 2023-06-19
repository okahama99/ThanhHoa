package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(String categoryID);

    List<Category> findByNameContaining(String name);
}
