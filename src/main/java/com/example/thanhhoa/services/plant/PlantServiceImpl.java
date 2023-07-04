package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantIMGModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.OrderDetail;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.entities.PlantShipPrice;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.OrderDetailRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantPriceRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.PlantShipPriceRepository;
import com.example.thanhhoa.repositories.pagings.PlantPagingRepository;
import com.example.thanhhoa.services.firebaseIMG.ImageService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private PlantPagingRepository plantPagingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
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
        if(checkExistedPlant == null) {
            return null;
        }
        Plant plant = checkExistedPlant.get();
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findAllByPlant_IdAndStatus(plant.getId(), Status.ACTIVE);
        List<ShowPlantCategory> showPlantCategoryList = new ArrayList<>();
        ShowPlantCategory showPlantCategory = new ShowPlantCategory();
        for(PlantCategory plantCategory : plantCategoryList) {
            showPlantCategory.setCategoryID(plantCategory.getCategory().getId());
            showPlantCategory.setCategoryName(plantCategory.getCategory().getName());
            showPlantCategoryList.add(showPlantCategory);
        }

        List<ShowPlantIMGModel> showPlantIMGList = new ArrayList<>();
        List<PlantIMG> plantIMGList = plantIMGRepository.findByPlant_Id(plant.getId());
        if(plantIMGList != null){
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

        PlantPrice newestPrice = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
        ShowPlantPriceModel showPlantPriceModel = new ShowPlantPriceModel();
        showPlantPriceModel.setId(newestPrice.getId());
        showPlantPriceModel.setPrice(newestPrice.getPrice());
        showPlantPriceModel.setApplyDate(newestPrice.getApplyDate());

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
        model.setStatus(plant.getStatus());
        return model;
    }

    @Override
    public String createPlant(CreatePlantModel createPlantModel) throws Exception {
        Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findById(createPlantModel.getShipPriceID());
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
            plantCategoryRepository.save(plantCategory);
        }

        if(createPlantModel.getFiles() != null) {
            for(MultipartFile file : createPlantModel.getFiles()) {
                String fileName = imageService.save(file);
                String imgName = imageService.getImageUrl(fileName);
                PlantIMG plantIMG = new PlantIMG();
                PlantIMG lastPlantIMG = plantIMGRepository.findFirstByOrderByIdDesc();
                if(lastPlantIMG == null) {
                    plantIMG.setId(util.createNewID("PIMG"));
                } else {
                    plantIMG.setId(util.createIDFromLastID("PIMG", 4, lastPlantIMG.getId()));
                }
                plantIMG.setPlant(plant);
                plantIMG.setImgURL(imgName);
                plantIMGRepository.save(plantIMG);
            }
        }

        PlantPrice plantPrice = new PlantPrice();
        PlantPrice lastPlantPrice = plantPriceRepository.findFirstByOrderByIdDesc();
        if(lastPlantPrice == null) {
            plantPrice.setId(util.createNewID("PP"));
        } else {
            plantPrice.setId(util.createIDFromLastID("PP", 2, lastPlantPrice.getId()));
        }
        plantPrice.setPrice(createPlantModel.getPrice());
        plantPrice.setApplyDate(LocalDateTime.now());
        plantPrice.setPlant(plant);
        plantPriceRepository.save(plantPrice);

        List<PlantPrice> plantPriceList = new ArrayList<>();
        plantPriceList.add(plantPrice);
        plant.setPlantPriceList(plantPriceList);
        plantRepository.save(plant);
        return "Tạo thành công.";
    }

    @Override
    public String updatePlant(UpdatePlantModel updatePlantModel) throws Exception {
        Optional<Plant> checkPlant = plantRepository.findById(updatePlantModel.getPlantID());
        if(checkPlant != null) {
            Optional<PlantShipPrice> plantShipPrice = plantShipPriceRepository.findById(updatePlantModel.getShipPriceID());
            if(plantShipPrice == null) {
                return "Không tìm thấy dữ liệu với ShipPriceID = " + updatePlantModel.getShipPriceID() + ".";
            }

            Plant plant = checkPlant.get();
            plant.setName(updatePlantModel.getName());
            plant.setDescription(updatePlantModel.getDescription());
            plant.setHeight(updatePlantModel.getHeight());
            plant.setCareNote(updatePlantModel.getCareNote());
            plant.setWithPot(updatePlantModel.getWithPot());
            plant.setPlantShipPrice(plantShipPrice.get());


            if(updatePlantModel.getCategoryIDList() == null) {
                return "Danh sách Category không được để trống.";
            }
            // Tim category tu list categoryID roi add vao 1 list
            List<Category> categoryList = new ArrayList<>();
            for(String categoryID : updatePlantModel.getCategoryIDList()) {
                Optional<Category> category = categoryRepository.findById(categoryID);
                if(category != null) {
                    categoryList.add(category.get());
                }
            }
            // Lay het toan bo relationship dang ton tai cua plant
            if(categoryList != null) {
                List<PlantCategory> plantCategoryList = plantCategoryRepository.findByPlantAndStatus(plant, Status.ACTIVE);
                List<Category> plantCate = new ArrayList<>();
                // Luu lai cac category dang ton tai roi xoa relationship
                for(PlantCategory plantCategory : plantCategoryList) {
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
                for(Category category : plantCate) {
                    PlantCategory plantCategory = new PlantCategory();
                    PlantCategory lastPlantCategory = plantCategoryRepository.findFirstByOrderByIdDesc();
                    if(lastPlantCategory == null) {
                        plantCategory.setId(util.createNewID("PC"));
                    } else {
                        plantCategory.setId(util.createIDFromLastID("PC", 2, lastPlantCategory.getId()));
                    }
                    plantCategory.setPlant(plant);
                    plantCategory.setCategory(category);
                    plantCategoryRepository.save(plantCategory);
                }
            }

            if(updatePlantModel.getFiles() != null) {
                for(PlantIMG image : plant.getPlantIMGList()) {
                    String imgNameString = image.getImgURL();
                    PlantIMG plantImage = plantIMGRepository.findByImgURL(imgNameString);
                    plantImage.setPlant(null);
                    plantImage.setImgURL(null);
                    plantIMGRepository.save(plantImage);
                    String[] strArr;
                    strArr = imgNameString.split("[/;?]");
                    imageService.delete(strArr[7]);
                }
                for(MultipartFile file : updatePlantModel.getFiles()) {
                    String fileName = imageService.save(file);
                    String imgName = imageService.getImageUrl(fileName);
                    PlantIMG plantIMG = new PlantIMG();
                    PlantIMG lastPlantIMG = plantIMGRepository.findFirstByOrderByIdDesc();
                    if(lastPlantIMG == null) {
                        plantIMG.setId(util.createNewID("PIMG"));
                    } else {
                        plantIMG.setId(util.createIDFromLastID("PIMG", 4, lastPlantIMG.getId()));
                    }
                    plantIMG.setPlant(plant);
                    plantIMG.setImgURL(imgName);
                    plantIMGRepository.save(plantIMG);
                }
            }

            if(updatePlantModel.getPrice() != null && updatePlantModel.getApplyDate() != null){
                PlantPrice checkExisted = plantPriceRepository.findByPriceAndApplyDate
                        (updatePlantModel.getPrice(), updatePlantModel.getApplyDate());
                if(checkExisted == null){
                    PlantPrice plantPrice = new PlantPrice();
                    PlantPrice lastPlantPrice = plantPriceRepository.findFirstByOrderByIdDesc();
                    if(lastPlantPrice == null) {
                        plantPrice.setId(util.createNewID("PP"));
                    } else {
                        plantPrice.setId(util.createIDFromLastID("PP", 2, lastPlantPrice.getId()));
                    }
                    plantPrice.setPrice(updatePlantModel.getPrice());
                    plantPrice.setApplyDate(LocalDateTime.now());
                    plantPrice.setPlant(plant);
                    plantPriceRepository.save(plantPrice);

                    plant.getPlantPriceList().add(plantPrice);
                }
            }

            plantRepository.save(plant);
            return "Cập nhật thành công.";
        }
        return "Cập nhật thất bại.";
    }

    @Override
    public String deletePlant(String plantID) {
        Optional<Plant> checkingPlant = plantRepository.findById(plantID);
        if(checkingPlant != null) {
            Plant plant = checkingPlant.get();

            if(orderDetailRepository.findByPlant_IdAndTblOrder_ProgressStatus(plantID, Status.WAITING) != null){
                return "Không thể xóa cây đang được sử dụng.";
            }

            plant.setStatus(Status.INACTIVE);
            plantRepository.save(checkingPlant.get());
            return "Xóa cây thành công.";
        }
        return "Không tìm thấy cây với ID là " + plantID + ".";
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
            imgURL.add(imageService.getImageUrl(plantIMG.getImgURL()));
        }
        return imgURL;
    }

    @Override
    public List<ShowPlantModel> getPlantByCategory(String categoryID, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndStatus(categoryID, Status.ACTIVE);

        List<Plant> plantList = new ArrayList<>();
        for(PlantCategory plantCategory : plantCategoryList) {
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
    public List<ShowPlantModel> getPlantByPriceMin(Double minPrice, Pageable paging) {
        List<Plant> list = plantRepository.findAllByStatus(Status.ONSALE);
        if(list == null){
            return null;
        }

        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : list) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() < minPrice){
                removePlants.add(plant);
            }
        }
        list.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(list);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByPriceMax(Double maxPrice, Pageable paging) {
        List<Plant> list = plantRepository.findAllByStatus(Status.ONSALE);
        if(list == null){
            return null;
        }

        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : list) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() > maxPrice){
                removePlants.add(plant);
            }
        }
        list.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(list);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByPriceInRange(Double fromPrice, Double toPrice, Pageable paging) {
        List<Plant> list = plantRepository.findAllByStatus(Status.ONSALE);
        if(list == null){
            return null;
        }

        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : list) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() < fromPrice || price.getPrice() > toPrice){
                removePlants.add(plant);
            }
        }
        list.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(list);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndName(String categoryID, String name, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndPlant_NameContainingAndStatus(categoryID, name, Status.ACTIVE);
        List<Plant> catePlantList = new ArrayList<>();
        for(PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }
        Page<Plant> pagingResult = new PageImpl<>(catePlantList);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging) {
        List<Plant> list = plantRepository.findAllByNameContainingAndStatus(name, Status.ONSALE);
        if(list == null){
            return null;
        }

        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : list) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() < fromPrice || price.getPrice() > toPrice){
                removePlants.add(plant);
            }
        }
        list.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(list);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndPrice(String categoryID, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndStatus(categoryID, Status.ACTIVE);

        List<Plant> catePlantList = new ArrayList<>();
        for(PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : catePlantList) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() < fromPrice || price.getPrice() > toPrice){
                removePlants.add(plant);
            }
        }
        catePlantList.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(catePlantList);
        return util.plantPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(String categoryID, String name, Double fromPrice, Double toPrice, Pageable paging) {
        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndStatus(categoryID, Status.ACTIVE);

        List<Plant> catePlantList = new ArrayList<>();
        for(PlantCategory plantCategory : plantCategoryList) {
            catePlantList.add(plantCategory.getPlant());
        }

        List<Plant> list = plantRepository.findAllByNameContainingAndStatus(name, Status.ONSALE);
        if(list == null){
            return null;
        }

        list.addAll(catePlantList);
        List<Plant> noDuplicate = new ArrayList<>(new HashSet<>(list));
        List<Plant> removePlants = new ArrayList<>();
        for(Plant plant : noDuplicate) {
            PlantPrice price = plantPriceRepository.findFirstByPlant_IdOrderByApplyDateDesc(plant.getId());
            if(price.getPrice() < fromPrice || price.getPrice() > toPrice){
                removePlants.remove(plant);
            }
        }
        list.removeAll(removePlants);
        Page<Plant> pagingResult = new PageImpl<>(noDuplicate);
        return util.plantPagingConverter(pagingResult, paging);
    }
}
