package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.ResultApi;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.ResutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResultController implements ResultApi {
    private final ResutService resutService;

    @Override
    public ResponseEntity<ResponseDto> roundWiseResult(String contestId, String userId) {
        return resutService.roundWiseResult(contestId, userId);
    }
}
