package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantIMGModel;
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
import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.OrderDetailRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.StorePlantRepository;
import com.example.thanhhoa.repositories.pagings.PlantCategoryPagingRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.example.thanhhoa.repositories.pagings.PlantPricePagingRepository;
import com.example.thanhhoa.services.firebaseIMG.FirebaseImageService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
    private PlantPagingRepository plantPagingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FirebaseImageService firebaseImageService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private PlantCategoryPagingRepository plantCategoryPagingRepository;
    @Autowired
    private PlantPricePagingRepository plantPricePagingRepository;
    @Autowired
    private Util util;
    @Autowired
    private StorePlantRepository storePlantRepository;

    @Override
    public List<ShowPlantModel> getAllPlant(Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findAllByStatus(Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getAllPlantWithInactive(Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findAll(paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantV2(String storeID, Pageable pageable) {
        Page<Plant> pagingResult = plantPagingRepository.findAll(pageable);
        return util.plantPagingConverterV2(storeID, pagingResult, pageable);
    }

    @Override
    public ShowPlantModel getPlantByID(String plantID) {
        Optional<Plant> checkExistedPlant = plantRepository.findById(plantID);
        if(checkExistedPlant == null) {
            return null;
        }
        Plant plant = checkExistedPlant.get();
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
        List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
        for(PlantCategory plantCategory : plantCategoryList) {
            ShowPlantCategory showPlantCategory = new ShowPlantCategory();
            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
            showPlantCategoryList.add(showPlantCategory);
        }

        List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
        List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getId());
        if(plantIMGList != null) {
            for(PlantIMG img : plantIMGList) {
                ShowPlantIMGModel model = new ShowPlantIMGModel();
                model.setId(img.getId());
                model.setUrl(img.getImgURL());
                showPlantIMGList.add(model);
            }
        }

        ShowPlantShipPriceModel showPlantShipPriceModel = new ShowPlantShipPriceModel();
        showPlantShipPriceModel.setId(plant.getPlantShipPrice().getId());
        showPlantShipPriceModel.setPotSize(plant.getPlantShipPrice().getPotSize());
        showPlantShipPriceModel.setPricePerPlant(plant.getPlantShipPrice().getPricePerPlant());

        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdAndStatusOrderByApplyDateDesc(plant.getId(), Status.ACTIVE);
        ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
        showPlantPriceModel.setId(newestPrice.getId());
        showPlantPriceModel.setPrice(newestPrice.getPrice());
        showPlantPriceModel.setApplyDate(newestPrice.getApplyDate());

        Integer totalPlant = storePlantRepository.sumQuantity(plant.getId());

        ShowPlantModel model = new ShowPlantModel();
        model.setPlantID(plant.getId());
        model.setName(plant.getName());
        model.setHeight(plant.getHeight());
        model.setWithPot(plant.getWithPot());
        model.setShowPlantShipPriceModel(showPlantShipPriceModel);
        model.setPlantCategoryList(showPlantCategoryList);
        model.setShowPlantPriceModel(showPlantPriceModel);
        model.setPlantIMGList(showPlantIMGList);
        model.setDescription(plant.getDescription());
        model.setCareNote(plant.getCareNote());
        model.setTotalPlant(totalPlant);
        model.setStatus(plant.getStatus());
        return model;
    }

    @Override
    public String createPlant(CreatePlantModel createPlantModel) throws Exception {
        Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findByIdAndStatus(createPlantModel.getShipPriceID(), Status.ACTIVE);
        if(plantShipPrice == null) {
            return "Không tìm thấy dữ liệu với ShipPriceID = " + createPlantModel.getShipPriceID() + ".";
        }

        if(createPlantModel.getCategoryIDList() == null) {
            return "Danh sách Category không được để trống.";
        }

        Plant plant = new Plant();
        Plant lastPlant = plantRepository.findFirstByOrderByIdDesc();
        if(lastPlant == null) {
            plant.setId(util.createNewID("P"));
        } else {
            plant.setId(util.createIDFromLastID("P", 1, lastPlant.getId()));
        }
        plant.setName(createPlantModel.getName());
        plant.setDescription(createPlantModel.getDescription());
        plant.setHeight(createPlantModel.getHeight());
        plant.setCareNote(createPlantModel.getCareNote());
        plant.setWithPot(createPlantModel.getWithPot());
        plant.setStatus(Status.ONSALE);
        plant.setPlantShipPrice(plantShipPrice.get());

        for(String categoryID : createPlantModel.getCategoryIDList()) {
            Optional<Category> category = categoryRepository.findById(categoryID);
            if(category == null) {
                return "Không tìm thấy Category với CategoryID = " + categoryID + ".";
            }
            PlantCategory plantCategory = new PlantCategory();
            PlantCategory lastPlantCategory = plantCategoryRepository.findFirstByOrderByIdDesc();
            if(lastPlantCategory == null) {
                plantCategory.setId(util.createNewID("PC"));
            } else {
                plantCategory.setId(util.createIDFromLastID("PC", 2, lastPlantCategory.getId()));
            }
            plantCategory.setCategory(category.get());
            plantCategory.setPlant(plant);
            plantCategory.setStatus(Status.ACTIVE);
            plantCategoryRepository.save(plantCategory);
        }

        PlantPrice plantPrice = new PlantPrice();
        List<PlantPrice> checkExistedPrice = plantPriceRepository.findByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
        if(checkExistedPrice != null) {
            for(PlantPrice pPrice : checkExistedPrice) {
                pPrice.setStatus(Status.INACTIVE);
                plantPriceRepository.save(pPrice);
            }
        }
        PlantPrice lastPlantPrice = plantPriceRepository.findFirstByOrderByIdDesc();
        if(lastPlantPrice == null) {
            plantPrice.setId(util.createNewID("PP"));
        } else {
            plantPrice.setId(util.createIDFromLastID("PP", 2, lastPlantPrice.getId()));
        }
        plantPrice.setPrice(createPlantModel.getPrice());
        plantPrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        plantPrice.setPlant(plant);
        plantPrice.setStatus(Status.ACTIVE);

        for(String imageURL : createPlantModel.getListURL()) {
            PlantIMG plantIMG = new PlantIMG();
            PlantIMG lastPlantIMG = plantIMGRepository.findFirstByOrderByIdDesc();
            if(lastPlantIMG == null) {
                plantIMG.setId(util.createNewID("PIMG"));
            } else {
                plantIMG.setId(util.createIDFromLastID("PIMG", 4, lastPlantIMG.getId()));
            }
            plantIMG.setPlant(plant);
            plantIMG.setImgURL(imageURL);
            plantIMGRepository.save(plantIMG);
        }

        List<PlantPrice> plantPriceList = new ArrayList<>();
        plantPriceList.add(plantPrice);
        plant.setPlantPriceList(plantPriceList);
        plantPriceRepository.save(plantPrice);
        plantRepository.save(plant);
        return plant.getId();
    }

    @Override
    public String updatePlant(UpdatePlantModel updatePlantModel) throws Exception {
        Optional<Plant> checkPlant = plantRepository.findById(updatePlantModel.getPlantID());
        if(checkPlant != null) {
            Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findByIdAndStatus(updatePlantModel.getShipPriceID(), Status.ACTIVE);
            if(plantShipPrice == null) {
                return "Không tìm thấy dữ liệu với ShipPriceID = " + updatePlantModel.getShipPriceID() + ".";
            }
            if(updatePlantModel.getCategoryIDList() == null) {
                return "Phải có ít nhất 1 category.";
            }

            Plant plant = checkPlant.get();

            if(updatePlantModel.getName() != null) {
                plant.setName(updatePlantModel.getName());
            }

            plant.setDescription(updatePlantModel.getDescription());
            plant.setHeight(updatePlantModel.getHeight());
            plant.setCareNote(updatePlantModel.getCareNote());
            plant.setWithPot(updatePlantModel.getWithPot());
            plant.setPlantShipPrice(plantShipPrice.get());


            List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(updatePlantModel.getPlantID(), Status.ACTIVE);
            if(plantCategoryList != null) {
                for(PlantCategory plantCategory : plantCategoryList) {
                    plantCategory.setStatus(Status.INACTIVE);
                    plantCategoryRepository.save(plantCategory);
                }
            }

            for(String categoryID : updatePlantModel.getCategoryIDList()) {
                Optional<Category> category = categoryRepository.findById(categoryID);
                if(category == null) {
                    return "Không tìm thấy Category với ID là " + categoryID + ".";
                }
                PlantCategory plantCategory = new PlantCategory();
                PlantCategory lastPlantCategory = plantCategoryRepository.findFirstByOrderByIdDesc();
                if(lastPlantCategory == null) {
                    plantCategory.setId(util.createNewID("PC"));
                } else {
                    plantCategory.setId(util.createIDFromLastID("PC", 2, lastPlantCategory.getId()));
                }
                plantCategory.setCategory(category.get());
                plantCategory.setPlant(plant);
                plantCategory.setStatus(Status.ACTIVE);
                plantCategoryRepository.save(plantCategory);
            }

            if(updatePlantModel.getPrice() != null) {
                PlantPrice checkExisted = plantPriceRepository.findByPriceAndStatus(updatePlantModel.getPrice(), Status.ACTIVE);
                if(checkExisted == null) {
                    PlantPrice plantPrice = new PlantPrice();
                    List<PlantPrice> checkExistedPrice = plantPriceRepository.findByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
                    if(checkExistedPrice != null) {
                        for(PlantPrice pPrice : checkExistedPrice) {
                            pPrice.setStatus(Status.INACTIVE);
                            plantPriceRepository.save(pPrice);
                        }
                    }
                    PlantPrice lastPlantPrice = plantPriceRepository.findFirstByOrderByIdDesc();
                    if(lastPlantPrice == null) {
                        plantPrice.setId(util.createNewID("PP"));
                    } else {
                        plantPrice.setId(util.createIDFromLastID("PP", 2, lastPlantPrice.getId()));
                    }
                    plantPrice.setPrice(updatePlantModel.getPrice());
                    plantPrice.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                    plantPrice.setPlant(plant);
                    plantPrice.setStatus(Status.ACTIVE);
                    plantPriceRepository.save(plantPrice);

                    plant.getPlantPriceList().add(plantPrice);
                }
            }

            for(String imageURL : updatePlantModel.getListURL()) {
                PlantIMG plantIMG = new PlantIMG();
                PlantIMG lastPlantIMG = plantIMGRepository.findFirstByOrderByIdDesc();
                if(lastPlantIMG == null) {
                    plantIMG.setId(util.createNewID("PIMG"));
                } else {
                    plantIMG.setId(util.createIDFromLastID("PIMG", 4, lastPlantIMG.getId()));
                }
                plantIMG.setPlant(plant);
                plantIMG.setImgURL(imageURL);
                plantIMGRepository.save(plantIMG);
            }

            plantRepository.save(plant);
            return "Cập nhật thành công.";
        }
        return "Cập nhật thất bại.";
    }

    @Override
    public String activatePlant(String plantID) throws Exception {
        Plant plant = plantRepository.findByIdAndStatus(plantID, Status.INACTIVE);
        if(plant != null) {
            plant.setStatus(Status.ONSALE);
            plantRepository.save(plant);
            return "Cập nhật thành công.";
        }
        return "Không tìm thấy cây với ID là " + plantID + " có trạng thái INACTIVE.";
    }

    @Override
    public String activatePlantCategory(String plantCategoryID) throws Exception {
        PlantCategory plantCategory = plantCategoryRepository.findByIdAndStatus(plantCategoryID, Status.INACTIVE);
        if(plantCategory != null) {
            plantCategory.setStatus(Status.ACTIVE);
            plantCategoryRepository.save(plantCategory);
            return "Cập nhật thành công.";
        }
        return "Không tìm thấy PlantCategory với ID là " + plantCategoryID + " có trạng thái INACTIVE.";
    }

    @Override
    public String deletePlant(String plantID) {
        Optional<Plant> checkingPlant = plantRepository.findById(plantID);
        if(checkingPlant != null) {
            Plant plant = checkingPlant.get();
            StorePlant storePlant = storePlantRepository.findByPlant_IdAndPlant_Status(plantID, Status.ONSALE);
            if(storePlant != null) {
                if(orderDetailRepository.findByStorePlant_IdAndTblOrder_ProgressStatus(storePlant.getId(), Status.WAITING) != null && !orderDetailRepository.findByStorePlant_IdAndTblOrder_ProgressStatus(plantID, Status.WAITING).isEmpty()) {
                    return "Không thể xóa cây đang được sử dụng.";
                }
            }

            plant.setStatus(Status.INACTIVE);
            plantRepository.save(checkingPlant.get());
            return "Xóa cây thành công.";
        }
        return "Không tìm thấy cây với ID là " + plantID + ".";
    }

    @Override
    public String deletePlantIMG(String id) {
        Optional<PlantIMG> checkExisted = plantIMGRepository.findById(id);
        if(checkExisted != null) {
            PlantIMG plantIMG = checkExisted.get();
            plantIMG.setPlant(null);
            plantIMG.setImgURL(null);
            plantIMGRepository.save(plantIMG);
            return "Xóa thành công.";
        }
        return "Không tìm thấy PlantIMG với ID là " + id + ".";
    }

    @Override
    public Plant checkDuplicate(String plantName) {
        Plant checkingPlant = plantRepository.findByName(plantName);
        return checkingPlant;
    }

    @Override
    public List<String> getPlantIMGByPlantID(String plantID) {
        List<PlantIMG> imgList = plantIMGRepository.findByPlant_Id(plantID);
        List<String> imgURL = new ArrayList<>();
        for(PlantIMG plantIMG : imgList) {
            imgURL.add(firebaseImageService.getImageUrl(plantIMG.getImgURL()));
        }
        return imgURL;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategory(String categoryID, Pageable paging) {
        Page<PlantCategory> plantCategoryPage = plantCategoryPagingRepository.findByCategory_IdAndStatus(categoryID, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantCategoryPage.map(plantCategory -> plantRepository.getById(plantCategory.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByName(String name, Pageable paging) {
        Page<Plant> pagingResult = plantPagingRepository.findByNameContainingAndStatus(name, Status.ONSALE, paging);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByPriceMin(Double minPrice, Pageable paging) {
        Page<PlantPrice> plantPricePage = plantPricePagingRepository.findByPriceGreaterThanEqualAndStatus(minPrice, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantPricePage.map(plantPrice -> plantRepository.getById(plantPrice.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByPriceMax(Double maxPrice, Pageable paging) {
        Page<PlantPrice> plantPricePage = plantPricePagingRepository.findByPriceLessThanEqualAndStatus(maxPrice, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantPricePage.map(plantPrice -> plantRepository.getById(plantPrice.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByPriceInRange(Double fromPrice, Double toPrice, Pageable paging) {
        Page<PlantPrice> plantPricePage = plantPricePagingRepository.findByPriceBetweenAndStatus(fromPrice, toPrice, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantPricePage.map(plantPrice -> plantRepository.getById(plantPrice.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndName(String categoryID, String name, Pageable paging) {
        Page<PlantCategory> plantCategoryPage = plantCategoryPagingRepository.findByCategory_IdAndPlant_NameContainingAndStatus(categoryID, name, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantCategoryPage.map(plantCategory -> plantRepository.getById(plantCategory.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        Page<PlantPrice> plantPricePage = plantPricePagingRepository.findByPlant_NameContainingAndPriceBetweenAndStatus(name, fromPrice, toPrice, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantPricePage.map(plantPrice -> plantRepository.getById(plantPrice.getPlant().getId()));
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndPrice(String categoryID, Double fromPrice, Double toPrice, Pageable paging) {
        Page<PlantCategory> plantCategoryPage = plantCategoryPagingRepository.findByCategory_IdAndStatus(categoryID, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantCategoryPage.map(plantCategory -> plantRepository.getById(plantCategory.getPlant().getId()));
        return util.plantPricePagingConverter(fromPrice, toPrice, pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(String categoryID, String name, Double fromPrice, Double toPrice, Pageable paging) {
        Page<PlantCategory> plantCategoryPage = plantCategoryPagingRepository.findByCategory_IdAndPlant_NameContainingAndStatus(categoryID, name, Status.ACTIVE, paging);
        Page<Plant> pagingResult = plantCategoryPage.map(plantCategory -> plantRepository.getById(plantCategory.getPlant().getId()));
        return util.plantPricePagingConverter(fromPrice, toPrice, pagingResult, paging);
    }
}
