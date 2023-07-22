package com.example.thanhhoa.services.category;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<ShowCategoryModel> getAllCategory(Pageable paging);

    ShowCategoryModel getCategoryByID(String categoryID);

    List<ShowCategoryModel> getCategory();

    String create(String name);

    String update(String id, String name);
}
