package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.entity.Cases;
import com.divum.hiring_platform.entity.CodingImageUrl;
import com.divum.hiring_platform.entity.FunctionCode;
import com.divum.hiring_platform.entity.StaticCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCodingQnDto {
    private String question;
    private String constraints;
    private java.util.List<CodingImageUrl> imageUrl;
    private String category;
    private List<Cases> casesList;
    private Difficulty difficulty;
    private List<StaticCode> staticCodes;
    private List<FunctionCode> functionCodes;
}
