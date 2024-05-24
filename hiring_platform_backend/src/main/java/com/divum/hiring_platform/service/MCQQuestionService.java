package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.McqDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UpdateMcqDto;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MCQQuestionService {

    ResponseEntity<ResponseDto> getQuestion(String questionId);


    ResponseEntity<ResponseDto> updateQuestion(String questionId, UpdateMcqDto updateMcqDto);

    ResponseEntity<ResponseDto> deleteQuestion(String questionId);

    ResponseEntity<ResponseDto> addQuestion(McqDto mcqDto);

    ResponseEntity<ResponseDto> getAll(Pageable pageable, List<QuestionCategory> type, List<Difficulty> difficulty);
}
