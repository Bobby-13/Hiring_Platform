package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.util.enums.RoundType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewRoundDTO {

    private int roundNumber;
    private RoundType interviewType;
}
