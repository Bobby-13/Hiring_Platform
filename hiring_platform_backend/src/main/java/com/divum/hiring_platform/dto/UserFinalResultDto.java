package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFinalResultDto {
    private String userName;
    private String email;
    private Map<String,String> mcqMark;
    private Map<String,String> codingMark;

}
