package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.RoundType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundDetailsDto {
    private String roundId;
    private RoundType roundName;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Override
    public String toString() {
        return "Round{" +
                "roundId='" + roundId + '\'' +
                ", roundName='" + roundName + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
