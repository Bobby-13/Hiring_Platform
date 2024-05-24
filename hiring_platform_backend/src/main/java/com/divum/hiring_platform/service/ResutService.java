package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ResutService {
    ResponseEntity<ResponseDto> roundWiseResult(String contestId, String userId);
}
