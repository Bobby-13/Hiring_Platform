package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.CategoryRepositoryService;
import com.divum.hiring_platform.repository.service.MCQQuestionRepositoryService;
import com.divum.hiring_platform.repository.service.OptionsRepositoryService;
import com.divum.hiring_platform.repository.service.RoundsAndMcqQuestionRepositoryService;
import com.divum.hiring_platform.service.MCQQuestionService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MCQQuestionServiceImpl implements MCQQuestionService {
    private final CategoryRepositoryService categoryRepositoryService;
    private final OptionsRepositoryService optionsRepositoryService;
    private final MCQQuestionRepositoryService mcqQuestionRepositoryService;
    private final RoundsAndMcqQuestionRepositoryService roundsAndMcqQuestionRepositoryService;


    @Override
    public ResponseEntity<ResponseDto> getQuestion(String questionId) {
        Optional<MultipleChoiceQuestion> multipleChoiceQuestion = mcqQuestionRepositoryService.findById(questionId);
        if (multipleChoiceQuestion.isEmpty())
            throw new ResourceNotFoundException(Strings.NOT_FOUND_MCQ_QUESTION_ID);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("The question with the id ", multipleChoiceQuestion));

    }


    public ResponseEntity<ResponseDto> updateQuestion(String questionId, UpdateMcqDto updateMcqDto) {

        Optional<MultipleChoiceQuestion> multipleChoiceQuestion = mcqQuestionRepositoryService.findById(questionId);
        if (multipleChoiceQuestion.isEmpty())
            throw new ResourceNotFoundException(Strings.NOT_FOUND_MCQ_QUESTION_ID);


        updateQuestionDetails(multipleChoiceQuestion.get(), updateMcqDto);
        mcqQuestionRepositoryService.save(multipleChoiceQuestion.get());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.UPDATE_MCQ_SUCCESS, multipleChoiceQuestion));

    }

    public void updateQuestionDetails(MultipleChoiceQuestion question, UpdateMcqDto updateMcqDto) {
        if (updateMcqDto.getQuestion() != null) {
            question.setQuestion(updateMcqDto.getQuestion());
        }
        if (updateMcqDto.getCategory() != null) {
            Category category = categoryRepositoryService.findCategoryByCategory(updateMcqDto.getCategory());
            question.setCategory(category);
        }
        if (updateMcqDto.getDifficulty() != null) {
            question.setDifficulty(Difficulty.valueOf(updateMcqDto.getDifficulty()));
        }
        if (updateMcqDto.getOptions() != null) {
            updateOptions(question, updateMcqDto.getOptions());
        }
        if (updateMcqDto.getQuestion() != null) {
            question.setImageUrl(question.getImageUrl());
        }
    }

    public void updateOptions(MultipleChoiceQuestion question, List<Options> updatedOptions) {
        List<Options> optionsList = new ArrayList<>();
        for (Options option : updatedOptions) {
            if (option.getId() == null) {
                option.setMultipleChoiceQuestion(question);
                optionsList.add(option);
            } else {
                Optional<Options> optionalOption = optionsRepositoryService.findById(option.getId());
                optionalOption.ifPresent(existingOption -> {
                    existingOption.setOption(option.getOption());
                    existingOption.setId(option.getId());
                    existingOption.setCorrect(option.isCorrect());
                    existingOption.setMultipleChoiceQuestion(question);
                    optionsList.add(existingOption);
                });
            }
        }
        question.setOptions(optionsList);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteQuestion(String questionId) {

        Optional<MultipleChoiceQuestion> multipleChoiceQuestion = mcqQuestionRepositoryService.findById(questionId);
        if (multipleChoiceQuestion.isEmpty())
            throw new ResourceNotFoundException(Strings.NOT_FOUND_MCQ_QUESTION_ID);
        List<RoundAndMcqQuestion> roundAndMcqQuestions = roundsAndMcqQuestionRepositoryService.findRoundAndMcqQuestionByMcq(multipleChoiceQuestion.get());
        for (RoundAndMcqQuestion roundAndMcqQuestion : roundAndMcqQuestions) {
            roundsAndMcqQuestionRepositoryService.delete(roundAndMcqQuestion);
        }
        mcqQuestionRepositoryService.deleteById(multipleChoiceQuestion.get().getQuestionId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.DELETE_MCQ_SUCCESS, null));

    }

    @Override
    public ResponseEntity<ResponseDto> addQuestion(McqDto mcqDto) {
        if (mcqDto != null) {
            MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
            multipleChoiceQuestion.setQuestion(mcqDto.getQuestion());
            multipleChoiceQuestion.setDifficulty(Difficulty.valueOf(mcqDto.getDifficulty()));
            Category category = categoryRepositoryService.findCategoryByCategory(mcqDto.getCategory());
            multipleChoiceQuestion.setCategory(category);
            List<McqImageUrl> images = new ArrayList<>();
            for (String image : mcqDto.getImageUrl()) {
                McqImageUrl mcqImageUrl = new McqImageUrl();
                mcqImageUrl.setId(UUID.randomUUID().toString());
                mcqImageUrl.setImageUrl(image);
                mcqImageUrl.setMultipleChoiceQuestion(multipleChoiceQuestion);
                images.add(mcqImageUrl);
            }
            multipleChoiceQuestion.setImageUrl(images);
            List<Options> options = new ArrayList<>();

            for (OptionDto optionDto : mcqDto.getOptions()) {
                Options options1 = new Options();
                options1.setOption(optionDto.getOption());
                options1.setMultipleChoiceQuestion(multipleChoiceQuestion);
                options1.setCorrect(optionDto.getIsCorrect());
                options.add(options1);
            }
            multipleChoiceQuestion.setOptions(options);
            mcqQuestionRepositoryService.save(multipleChoiceQuestion);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.CREATE_MCQ_QUESTION_SUCCESS, multipleChoiceQuestion));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(Strings.EMPTY_INPUT, null));
        }
    }


    @Override
    public ResponseEntity<ResponseDto> getAll(Pageable pageable, List<QuestionCategory> type, List<Difficulty> difficulty) {
        if (type == null && difficulty == null) {
            Page<McqPaginationDto> multipleChoiceQuestions = mcqQuestionRepositoryService.getAllMCQs(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.ALL_QUESTIONS,multipleChoiceQuestions));
        } else if (type != null && difficulty == null) {
            Page<McqPaginationDto> multipleChoiceQuestions = mcqQuestionRepositoryService.getAllMCQByType(pageable, type);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.ALL_QUESTIONS_WITH_CATEGORY + type, multipleChoiceQuestions));
        } else if (type == null) {
            Page<McqPaginationDto> multipleChoiceQuestions = mcqQuestionRepositoryService.getAllMCQByDifficulty(pageable, difficulty);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.ALL_QUESTIONS_WITH_DIFFICULTY + difficulty, multipleChoiceQuestions));
        } else {
            Page<McqPaginationDto> multipleChoiceQuestions = mcqQuestionRepositoryService.getAllMCQByDifficultyAndType(pageable, difficulty, type);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.ALL_QUESTIONS_WITH_CATEGORY_AND_DIFFICULTY +"\n"+ type +"\n"+difficulty+"\n",multipleChoiceQuestions));
        }

    }
}
