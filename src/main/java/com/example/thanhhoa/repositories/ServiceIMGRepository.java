package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServiceIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceIMGRepository extends JpaRepository<ServiceIMG, String> {
    ServiceIMG findByImgURL(String url);
}
