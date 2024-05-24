package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultChartDto {

    String name;
    String email;
    String college;
    String totalPercentage;
    List<RoundWisePercentageDto> roundWiseDto;
    List<PartWiseForEachRoundDto> partWiseDto;
}
