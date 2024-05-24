package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.Password;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.ResumeUploadRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
public interface UserApi {


    @GetMapping("/{userId}")
    ResponseEntity<ResponseDto> landingPageCredentials(@PathVariable String userId);

    @PostMapping("/password-reset/{email}")
    ResponseEntity<ResponseDto> passwordReset(@PathVariable String email , @RequestBody Password passwordResetRequestDto);

    @PostMapping("/resume-upload/{userId}")
    ResponseEntity<ResponseDto> resumeUpload(@PathVariable String userId, @RequestBody ResumeUploadRequestDto resumeUploadRequestDto);
}


