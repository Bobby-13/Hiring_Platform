package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/question")
public interface QuestionApi {
    @GetMapping("/{roundId}/mcq")
    ResponseEntity<ResponseDto> getRoundQuestionMcq(@PathVariable String roundId);

}
