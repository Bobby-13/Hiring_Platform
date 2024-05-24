package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.LoginDto;
import com.divum.hiring_platform.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/v1/auth")
public interface  AuthApi {
    @PostMapping("/login")
    ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto);

    @PostMapping("/logout")
    ResponseEntity<ResponseDto> logout();
}