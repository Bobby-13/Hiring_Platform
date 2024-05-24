package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.CodingQnApi;
import com.divum.hiring_platform.dto.CreateCodingQnDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.CodingQnService;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CodingQnController implements CodingQnApi {
    public final CodingQnService codingQnService;
    @Override
    public ResponseEntity<ResponseDto> createCodingQuestion(CreateCodingQnDto createCodingQnDto) throws IOException {
        return codingQnService.createCodingQuestion(createCodingQnDto,null);
    }

    @Override
    public ResponseEntity<ResponseDto> getCodingQuestion(Long  id) {
        return codingQnService.getCodingQuestion(id);
    }

    @Override
    public ResponseEntity<ResponseDto> getCodingQuestions(int page,int size, List<QuestionCategory> category, List<Difficulty> difficulty) {
        return codingQnService.getCodingQuestions(page,size,category,difficulty);
    }

    @Override
    public ResponseEntity<ResponseDto> updateCodingQuestion(Long id, CreateCodingQnDto createCodingQnDto) {
        return codingQnService.updateCodingQuestion(id,createCodingQnDto);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCodingQuestion(Long id) {
        return codingQnService.deleteCodingQuestion(id);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteAllCodingQuestion() {
        return codingQnService.deleteAllCodingQuestion();
    }
}
