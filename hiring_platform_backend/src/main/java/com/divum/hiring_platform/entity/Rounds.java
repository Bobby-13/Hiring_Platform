package com.divum.hiring_platform.entity;

import com.divum.hiring_platform.util.enums.RoundType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name = "round")
public class Rounds {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    private int participantsCounts;


    @OneToMany(mappedBy = "rounds", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("rounds")
    private List<Part> parts;

    @OneToMany(mappedBy = "rounds", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EmailTask> emailTasks;


    @OneToMany(mappedBy = "rounds")
    @JsonIgnore
    private List<Interview> interviews;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int roundNumber;
    private int passCount;
    private int passPercentage;

    @Override
    public String toString() {
        return "Rounds{" +
                "id='" + id + '\'' +
                ", roundType=" + roundType +
                ", contest=" + (contest != null ? contest.getContestId() : null) +
                ", participantsCounts=" + participantsCounts +
                ", parts=" + (parts != null ? parts.toString() : null) +
                ", interviews=" + (interviews != null ? interviews.toString() : null) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", roundNumber=" + roundNumber +
                ", passCount=" + passCount +
                ", passPercentage=" + passPercentage +
                '}';
    }

}
