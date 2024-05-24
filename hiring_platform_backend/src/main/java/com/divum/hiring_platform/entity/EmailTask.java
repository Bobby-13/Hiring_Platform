package com.divum.hiring_platform.entity;


import com.divum.hiring_platform.util.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "email_task")
public class EmailTask {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rounds_id", nullable = false)
    private Rounds rounds;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private LocalDateTime taskTime;

    @Override
    public String toString() {
        return "EmailTask{" +
                "id=" + id +
                ", rounds=" + rounds.getId() +
                ", taskStatus=" + taskStatus +
                ", taskTime=" + taskTime +
                '}';
    }
}
