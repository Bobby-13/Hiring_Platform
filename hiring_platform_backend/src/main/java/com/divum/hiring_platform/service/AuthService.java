package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.LoginDto;
import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseDto> login(LoginDto loginDto);

    ResponseEntity<ResponseDto> logout();
}
