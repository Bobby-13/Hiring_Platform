package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.repository.CategoryRepository;
import com.divum.hiring_platform.repository.service.CategoryRepositoryService;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryRepositoryServiceImpl implements CategoryRepositoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public Category findByQuestionCategory(QuestionCategory categoryString) {
        return categoryRepository.findByQuestionCategory(categoryString);
    }

    @Override
    public Category findCategoryByCategory(String category) {
        return categoryRepository.findByQuestionCategory(QuestionCategory.valueOf(category));
    }

    @Override
    public Category findCategoryByQuestionCategory(QuestionCategory category) {
        return categoryRepository.findByQuestionCategory(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

}
