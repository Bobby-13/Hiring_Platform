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
public class IndividualInterviewRequest {

    private ContestDetails contestDetails;
    private EmployeeDetails employeeDetails;
    private List<ScheduleDetails> scheduleDetails;
    private List<InterviewAssignEmployee> employees;
}
