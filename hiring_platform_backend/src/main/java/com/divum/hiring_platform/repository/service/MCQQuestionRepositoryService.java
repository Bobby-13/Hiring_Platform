package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.dto.McqPaginationDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MCQQuestionRepositoryService {
    Page<McqPaginationDto> getAllMCQs(Pageable pageable);

    Page<McqPaginationDto> getAllMCQByType(Pageable pageable, List<QuestionCategory> type);

    Page<McqPaginationDto> getAllMCQByDifficulty(Pageable pageable, List<Difficulty> difficulty);

    Page<McqPaginationDto> getAllMCQByDifficultyAndType(Pageable pageable, List<Difficulty> difficulty, List<QuestionCategory> type);

    MultipleChoiceQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int i);

    Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty);

    void save(MultipleChoiceQuestion mcq);

    Optional<MultipleChoiceQuestion> findById(String questionId);

    void deleteById(String questionId);
}
