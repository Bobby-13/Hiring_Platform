package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.InterviewRequestStatus;
import com.divum.hiring_platform.util.enums.InterviewRequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview_request")
public class InterviewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reason;

    @OneToOne
    private Interview interview;

    private LocalDateTime preferredTime;

    @Enumerated(EnumType.STRING)
    private InterviewRequestType interviewRequestType;

    @Enumerated(EnumType.STRING)
    private InterviewRequestStatus interviewRequestStatus;

    @Override
    public String toString() {
        return "InterviewRequest{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                ", interview=" + interview.getInterviewId() +
                ", preferredTime=" + preferredTime +
                ", interviewRequestType=" + interviewRequestType +
                ", interviewRequestStatus=" + interviewRequestStatus +
                '}';
    }
}
