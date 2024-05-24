package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.EmailTask;
import com.divum.hiring_platform.entity.Rounds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailTaskRepository extends JpaRepository<EmailTask, Long> {
    List<EmailTask> findAllByRounds(Rounds existingRound);


    List<EmailTask> findEmailTasksByTaskTimeAfterAndTaskTimeBefore(LocalDateTime startTime, LocalDateTime endTime);

    EmailTask findFirstByRoundsOrderByTaskTimeAsc(Rounds round);
}
