package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleEventRequestDto {
    private String summary;
    private Date startTime;
    private Date endTime;
    private String description;
    private List<String> attendees;
    private String organizerEmail;
}
