package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.entity.CodingQuestionObject;
import com.divum.hiring_platform.entity.TestCasesObject;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodingResultDTO {
    private String question;
    private String code;
    private int passCount;
    private int testCaseCount;

}
