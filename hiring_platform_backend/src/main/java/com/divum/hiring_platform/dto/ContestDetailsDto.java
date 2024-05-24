package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestDetailsDto {

    private String contestId;
    private String contestName;
    private ContestStatus contestStatus;
    private List<RoundDetailsDto> round;

    @Override
    public String toString() {
        return "Contest{" +
                "contestId='" + contestId + '\'' +
                ", contestName='" + contestName + '\'' +
                ", contestStatus='" + contestStatus + '\'' +
                ", round=" + round +
                '}';
    }
}
