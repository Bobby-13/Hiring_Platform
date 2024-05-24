package com.divum.hiring_platform.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String questionId;
    private List<String> chosenAnswer;
    private String difficulty;
    private Boolean isCorrect;

    @Override
    public String toString() {
        return "UserResponse{" +
                "questionId='" + questionId + '\'' +
                ", chosenAnswer=" + chosenAnswer +
                ", difficulty='" + difficulty + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
