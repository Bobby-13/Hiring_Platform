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
public class InterviewAssignDTO {

    private String round;
    private String id;
    private String contestName;
    private Integer roundsNumber;
    private List<Contestants> contestants;
    private List<InterviewAssignEmployee> employees;
}
