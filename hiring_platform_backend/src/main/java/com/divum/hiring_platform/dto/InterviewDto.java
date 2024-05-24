package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDto {
    private String name;
    private LocalDateTime interviewTime;
}
