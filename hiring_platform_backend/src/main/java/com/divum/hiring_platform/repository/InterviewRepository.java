package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.dto.InterviewProjection;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Interview;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.util.enums.InterviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {
    List<Interview> findInterviewsByRoundsId(String id);

    List<Interview> findInterviewsByEmployee(Employee employee);

    int countByRoundsId(String roundId);

    @Query("SELECT i FROM Interview i WHERE i.interviewType = :interviewType AND i.rounds IN :rounds")
    List<Interview> findInterviewsByInterviewTypeAndRounds(
            @Param("interviewType") InterviewType interviewType,
            @Param("rounds") List<Rounds> rounds
    );

    List<InterviewProjection> findInterviewsByEmployeeAndRounds(Employee employee, Rounds rounds);

    Interview findByRoundsAndUser(Rounds rounds, User user);

    Interview findInterviewByRoundsIdAndUserUserId(String roundId, String userId);

    @Query("SELECT i FROM Interview i WHERE i.rounds =?1")
    List<Interview> getPassedContestants(Rounds round);

    Interview findInterviewsByEmployeeEmployeeIdAndUserUserId(Long employeeId, String userId);

    List<Interview> findAllByEmployeeAndRounds(Employee employee, Rounds rounds1);

    List<Interview> findByRounds(Rounds round);

    List<Interview> findInterviewsByUser(User user);

    boolean existsByRounds(Rounds roundId);
}
