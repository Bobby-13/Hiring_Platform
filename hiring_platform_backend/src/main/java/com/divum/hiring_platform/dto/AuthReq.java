package com.divum.hiring_platform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthReq {

    private String username;
    private String password;
    private String client;
}
