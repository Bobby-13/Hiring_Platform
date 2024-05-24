package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.McqDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UpdateMcqDto;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/questions/mcq")
public interface MCQQuestionApi {

    @GetMapping("/{questionId}")
    ResponseEntity<ResponseDto> getQuestion(@PathVariable String questionId);

    @PutMapping("/{questionId}")
    ResponseEntity<ResponseDto> updateQuestion(@PathVariable String questionId, @RequestBody UpdateMcqDto updateMcqDto);

    @DeleteMapping("/{questionId}")
    ResponseEntity<ResponseDto> deleteQuestion(@PathVariable String questionId);

    @PostMapping
    ResponseEntity<ResponseDto> addQuestion(@RequestBody McqDto mcqDto);

    @GetMapping
    ResponseEntity<ResponseDto> getAll(Pageable pageable, @RequestParam(required = false) List<QuestionCategory> type, @RequestParam(required = false) List<Difficulty> difficulty);

}
