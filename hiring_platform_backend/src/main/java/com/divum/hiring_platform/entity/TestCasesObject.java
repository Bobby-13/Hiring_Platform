package com.divum.hiring_platform.entity;

import com.divum.hiring_platform.util.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCasesObject {
    private String id;
    private String input;
    private String output;
    private String expectedOutput;
    private Result result;
}