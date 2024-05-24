package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.CodingImageUrl;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionsCoding {
    private Long questionId;
    private List<CodingImageUrl> imageUrl;
    private String question;
    private List<SampleCasesDto> sampleCases;
    private String difficulty;
}
