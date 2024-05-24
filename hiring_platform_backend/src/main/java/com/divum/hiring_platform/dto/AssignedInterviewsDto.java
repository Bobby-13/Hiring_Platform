package com.divum.hiring_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignedInterviewsDto {
    private String userName;
    private String userEmail;
    private LocalDateTime interviewTime;
    private String resume;
    private Map<String,String> prevoiusRoundResult;
    private String meetingLink;
    private String interviewId;
    private String feedBack;
    private String status;
}