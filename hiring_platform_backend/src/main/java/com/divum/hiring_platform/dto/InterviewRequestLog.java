package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewRequestLog {

    private Long id;
    private String round;
    private String contestName;
    private String employeeName;
    private String requestType;
    private String reason;
    private String assignedTime;
    private String preferredTime;
    private String status;
    private String intervieweeName;
}
