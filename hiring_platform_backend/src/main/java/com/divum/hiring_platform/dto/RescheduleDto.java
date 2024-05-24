package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.InterviewRequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleDto {
    private String reason;
    private LocalDateTime preferredTime;
    private InterviewRequestType interviewRequestType;

}
