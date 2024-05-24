package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetCodingQnDto {
    private Long questionId;
    private String question;
    private QuestionCategory category;
    private Difficulty difficulty;
}
