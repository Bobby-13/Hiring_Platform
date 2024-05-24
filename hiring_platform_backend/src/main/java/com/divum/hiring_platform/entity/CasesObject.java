package com.divum.hiring_platform.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasesObject {

    private String testcaseId;
    private String input;
    private String output;
    private String userOutput;

}