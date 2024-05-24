package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartWiseForEachRoundDto {
    int roundNum;
    List<PartWisePercentageDto> partWisePercentageDto;
}
