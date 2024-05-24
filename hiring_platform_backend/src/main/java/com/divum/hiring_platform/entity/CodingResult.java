package com.divum.hiring_platform.entity;

import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.Result;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "codingResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodingResult implements ResultEntity {

    @Id
    private String id;
    private String contestId;
    private String roundId;
    private String userId;
    private List<CodingQuestionObject> question;
    private float totalPercentage;
    private Map<Difficulty,Integer> percentage;
    private Result result;
}