package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ExcelService {
    ResponseEntity<ResponseDto> excelUpload(MultipartFile file, String uploadType, String contestId) throws IOException;

}
