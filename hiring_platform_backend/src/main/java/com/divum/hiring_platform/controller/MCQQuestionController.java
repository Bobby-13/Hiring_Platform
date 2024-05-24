package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.MCQQuestionApi;
import com.divum.hiring_platform.dto.McqDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UpdateMcqDto;
import com.divum.hiring_platform.service.MCQQuestionService;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MCQQuestionController implements MCQQuestionApi {

    private final MCQQuestionService mcqQuestionService;


    @Override
    public ResponseEntity<ResponseDto> getQuestion(String questionId) {
        return mcqQuestionService.getQuestion(questionId);
    }

    @Override

    public ResponseEntity<ResponseDto> updateQuestion(String questionId, UpdateMcqDto updateMcqDto) {
        return mcqQuestionService.updateQuestion(questionId, updateMcqDto);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteQuestion(String questionId) {
        return mcqQuestionService.deleteQuestion(questionId);
    }


    @Override
    public ResponseEntity<ResponseDto> addQuestion(McqDto mcqDto) {
        return mcqQuestionService.addQuestion(mcqDto);
    }

    @Override
    public ResponseEntity<ResponseDto> getAll(Pageable pageable, List<QuestionCategory> type, List<Difficulty> difficulty) {
        return mcqQuestionService.getAll(pageable, type, difficulty);
    }


}
