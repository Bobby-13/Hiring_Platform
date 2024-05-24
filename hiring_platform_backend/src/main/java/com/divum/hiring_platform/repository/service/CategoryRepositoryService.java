package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.util.enums.QuestionCategory;

import java.util.List;

public interface CategoryRepositoryService {
    Category findCategoryByCategory(String category);

    Category findCategoryByQuestionCategory(QuestionCategory category);

    List<Category> findAll();

    void save(Category category);

    Category findByQuestionCategory(QuestionCategory categoryString);

}
