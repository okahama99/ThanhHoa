package com.example.thanhhoa.services.servicePack;

import com.example.thanhhoa.dtos.ServicePackModels.ShowServicePackModel;
import com.example.thanhhoa.enums.Status;

import java.util.List;

public interface ServicePackService {

    String create(String range, String unit, Integer percentage, Status status);

    String delete(String id);

    String updateStatus(String id);

    List<ShowServicePackModel> getAll();

    List<ShowServicePackModel> getAllForOwner();


}
