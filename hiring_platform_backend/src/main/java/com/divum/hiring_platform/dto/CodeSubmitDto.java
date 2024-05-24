package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CodeSubmitDto {
    private Long questionId;
    private String code;
    private String language;
    private String version;
    private String input;

}
