package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.FinalResultApi;
import com.divum.hiring_platform.service.FinalResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FinalResultController implements FinalResultApi {
    private final FinalResultService finalResultService;


    @Override
    public ResponseEntity<Resource> getExcel(String contestId) throws IOException {
        return finalResultService.getExcel(contestId);
    }




}
