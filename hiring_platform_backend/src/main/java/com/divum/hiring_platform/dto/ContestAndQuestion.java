package com.divum.hiring_platform.dto;


import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContestAndQuestion {

    private String contestId;
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;
    private List<CodingQuestion> codingQuestions;
}
