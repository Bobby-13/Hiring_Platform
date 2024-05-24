package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeUploadRequestDto {
    private String department;
    private String resumeUrl;
    private int yearOfGraduation;
}
