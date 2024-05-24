package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.ResumeUploadRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<ResponseDto> landingPageCredentials(String userId);

    ResponseEntity<ResponseDto> passwordReset(String email, Password passwordResetRequestDto);

    ResponseEntity<ResponseDto> resumeUpload(String userId, ResumeUploadRequestDto resumeUploadRequestDto);
}
