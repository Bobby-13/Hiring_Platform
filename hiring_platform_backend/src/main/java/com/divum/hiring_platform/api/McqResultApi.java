package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.PartResponseDto;
import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/result/mcq")
public interface McqResultApi {

    @PostMapping("/{userId}/{roundId}")
    ResponseEntity<ResponseDto> partWiseResponseMcq(@PathVariable String userId, @PathVariable String roundId, @RequestParam("status") String status, @RequestBody PartResponseDto partWiseResponseDtoS);
}
