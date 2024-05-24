package com.divum.hiring_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartWiseMark {

    public String part;
    public Map<String, Double> difficultyWiseMarks;
    public int correctAnswerCount;

    @Override
    public String toString() {
        return "PartWiseMark{" +
                "part='" + part + '\'' +
                ", difficultyWiseMarks=" + difficultyWiseMarks +
                ", correctAnswerCount=" + correctAnswerCount +
                '}';
    }
}
