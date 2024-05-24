package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.PartResponseDto;
import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface McqResultService {

    ResponseEntity<ResponseDto> partWiseResponseMcq(String userId, String roundId, PartResponseDto partResponseDto, String status);
}
