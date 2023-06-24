package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.StorePlantRecordRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.example.thanhhoa.services.firebaseIMG.ImageService;
import com.example.thanhhoa.utils.Util;
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
    private PlantShipPriceRepository plantShipPriceRepository;
    @Autowired
    private PlantPriceRepository plantPriceRepository;
    @Autowired
    private PlantCategoryRepository plantCategoryRepository;
    @Autowired
    private StorePlantRepository storePlantRepository;
    @Autowired
    private StorePlantRecordRepository storePlantRecordRepository;
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
        Page<Plant> pagingResult = plantPagingRepository.findAllByStatus(Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public ShowPlantModel getPlantByID(String plantID) {
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

        ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
        showPlantPriceModel.setId(plant.getPlantPrice().getId());
        showPlantPriceModel.setPrice(plant.getPlantPrice().getPrice());
        showPlantPriceModel.setApplyDate(plant.getPlantPrice().getApplyDate());

        ShowPlantModel model = new ShowPlantModel();
        model.setPlantID(plant.getId());
        model.setName(plant.getName());
        model.setHeight(plant.getHeight());
        model.setWithPot(plant.getWithPot());
        model.setShowPlantShipPriceModel(showPlantShipPriceModel);
        model.setPlantCategoryList(showPlantCategoryList);
        model.setShowPlantPriceModel(showPlantPriceModel);
        return model;
    }

    @Override
    public String createPlant(CreatePlantModel createPlantModel) throws Exception {
        Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findById(createPlantModel.getShipPriceID());
        if (plantShipPrice == null) {
            return "Không tìm thấy dữ liệu với ShipPriceID = " + createPlantModel.getShipPriceID() + ".";
        }

        Optional<PlantPrice> plantPrice = plantPriceRepository.findById(createPlantModel.getPlantPriceID());
        if (plantPrice == null) {
            return "Không tìm thấy dữ liệu với PlantPriceID = " + createPlantModel.getPlantPriceID() + ".";
        }

        if (createPlantModel.getCategoryIDList() == null) {
            return "Danh sách Category không được để trống.";
        }

        Plant plant = new Plant();
        Plant lastPlant = plantRepository.findFirstByOrderByIdDesc();
        if (lastPlant == null) {
            plant.setId(util.createNewID("SP"));
        } else {
            plant.setId(util.createIDFromLastID("SP", 2, lastPlant.getId()));
        }
        plant.setName(createPlantModel.getName());
        plant.setDescription(createPlantModel.getDescription());
        plant.setHeight(createPlantModel.getHeight());
        plant.setCareNote(createPlantModel.getCareNote());
        plant.setWithPot(createPlantModel.getWithPot());
        plant.setStatus(Status.ONSALE);
        plant.setPlantShipPrice(plantShipPrice.get());
        plant.setPlantPrice(plantPrice.get());
        Plant plantWithID = plantRepository.saveAndFlush(plant);

        PlantCategory plantCategory = new PlantCategory();
        for (String categoryID : createPlantModel.getCategoryIDList()) {
            Optional<Category> category = categoryRepository.findById(categoryID);
            if (category != null) {
                plantCategory.setCategory(category.get());
                plantCategory.setPlant(plantWithID);
                plantCategoryRepository.save(plantCategory);
            }

            return "Không tìm thấy Category với CategoryID = " + categoryID + ".";
        }

        if (createPlantModel.getFiles() != null) {
            for (MultipartFile file : createPlantModel.getFiles()) {
                String fileName = imageService.save(file);
                String imgName = imageService.getImageUrl(fileName);
                PlantIMG plantIMG = new PlantIMG();
                plantIMG.setPlant(plantWithID);
                plantIMG.setImgURL(imgName);
                plantIMGRepository.save(plantIMG);
            }
        }
        return "Tạo thành công.";
    }

    @Override
    public String updatePlant(UpdatePlantModel updatePlantModel, List<MultipartFile> files) throws Exception {
        Optional<Plant> checkPlant = plantRepository.findById(updatePlantModel.getPlantID());
        if (checkPlant != null) {
            Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findById(updatePlantModel.getShipPriceID());
            if (plantShipPrice == null) {
                return "Không tìm thấy dữ liệu với ShipPriceID = " + updatePlantModel.getShipPriceID() + ".";
            }

            Optional<PlantPrice> plantPrice = plantPriceRepository.findById(updatePlantModel.getPlantPriceID());
            if (plantPrice == null) {
                return "Không tìm thấy dữ liệu với PlantPriceID = " + updatePlantModel.getPlantPriceID() + ".";
            }

            Plant plant = checkPlant.get();
            plant.setName(updatePlantModel.getName());
            plant.setDescription(updatePlantModel.getDescription());
            plant.setHeight(updatePlantModel.getHeight());
            plant.setCareNote(updatePlantModel.getCareNote());
            plant.setWithPot(updatePlantModel.getWithPot());
            plant.setPlantShipPrice(plantShipPrice.get());
            plant.setPlantPrice(plantPrice.get());
            plantRepository.save(plant);

            if (updatePlantModel.getCategoryIDList() == null) {
                return "Danh sách Category không được để trống.";
            }
            // Tim category tu list categoryID roi add vao 1 list
            List<Category> categoryList = new ArrayList<>();
            for (String categoryID : updatePlantModel.getCategoryIDList()) {
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

            if (updatePlantModel.getFiles() != null) {
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
            }
            return "Cập nhật thành công.";
        }
        return "Cập nhật thất bại.";
    }

    @Override
    public Boolean deletePlant(String plantID) {
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
    public List<ShowPlantModel> getPlantByCategory(String categoryID, Pageable paging) {
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
        Page<Plant> pagingResult = plantPagingRepository.findByNameContainingAndStatus(name, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceMin(Double minPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPlantPrice_PriceGreaterThan(minPrice, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceMax(Double maxPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPlantPrice_PriceLessThan(maxPrice, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getNameByPriceInRange(Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPlantPrice_PriceBetweenAndStatus(fromPrice, toPrice, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndName(String categoryID, String name, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndPlant_NameContaining(categoryID, name);
        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }
        Page<Plant> pagingResult = new PageImpl<>(catePlantList);
        return util.plantPagingConverter(pagingResult, paging);
//        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);
//
//        List<Plant> catePlantList = new ArrayList<>();
//        for (PlantCategory plantCategory : plantCategoryList) {
//            catePlantList.add(plantCategory.getPlant());
//        }
//
//        List<Plant> plantList = plantRepository.findByNameContainingAndStatus(name, Status.ONSELL);
//        if (plantList != null) {
//            plantList.addAll(catePlantList);
//            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));
//
//            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
//            return util.plantPagingConverter(pagingResult, paging);
//        }
//        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByPlantPrice_PriceBetweenAndNameContainingAndStatus(fromPrice, toPrice, name, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndPrice(String categoryID, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPlantPrice_PriceBetweenAndStatus(fromPrice, toPrice, Status.ONSALE);
        if (plantList != null) {
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            return util.plantPagingConverter(pagingResult, paging);
        }
        return null;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(String categoryID, String name, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_Id(categoryID);

        List<Plant> catePlantList = new ArrayList<>();
        for (PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> plantList = plantRepository.findByPlantPrice_PriceBetweenAndNameAndStatus(fromPrice, toPrice, name, Status.ONSALE);
        if (plantList != null) {
            plantList.addAll(catePlantList);
            List<Plant> noDuplicatePlantList = new ArrayList<>(new HashSet<>(plantList));

            Page<Plant> pagingResult = new PageImpl<>(noDuplicatePlantList);
            return util.plantPagingConverter(pagingResult, paging);
        }
        return null;
    }
}
