package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDto {
    private String token;
    private String id;
    private String role;
    private String email;
    private String fname;
    private String lname;
}
