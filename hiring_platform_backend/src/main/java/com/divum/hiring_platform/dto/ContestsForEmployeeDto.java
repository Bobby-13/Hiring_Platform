package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestsForEmployeeDto {
    private String contestId;
    private String name;
    private ContestStatus contestStatus;
    private String startTime;

}
