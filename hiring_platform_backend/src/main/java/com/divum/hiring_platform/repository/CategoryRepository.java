package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByQuestionCategory(QuestionCategory category);

}
