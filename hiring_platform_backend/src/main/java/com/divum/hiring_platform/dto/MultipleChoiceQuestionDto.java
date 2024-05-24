package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuestionDto {
    private List<String> imageUrls;
    private String question;
    private List<String> options;
    private String difficulty;
    private String category;
}
