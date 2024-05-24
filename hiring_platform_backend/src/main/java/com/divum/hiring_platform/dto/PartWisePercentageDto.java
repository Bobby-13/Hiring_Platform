package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartWisePercentageDto {
    private String part;
    private Map<String, Double> difficultyWisePercentage;
}
