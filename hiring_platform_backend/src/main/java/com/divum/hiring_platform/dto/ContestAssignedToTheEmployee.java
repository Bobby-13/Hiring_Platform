package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContestAssignedToTheEmployee {

    private String contestId;
    private String contestName;
    private String contestStatus;
    private String contestStartDate;
    private String contestEndDate;
}
