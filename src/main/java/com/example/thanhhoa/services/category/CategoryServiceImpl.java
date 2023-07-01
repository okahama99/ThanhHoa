package com.example.thanhhoa.services.category;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.repositories.CategoryRepository;
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
            listModel.add(model);
        }
        return listModel;
    }
}
