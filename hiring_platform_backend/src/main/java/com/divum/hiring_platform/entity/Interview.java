package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.InterviewResult;
import com.divum.hiring_platform.util.enums.InterviewType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "interview")
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String interviewId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties("contest")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonIgnoreProperties("contest")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    private String feedBack;

    @Enumerated(EnumType.STRING)
    private InterviewResult interviewResult;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "roundsId")
    private Rounds rounds;

    private LocalDateTime interviewTime;

    @Override
    public String toString() {
        return "Interview{" +
                "interviewId='" + interviewId + '\'' +
                ", user=" + (user != null ? user.getUserId() : null) +
                ", employee=" + (employee != null ? employee.getEmployeeId() : null) +
                ", interviewType=" + interviewType +
                ", feedBack='" + feedBack + '\'' +
                ", interviewResult=" + interviewResult +
                ", rounds=" + (rounds != null ? rounds.getId() : null) +
                ", interviewTime=" + interviewTime +
                ", interviewUrl=" + interviewUrl +
                ", eventId=" + eventId +
                '}';
    }

    private String interviewUrl;
    private String eventId;
}

