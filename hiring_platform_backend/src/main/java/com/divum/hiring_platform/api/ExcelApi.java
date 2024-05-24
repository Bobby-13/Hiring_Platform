package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/v1/excel")
public interface ExcelApi {
    @PostMapping("/{uploadType}/{contestId}")
    ResponseEntity<ResponseDto> excelUpload(@RequestPart("file") MultipartFile file, @PathVariable String uploadType, @PathVariable String contestId) throws IOException;
}
