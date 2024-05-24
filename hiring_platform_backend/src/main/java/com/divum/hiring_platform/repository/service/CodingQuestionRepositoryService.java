package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.dto.GetCodingQnDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CodingQuestionRepositoryService {
    Page<GetCodingQnDto> findCodingQuestionsWithCategoryAndDifficulty(List<QuestionCategory> category, List<Difficulty> difficulty, Pageable pageRequest);

    Page<GetCodingQnDto> findCodingQuestionsByCategory(List<QuestionCategory> category, Pageable pageRequest);

    Page<GetCodingQnDto> findCodingQuestionsByDifficulty(List<Difficulty> difficulty, Pageable pageRequest);

    Page<GetCodingQnDto> findAllQn(Pageable pageRequest);

    Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty);

    CodingQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int i);

    Optional<CodingQuestion> findById(long questionId);

    void deleteAll();

    void deleteById(Long id);

    void save(CodingQuestion codingQuestion);

    void saveAll(List<CodingQuestion> codeQuestions);
}

