package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.dto.GetCodingQnDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.repository.CodingQuestionRepository;
import com.divum.hiring_platform.repository.service.CodingQuestionRepositoryService;
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
public class CodingQuestionRepositoryServiceImpl implements CodingQuestionRepositoryService {
    private final CodingQuestionRepository codingQuestionRepository;

    @Override
    public Page<GetCodingQnDto> findCodingQuestionsWithCategoryAndDifficulty(List<QuestionCategory> category, List<Difficulty> difficulty, Pageable pageRequest) {
        return codingQuestionRepository.findCodingQuestionsWithCategoryAndDifficulty(category,difficulty,pageRequest);
    }

    @Override
    public Page<GetCodingQnDto> findCodingQuestionsByCategory(List<QuestionCategory> category, Pageable pageRequest) {
        return codingQuestionRepository.findCodingQuestionsByCategory(category, pageRequest);
    }

    @Override
    public Page<GetCodingQnDto> findCodingQuestionsByDifficulty(List<Difficulty> difficulty, Pageable pageRequest) {
        return codingQuestionRepository.findCodingQuestionsByDifficulty(difficulty,pageRequest);
    }

    @Override
    public Page<GetCodingQnDto> findAllQn(Pageable pageRequest) {
        return codingQuestionRepository.findAllQn(pageRequest);

    }
    @Override
    public Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty) {
        return codingQuestionRepository.getQuestionCountDifficultyWise(category, difficulty);
    }

    @Override
    public CodingQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int i) {
        return codingQuestionRepository.getRandomQuestion(categoryId, difficulty,contest, i);
    }

    @Override
    public Optional<CodingQuestion> findById(long questionId) {
        return codingQuestionRepository.findById(questionId);
    }

    @Override
    public void deleteAll() {
        codingQuestionRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        codingQuestionRepository.deleteById(id);
    }

    @Override
    public void save(CodingQuestion codingQuestion) {
        codingQuestionRepository.save(codingQuestion);
    }

    @Override
    public void saveAll(List<CodingQuestion> codeQuestions) {
        codingQuestionRepository.saveAll(codeQuestions);
    }

}
