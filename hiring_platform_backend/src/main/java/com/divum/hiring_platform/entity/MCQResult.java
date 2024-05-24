package com.divum.hiring_platform.entity;

import com.divum.hiring_platform.util.enums.Result;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "mcqResult")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MCQResult implements ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String contestId;
    private String roundId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private Result result;

    private List<PartWiseResponse> savedMcq;
    private List<PartWiseMark> partWiseMarks;
    private float totalPercentage;

    @Override
    public String toString() {
        return "MCQResult{" +
                "id='" + id + '\'' +
                ", contestId='" + contestId + '\'' +
                ", roundId='" + roundId + '\'' +
                ", userId='" + userId + '\'' +
                ", result=" + result +
                ", savedMcq=" + savedMcq +
                ", partWiseMarks=" + partWiseMarks +
                ", totalPercentage=" + totalPercentage +
                '}';
    }
}
