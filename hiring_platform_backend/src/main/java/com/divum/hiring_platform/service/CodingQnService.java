package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.CreateCodingQnDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CodingQnService {
    ResponseEntity<ResponseDto> createCodingQuestion(CreateCodingQnDto createCodingQnDto, MultipartFile multipartFile) throws IOException;

    ResponseEntity<ResponseDto> getCodingQuestion(Long id);

    ResponseEntity<ResponseDto> updateCodingQuestion(Long id, CreateCodingQnDto createCodingQnDto);

    ResponseEntity<ResponseDto> deleteCodingQuestion(Long id);

    ResponseEntity<ResponseDto> deleteAllCodingQuestion();

    ResponseEntity<ResponseDto> getCodingQuestions(int page, int size, List<QuestionCategory> category, List<Difficulty> difficulty);
}
