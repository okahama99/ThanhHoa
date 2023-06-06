package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.dtos.OrderFeedbackModels.ShowOrderFeedback;
import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.OrderFeedbackIMGRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.example.thanhhoa.services.firebaseIMG.ImageService;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    private PlantPagingRepository plantPagingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public List<ShowPlantModel> getAllPlant(Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findAllByStatus(Status.ACTIVE, paging);
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowPlantCategory> categoryList = new ArrayList<>();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    if (plantCategoryList != null) {
                        for (PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            categoryList.add(showPlantCategory);
                        }
                    }

                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
    public Boolean createPlant(CreatePlantModel createPlantModel, MultipartFile[] files) throws Exception {
        Plant plant = new Plant();

        plant.setName(createPlantModel.getName());
        plant.setDescription(createPlantModel.getDescription());
        plant.setHeight(createPlantModel.getHeight());
        plant.setPrice(createPlantModel.getPrice());
        plant.setCareNote(createPlantModel.getCareNote());
        plant.setWithPot(createPlantModel.getWithPot());
        plant.setStatus(Status.ACTIVE);
        Plant plantWithID = plantRepository.saveAndFlush(plant);

        if (createPlantModel.getCategoryIDList() == null) {
            return false;
        }

        PlantCategory plantCategory = new PlantCategory();
        for (Long categoryID : createPlantModel.getCategoryIDList()) {
            Optional<Category> category = categoryRepository.findById(categoryID);
            if (category != null) {
                plantCategory.setCategory(category.get());
                plantCategory.setPlant(plantWithID);
                plantCategoryRepository.save(plantCategory);
            }
        }

        for (MultipartFile file : files) {
            String fileName = imageService.save(file);
            String imgName = imageService.getImageUrl(fileName);
            PlantIMG plantIMG = new PlantIMG();
            plantIMG.setPlant(plantWithID);
            plantIMG.setImgURL(imgName);
            plantIMGRepository.save(plantIMG);
        }

        StorePlant storePlant = new StorePlant();
        storePlant.setPlant(plantWithID);
        storePlant.setQuantity(createPlantModel.getQuantity());
        storePlantRepository.save(storePlant);

        PlantShipPrice plantShipPrice = new PlantShipPrice();
        plantShipPrice.setId(createPlantModel.getShipPriceID());
        plantShipPriceRepository.save(plantShipPrice);
        return true;
    }

    @Override
    public Boolean updatePlant(UpdatePlantModel updatePlantModel, MultipartFile[] files) throws Exception {
        Optional<Plant> checkPlant = plantRepository.findById(updatePlantModel.getPlantID());
        if (checkPlant != null) {
            Plant plant = checkPlant.get();
            plant.setName(updatePlantModel.getName());
            plant.setDescription(updatePlantModel.getDescription());
            plant.setHeight(updatePlantModel.getHeight());
            plant.setPrice(updatePlantModel.getPrice());
            plant.setCareNote(updatePlantModel.getCareNote());
            plant.setWithPot(updatePlantModel.getWithPot());
            plantRepository.save(plant);

            if (updatePlantModel.getCategoryIDList() == null) {
                return false;
            }
            // Tim category tu list categoryID roi add vao 1 list
            List<Category> categoryList = new ArrayList<>();
            for (Long categoryID : updatePlantModel.getCategoryIDList()) {
                Optional<Category> category = categoryRepository.findById(categoryID);
                if (category != null) {
                    categoryList.add(category.get());
                }
            }
            // Lay het toan bo relationship dang ton tai cua plant
            if (categoryList != null) {
                List<PlantCategory> plantCategoryList = plantCategoryRepository.findByPlant(plant);
                List<Category> plantCate = new ArrayList<>();
                // Luu lai cac category dang ton tai roi xoa relationship
                for (PlantCategory plantCategory : plantCategoryList) {
                    plantCate.add(plantCategory.getCategory());
                    plantCategoryRepository.delete(plantCategory);
                }
                // Tao 1 list tam thoi
                List<Category> copyOfPlantCate = new ArrayList<>(plantCate);

                // Lay ra cac phan tu trung nhau giua list duoc input va list tu database
                copyOfPlantCate.retainAll(categoryList);

                // Xoa het cac phan tu trung nhau tu list tu database
                plantCate.removeAll(copyOfPlantCate);

                // Xoa het cac phan tu trung nhau tu list duoc input
                categoryList.removeAll(copyOfPlantCate);

                // List tu database bay gio chi con cac phan tu khong trung nhau
                // Them cac phan tu tu list duoc input vao list tu database
                plantCate.addAll(categoryList);

                // Them cac phan tu trung nhau da duoc lay ra tu truoc do
                plantCate.addAll(copyOfPlantCate);

                // Tao ra cac relationship tu list da duoc chinh sua
                for (Category category : plantCate) {
                    PlantCategory plantCategory = new PlantCategory();
                    plantCategory.setPlant(plant);
                    plantCategory.setCategory(category);
                    plantCategoryRepository.save(plantCategory);
                }
            }

            for (PlantIMG image : plant.getPlantIMGList()) {
                String imgNameString = image.getImgURL();
                PlantIMG plantImage = plantIMGRepository.findByImgURL(imgNameString);
                plantImage.setPlant(null);
                plantImage.setImgURL(null);
                plantIMGRepository.save(plantImage);
                String[] strArr;
                strArr = imgNameString.split("[/;?]");
                imageService.delete(strArr[7]);
            }
            for (MultipartFile file : files) {
                String fileName = imageService.save(file);
                String imgName = imageService.getImageUrl(fileName);
                PlantIMG plantIMG = new PlantIMG();
                plantIMG.setPlant(plant);
                plantIMG.setImgURL(imgName);
                plantIMGRepository.save(plantIMG);
            }

            Optional<Store> store = storeRepository.findById(updatePlantModel.getStoreID());
            StorePlant storePlant = storePlantRepository.findByPlantAndStore(plant, store.get());
            storePlant.setQuantity(updatePlantModel.getQuantity());
            storePlantRepository.save(storePlant);

            Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findById(updatePlantModel.getShipPriceID());
            plantShipPrice.get().setId(updatePlantModel.getShipPriceID());
            plantShipPriceRepository.save(plantShipPrice.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean deletePlant(Long plantID) {
        Optional<Plant> checkingPlant = plantRepository.findById(plantID);
        if (checkingPlant != null) {
            checkingPlant.get().setStatus(Status.INACTIVE);
            plantRepository.save(checkingPlant.get());
            return true;
        }
        return false;
    }

    @Override
    public Plant checkDuplicate(String plantName) {
        Plant checkingPlant = plantRepository.findByName(plantName);
        return checkingPlant;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategory(List<Long> categoryIDList, Pageable paging) {
        List<PlantCategory> plantCategoryList = new ArrayList<>();
        for (Long categoryID : categoryIDList) {
            List<PlantCategory> tmpPlantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);
            plantCategoryList.addAll(tmpPlantCategoryList);
        }

        List<Plant> plantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            plantList.add(plantCategory.getPlant());
        }

        List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

        Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
        double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
        Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
            @Override
            protected ShowPlantModel doForward(Plant plant) {
                List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                if (orderFeedbackList != null) {
                    for (OrderFeedback orderFeedback : orderFeedbackList) {
                        ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                        List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                        if (orderFeedbackIMGList != null) {
                            showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                        }

                        showOrderFeedback.setRatingID(orderFeedback.getId());
                        showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
                model.setPlantCategoryList(null);
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
    }

    @Override
    public List<ShowPlantModel> getPlantByName(String name, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByNameAndStatus(name, Status.ACTIVE, paging);
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowPlantCategory> categoryList = new ArrayList<>();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    if (plantCategoryList != null) {
                        for (PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            categoryList.add(showPlantCategory);
                        }
                    }

                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
    public List<ShowPlantModel> getNameByPrice(Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceBetweenAndStatus(fromPrice, toPrice, Status.ACTIVE, paging);
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowPlantCategory> categoryList = new ArrayList<>();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    if (plantCategoryList != null) {
                        for (PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            categoryList.add(showPlantCategory);
                        }
                    }

                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
    public List<ShowPlantModel> getPlantByCategoryAndName(List<Long> categoryIDList, String name, Pageable paging) {
        List<PlantCategory> plantCategoryList = new ArrayList<>();
        for (Long categoryID : categoryIDList) {
            List<PlantCategory> tmpPlantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);
            plantCategoryList.addAll(tmpPlantCategoryList);
        }
        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByNameLikeAndStatus(name, Status.ACTIVE);
        if(plantList!=null){
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
                    model.setPlantCategoryList(null);
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
        }
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceBetweenAndNameAndStatus(fromPrice, toPrice, name, Status.ACTIVE, paging);
        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowPlantCategory> categoryList = new ArrayList<>();
                    List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plant.getId());
                    if (plantCategoryList != null) {
                        for (PlantCategory plantCategory : plantCategoryList) {
                            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
                            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
                            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
                            categoryList.add(showPlantCategory);
                        }
                    }

                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
    public List<ShowPlantModel> getPlantByCategoryAndPrice(List<Long> categoryIDList, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = new ArrayList<>();
        for (Long categoryID : categoryIDList) {
            List<PlantCategory> tmpPlantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);
            plantCategoryList.addAll(tmpPlantCategoryList);
        }
        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPriceBetweenAndStatus(fromPrice, toPrice, Status.ACTIVE);
        if(plantList!=null){
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
                    model.setPlantCategoryList(null);
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
        }
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(List<Long> categoryIDList, String name, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = new ArrayList<>();
        for (Long categoryID : categoryIDList) {
            List<PlantCategory> tmpPlantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);
            plantCategoryList.addAll(tmpPlantCategoryList);
        }
        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPriceBetweenAndNameAndStatus(fromPrice, toPrice, name, Status.ACTIVE);
        if(plantList!=null){
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / paging.getPageSize());
            Page<ShowPlantModel> modelResult = pagingResult.map(new Converter<>() {
                @Override
                protected ShowPlantModel doForward(Plant plant) {
                    List<ShowOrderFeedback> showOrderFeedbackList = new ArrayList<>();
                    List<OrderFeedback> orderFeedbackList = orderFeedbackRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if (orderFeedbackList != null) {
                        for (OrderFeedback orderFeedback : orderFeedbackList) {
                            ShowOrderFeedback showOrderFeedback = new ShowOrderFeedback();

                            List<OrderFeedbackIMG> orderFeedbackIMGList = orderFeedbackIMGRepository.findAllByOrderFeedback(orderFeedback);
                            if (orderFeedbackIMGList != null) {
                                showOrderFeedback.setOrderFeedbackIMGList(orderFeedbackIMGList);
                            }

                            showOrderFeedback.setRatingID(orderFeedback.getId());
                            showOrderFeedback.setRatingDes(orderFeedback.getDescription());
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
                    model.setPlantCategoryList(null);
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
        }
        return null;
    }
}
