package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.JobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String jobId;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}

