package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.CodeResultApi;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.CodingResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CodeResultController implements CodeResultApi {

    private final CodingResultService employeeCodingFlowService;

    @Override
    public ResponseEntity<ResponseDto> fetchResult(String codingResultId) {
        return employeeCodingFlowService.fetch(codingResultId);
    }

}
