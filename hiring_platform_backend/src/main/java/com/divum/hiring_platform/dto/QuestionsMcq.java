package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.McqImageUrl;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionsMcq {
    private String questionId;
    private List<McqImageUrl> imageUrl;
    private String question;
    private List<String> options;
    private String difficulty;
    private String type;
}
