package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.CreateCodingQnDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/coding-question")
public interface CodingQnApi {
    @PostMapping
    ResponseEntity<ResponseDto> createCodingQuestion(@RequestBody CreateCodingQnDto createCodingQnDto) throws IOException;
    @GetMapping("/{id}")
    ResponseEntity<ResponseDto> getCodingQuestion(@PathVariable Long id);

    @GetMapping()
    ResponseEntity<ResponseDto> getCodingQuestions(@RequestParam ("page")int page,@RequestParam ("size")int size, @RequestParam(value = "type", required = false)List<QuestionCategory> category, @RequestParam(value = "difficulty", required = false)List<Difficulty> difficulty);

    @PutMapping("/{id}")
    ResponseEntity<ResponseDto> updateCodingQuestion(@PathVariable Long id,@RequestBody CreateCodingQnDto createCodingQnDto);

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseDto> deleteCodingQuestion(@PathVariable Long id);

    @DeleteMapping()
    ResponseEntity<ResponseDto> deleteAllCodingQuestion();
}
