package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.QuestionApi;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;


    @Override
    public ResponseEntity<ResponseDto> getRoundQuestionMcq(String roundId) {
        return questionService.getRoundQuestionMcq(roundId);
    }


}
