package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository  extends JpaRepository<Rating, Long> {
}
