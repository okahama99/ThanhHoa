package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.constants.Status;
import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.OrderFeedbackModels.ShowOrderFeedback;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.repositories.OrderFeedbackIMGRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.RatingRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private PlantIMGRepository plantIMGRepository;
    @Autowired
    private OrderFeedbackRepository orderFeedbackRepository;
    @Autowired
    private OrderFeedbackIMGRepository orderFeedbackIMGRepository;
    @Autowired
    private PlantShipPriceRepository plantShipPriceRepository;
    @Autowired
    private PlantCategoryRepository plantCategoryRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PlantPagingRepository plantPagingRepository;

    @Override
    public List<ShowPlantModel> getAllPlant(Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findAllByStatus(Status.ACTIVE.toString(), paging);
        if (pagingResult.hasContent()) {
            double totalPage = pagingResult.getTotalPages();
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowPlantCategory> categoryList = new ArrayList<>();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    if (!plantCategoryList.isEmpty()) {
                        for (PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            categoryList.add(showPlantCategory);
                        }
                    }

                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE.toString());
                    if (!orderFeedbackList.isEmpty()) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (!orderFeedbackIMGList.isEmpty()) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setOrderFeedbackID(orderFeedback.getId());
                            showOrderFeedback.setDescription(orderFeedback.getDescription());
                            showOrderFeedback.setCreatedDate(orderFeedback.getCreatedDate());

                            showOrderFeedbackList.add(showOrderFeedback);
                        }
                    }

                    ShowPlantModel model = new ShowPlantModel();
                    model.setPlantID(plant.getId());
                    model.setName(plant.getName());
                    model.setHeight(plant.getHeight());
                    model.setPrice(plant.getPrice());
                    model.setWithPot(plant.getWithPot());
                    model.setTotalFeedback(orderFeedbackList.size());
                    model.setPlantCategoryList(categoryList);
                    model.setOrderFeedbackList(showOrderFeedbackList);
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

    @Override
    public Plant createPlant(CreatePlantModel createPlantModel, MultipartFile[] files) throws Exception {
        Plant plant = new Plant();
        plant.setName(createPlantModel.getName());
        plant.setDescription(createPlantModel.getDescription());
        plant.setHeight(createPlantModel.getHeight());
        plant.setPrice(createPlantModel.getPrice());
        plant.setCareNote(createPlantModel.getCareNote());
        plant.setWithPot(createPlantModel.getWithPot());
        plant.setStatus(Status.ACTIVE);

//        for (MultipartFile file : files) {
//            String fileName = imageService.save(file);
//            String imageUrl = imageService.getImageUrl(fileName);
//            PlantImage plantImage = new PlantImage();
//            plantImage.setPlant(plant);
//            plantImage.setImageName(imageUrl);
//            plantImageRepository.saveAndFlush(plantImage);
//        }
        plantRepository.saveAndFlush(plant);
        return plant;
    }

    @Override
    public Boolean updatePlant(UpdatePlantModel updatePlantModel, MultipartFile[] files) throws Exception {
        return null;
    }

    @Override
    public Boolean deletePlant(Long plantID) {
        return null;
    }

    @Override
    public Plant checkDuplicate(String plantName) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategory(List<Category> categoryList, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByName(String name, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getNameByPrice(Double fromPrice, Double toPrice, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndName(List<Category> categoryList, String name, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndPrice(List<Category> categoryList, Double fromPrice, Double toPrice, Pageable paging) {
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(List<Category> categoryList, String name, Double fromPrice, Double toPrice, Pageable paging) {
        return null;
    }
}
