package com.divum.hiring_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentlyCompletedRoundDTO {

    private String roundId;
    private String contestName;
    private String roundCompletedTime;
    private int RoundNumber;
    private String status;
}
