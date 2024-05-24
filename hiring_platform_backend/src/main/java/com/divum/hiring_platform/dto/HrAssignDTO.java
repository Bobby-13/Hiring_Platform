package com.divum.hiring_platform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HrAssignDTO {

    private String contestName;
    private Integer roundNumber;
    private String status;
    private String roundType;
    private String roundId;
    private String contestDate;
}
