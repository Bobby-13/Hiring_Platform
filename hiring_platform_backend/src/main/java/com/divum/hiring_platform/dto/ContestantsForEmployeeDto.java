package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestantsForEmployeeDto {
    private String contestantName;
    private String email;
    private String date;
    private String duration;
    private String resumeUrl;
    private String codingResultId;
}
