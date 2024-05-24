package com.divum.hiring_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContestDetails {
    private String contestName;
    private Integer roundNumber;
    private String roundType;
    private String interviewTime;
    private String interviewDate;
}
