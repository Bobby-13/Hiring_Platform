package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.dto.McqPaginationDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.repository.MCQQuestionRepository;
import com.divum.hiring_platform.repository.service.MCQQuestionRepositoryService;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MCQQuestionRepositoryServiceImpl implements MCQQuestionRepositoryService {
    private final MCQQuestionRepository mcqQuestionRepository;


    @Override
    public Page<McqPaginationDto> getAllMCQs(Pageable pageable) {
        return mcqQuestionRepository.getAllMCQs(pageable);
    }

    @Override
    public Page<McqPaginationDto> getAllMCQByType(Pageable pageable, List<QuestionCategory> type) {
        return mcqQuestionRepository.getAllMCQByType(pageable, type);
    }

    @Override
    public Page<McqPaginationDto> getAllMCQByDifficulty(Pageable pageable, List<Difficulty> difficulty) {
        return mcqQuestionRepository.getAllMCQByDifficulty(pageable, difficulty);
    }

    @Override
    public Page<McqPaginationDto> getAllMCQByDifficultyAndType(Pageable pageable, List<Difficulty> difficulty, List<QuestionCategory> type) {
        return mcqQuestionRepository.getAllMCQByDifficultyAndType(pageable, difficulty, type);
    }

    @Override
    public MultipleChoiceQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int i) {
        return mcqQuestionRepository.getRandomQuestion(categoryId, difficulty, contest, i);
    }

    @Override
    public Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty) {
        return mcqQuestionRepository.getQuestionCountDifficultyWise(category, difficulty);
    }

    @Override
    public void save(MultipleChoiceQuestion mcq) {
        mcqQuestionRepository.save(mcq);
    }

    @Override
    public Optional<MultipleChoiceQuestion> findById(String questionId) {
        return mcqQuestionRepository.findById(questionId);
    }

    @Override
    public void deleteById(String questionId) {
        mcqQuestionRepository.deleteById(questionId);
    }
}



