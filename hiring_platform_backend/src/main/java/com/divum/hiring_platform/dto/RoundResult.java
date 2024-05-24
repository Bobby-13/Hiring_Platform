package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoundResult {
    private String roundNumber;
    private String roundRange;
    private Integer passPercentage;
    private Integer participantsCount;
    private Integer passCount;
    private String roundType;
    private Integer totalAllocatedTime;
    private Integer totalNumberOfQuestions;
    private List<PartWiseMarkAllocation> partWiseMarkAllocations;
    private List<InterviewResultDetails> interviewResults;
}
