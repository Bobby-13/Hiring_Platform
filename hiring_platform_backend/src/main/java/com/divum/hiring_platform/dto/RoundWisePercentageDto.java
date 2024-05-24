package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.RoundType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoundWisePercentageDto {
    RoundType roundType;
    int roundNum;
    String result;
}
