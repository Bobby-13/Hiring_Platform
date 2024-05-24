package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminHomePageResponseDto {
    private Map<String,Integer> contestCount;
    private Map<Integer, Integer> contestCountByYear;
    private Map<String,Integer> assignedHR;
    private List<Notification> notifications;
}
