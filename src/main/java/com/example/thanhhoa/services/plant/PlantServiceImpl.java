package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.example.thanhhoa.services.firebaseIMG.ImageService;
import com.example.thanhhoa.utils.Util;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private Util util;

    @Override
    public List<ShowPlantModel> getAllPlant(Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findAllByStatus(Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public ShowPlantModel getPlantByID(Long plantID) {
        Optional<Plant> checkExistedPlant = plantRepository.findById(plantID);
        if (checkExistedPlant == null) {
            return null;
        }
        Plant plant = checkExistedPlant.get();
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
        return model;
    }

    @Override
    public Boolean createPlant(CreatePlantModel createPlantModel) throws Exception {
        Plant plant = new Plant();

        plant.setName(createPlantModel.getName());
        plant.setDescription(createPlantModel.getDescription());
        plant.setHeight(createPlantModel.getHeight());
        plant.setPrice(createPlantModel.getPrice());
        plant.setCareNote(createPlantModel.getCareNote());
        plant.setWithPot(createPlantModel.getWithPot());
        plant.setStatus(Status.ONSELL);
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

        for (MultipartFile file : createPlantModel.getFiles()) {
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
    public Boolean updatePlant(UpdatePlantModel updatePlantModel, List<MultipartFile> files) throws Exception {
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
    public Boolean deletePlant(Long plantID) throws IOException {
        Optional<Plant> checkingPlant = plantRepository.findById(plantID);
        if (checkingPlant != null) {
            checkingPlant.get().setStatus(Status.INACTIVE);
            plantRepository.save(checkingPlant.get());

            List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_Id(plantID);
            if (plantCategoryList != null) {
                for (PlantCategory plantCategory : plantCategoryList) {
                    plantCategoryRepository.delete(plantCategory);
                }
            }

            List<StorePlant> storePlantList = storePlantRepository.findByPlantId(plantID);
            if (storePlantList != null) {
                for (StorePlant storePlant : storePlantList) {
                    storePlantRepository.delete(storePlant);
                }
            }

            List<PlantIMG> plantIMGList = plantIMGRepository.findByPlantId(plantID);
            if (plantIMGList != null) {
                for (PlantIMG plantIMG : plantIMGList) {
                    imageService.delete(plantIMG.getImgURL());
                    plantIMGRepository.delete(plantIMG);
                }
            }
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
    public List<ShowPlantModel> getPlantByCategory(Long categoryID, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> plantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            plantList.add(plantCategory.getPlant());
        }

        List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

        Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByName(String name, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByNameAndStatus(name, Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceMin(Double minPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceGreaterThan(minPrice, Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceMax(Double maxPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceLessThan(maxPrice, Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceInRange(Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceBetweenAndStatus(fromPrice, toPrice, Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndName(Long categoryID, String name, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByNameContainingAndStatus(name, Status.ONSELL);
        if (plantList != null) {
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            return util.plantPagingConverter(pagingResult, paging);
        }
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPriceBetweenAndNameAndStatus(fromPrice, toPrice, name, Status.ONSELL, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndPrice(Long categoryID, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPriceBetweenAndStatus(fromPrice, toPrice, Status.ONSELL);
        if (plantList != null) {
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            return util.plantPagingConverter(pagingResult, paging);
        }
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(Long categoryID, String name, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPriceBetweenAndNameAndStatus(fromPrice, toPrice, name, Status.ONSELL);
        if (plantList != null) {
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            return util.plantPagingConverter(pagingResult, paging);
        }
        return null;
    }
}
