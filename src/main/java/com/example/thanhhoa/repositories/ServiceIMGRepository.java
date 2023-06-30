package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.ServiceIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceIMGRepository extends JpaRepository<ServiceIMG, String> {
    ServiceIMG findByImgURL(String url);

    List<ServiceIMG> findByService_Id(String serviceID);


}
