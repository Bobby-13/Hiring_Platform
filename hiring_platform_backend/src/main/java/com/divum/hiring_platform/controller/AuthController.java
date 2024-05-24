package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.AuthApi;
import com.divum.hiring_platform.dto.LoginDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    
    public final AuthService authService;
    @Override
    public ResponseEntity<ResponseDto> login(LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @Override
    public ResponseEntity<ResponseDto> logout() {
        return authService.logout();
    }
}