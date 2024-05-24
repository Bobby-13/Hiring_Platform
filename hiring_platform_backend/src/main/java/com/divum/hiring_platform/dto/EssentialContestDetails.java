package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.util.enums.ContestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EssentialContestDetails {

    private String contestId;
    private String startDate;
    private String endDate;
    private String name;
    private ContestStatus contestStatus;
}
