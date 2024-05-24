package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignedContestants {

    private String contestantName;
    private String email;
    private String schedule;
    private String resumeUrl;
    private String previousRoundResult;
    private String interviewId;
    private String interviewUrl;
    private boolean isCompleted;
}
