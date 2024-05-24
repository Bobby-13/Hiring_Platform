package com.divum.hiring_platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class McqDto {
    private String question;
    private List<OptionDto> options;
    private List<String> imageUrl;
    private String difficulty;
    private String category;
}
