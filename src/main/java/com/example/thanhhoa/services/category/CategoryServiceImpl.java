package com.example.thanhhoa.services.category;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.CategoryRepository;
import com.example.thanhhoa.repositories.PlantCategoryRepository;
import com.example.thanhhoa.repositories.pagings.CategoryPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private Util util;
    @Autowired
    private CategoryPagingRepository categoryPagingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PlantCategoryRepository plantCategoryRepository;

    @Override
    public List<ShowCategoryModel> getAllCategory(Pageable paging) {
        Page<Category> pagingResult = categoryPagingRepository.findAll(paging);
        return util.categoryPagingConverter(pagingResult, paging);
    }

    @Override
    public ShowCategoryModel getCategoryByID(String categoryID) {
        Optional<Category> searchResult = categoryRepository.findById(categoryID);
        if(searchResult == null){
            return null;
        }
        Category category = searchResult.get();
        ShowCategoryModel model = new ShowCategoryModel();
        model.setCategoryID(category.getId());
        model.setCategoryName(category.getName());
        model.setStatus(category.getStatus());
        return model;
    }

    @Override
    public List<ShowCategoryModel> getCategory() {
        List<Category> list = categoryRepository.findAll();
        if(list == null){
            return null;
        }
        List<ShowCategoryModel> listModel = new ArrayList<>();
        for(Category category : list) {
            ShowCategoryModel model = new ShowCategoryModel();
            model.setCategoryID(category.getId());
            model.setCategoryName(category.getName());
            model.setStatus(category.getStatus());
            listModel.add(model);
        }
        return listModel;
    }

    @Override
    public String create(String name) {
        Category checkExisted = categoryRepository.findByName(name);
        if(checkExisted != null){
            return "Đã tồn tại Category với Tên là : " + name + ".";
        }
        Category category = new Category();
        Category lastCategory = categoryRepository.findFirstByOrderByIdDesc();
        if(lastCategory == null) {
            category.setId(util.createNewID("C"));
        } else {
            category.setId(util.createIDFromLastID("C", 1, lastCategory.getId()));
        }
        category.setName(name);
        categoryRepository.save(category);
        return "Tạo thành công.";
    }

    @Override
    public String update(String id, String name) {
        Optional<Category> checkExistedID = categoryRepository.findById(id);
        if(checkExistedID != null){
            return "Đã tồn tại Category với ID là : " + id + ".";
        }
        Category checkExistedName = categoryRepository.findByName(name);
        if(checkExistedName != null){
            return "Đã tồn tại Category với Tên là : " + name + ".";
        }
        Category category = checkExistedID.get();
        category.setName(name);
        categoryRepository.save(category);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String delete(String id) {
        Optional<Category> checkExistedID = categoryRepository.findByIdAndStatus(id, Status.ACTIVE);
        if(checkExistedID == null){
            return "Không tìm thấy Category với ID là : " + id + ".";
        }
        Category category = checkExistedID.get();
        category.setStatus(Status.INACTIVE);

        List<PlantCategory> plantCategoryList = plantCategoryRepository.findByCategory_IdAndStatus(id, Status.ACTIVE);
        if(plantCategoryList != null && !plantCategoryList.isEmpty()){
            for(PlantCategory plantCategory : plantCategoryList) {
                plantCategory.setStatus(Status.INACTIVE);
                plantCategoryRepository.save(plantCategory);
            }
        }
        categoryRepository.save(category);
        return "Xóa thành công";
    }
}
