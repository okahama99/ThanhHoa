package com.example.thanhhoa.services.category;

import com.example.thanhhoa.dtos.CategoryModels.ShowCategoryModel;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<ShowCategoryModel> getAllCategory(Pageable paging);

    ShowCategoryModel getCategoryByID(String categoryID);

    List<ShowCategoryModel> getCategory();

    String activateCategory(String categoryID) throws Exception;

    String create(String name, Status status);

    String update(String id, String name);

    String delete(String id);
}
