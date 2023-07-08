package com.example.thanhhoa.services.servicePack;

import com.example.thanhhoa.dtos.ServicePackModels.ShowServicePackModel;

import java.util.List;

public interface ServicePackService {

    String create(String range, Integer percentage);

    String delete(String id);

    List<ShowServicePackModel> getAll();
}
