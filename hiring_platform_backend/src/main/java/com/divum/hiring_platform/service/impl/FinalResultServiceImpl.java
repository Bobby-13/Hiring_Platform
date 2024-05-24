package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.service.FinalResultService;
import com.divum.hiring_platform.util.ContestRelatedService;
import com.divum.hiring_platform.util.FinalResultCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class FinalResultServiceImpl implements FinalResultService {
    private final FinalResultCalculationService finalResultCalculationService;
    private final ContestRelatedService contestRelatedService;

    @Override
    public ResponseEntity<Resource> getExcel(String contestId) throws IOException {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        String contestName = contest.getName();
        String filename = contestName + ".xlsx";
        ByteArrayInputStream inputStream = finalResultCalculationService.getFinalResult(contestId);
        InputStreamResource file = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }


}
