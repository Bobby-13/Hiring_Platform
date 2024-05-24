package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.UserApi;
import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.ResumeUploadRequestDto;
import com.divum.hiring_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<ResponseDto> landingPageCredentials(String userId) {
        return userService.landingPageCredentials(userId);
    }

    @Override
    public ResponseEntity<ResponseDto> passwordReset(String email, Password passwordResetRequestDto) {
        return userService.passwordReset(email, passwordResetRequestDto);
    }

    @Override
    public ResponseEntity<ResponseDto> resumeUpload(String userId, ResumeUploadRequestDto resumeUploadRequestDto) {
        return userService.resumeUpload(userId, resumeUploadRequestDto);
    }
}

