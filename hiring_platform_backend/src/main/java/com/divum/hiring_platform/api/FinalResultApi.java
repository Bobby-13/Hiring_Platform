package com.divum.hiring_platform.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/api/v1/final")
public interface FinalResultApi {
    @RequestMapping("/{contestId}")
    ResponseEntity<Resource> getExcel(@PathVariable String contestId)throws IOException;

}
