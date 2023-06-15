package com.example.thanhhoa.utils;

import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class Util {

    @Autowired
    private PlantCategoryRepository plantCategoryRepository;
    /**
     * Small Util to return {@link Pageable} to replace dup code in serviceImpl
     */
    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean sortTypeAsc) {
        if (sortTypeAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

    public List<ShowUserModel> userPagingConverter(Page<tblAccount> pagingResult, Pageable paging){
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowUserModel> modelResult = pagingResult.map(new Converter<tblAccount, ShowUserModel>() {
                @Override
                protected ShowUserModel doForward(tblAccount user) {
                    ShowUserModel model = new ShowUserModel();
                    model.setUserName(user.getUsername());
                    model.setFullName(user.getFullName());
                    model.setEmail(user.getEmail());
                    model.setPhone(user.getPhone());
                    model.setAvatar(user.getAvatar());
                    model.setCreatedDate(user.getCreatedDate());
                    model.setAddress(user.getAddress());
                    model.setGender(user.getGender());
                    model.setRoleID(user.getRole().getId());
                    model.setRoleName(user.getRole().getRoleName());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected tblAccount doBackward(ShowUserModel ShowUserModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowPlantModel> plantPagingConverter(Page<Plant> pagingResult, Pageable paging){
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<Plant, ShowPlantModel>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
                    ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                    for (PlantCategory plantCategory : plantCategoryList) {
                        showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                        showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                        showPlantCategoryList.add(showPlantCategory);
                    }
                    ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
                    showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
                    showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
                    showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

                    ShowPlantModel model = new ShowPlantModel();
                    model.setPlantID(plant.getId());
                    model.setName(plant.getName());
                    model.setHeight(plant.getHeight());
                    model.setPrice(plant.getPrice());
                    model.setWithPot(plant.getWithPot());
                    model.setShowPlantShipPriceModel(showPlantShipPriceModel);
                    model.setPlantCategoryList(showPlantCategoryList);
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Plant doBackward(ShowPlantModel showPlantModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

}
