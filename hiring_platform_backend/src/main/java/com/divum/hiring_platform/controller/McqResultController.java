package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.McqResultApi;
import com.divum.hiring_platform.dto.PartResponseDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.McqResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class McqResultController implements McqResultApi {

    private final McqResultService mcqResultService;


    @Override
    public ResponseEntity<ResponseDto> partWiseResponseMcq(String userId, String roundId, String status, PartResponseDto partResponseDto) {
        return mcqResultService.partWiseResponseMcq(userId, roundId, partResponseDto, status);

    }
}
