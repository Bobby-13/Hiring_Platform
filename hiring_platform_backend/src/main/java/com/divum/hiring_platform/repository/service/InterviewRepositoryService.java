package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.dto.InterviewProjection;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Interview;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;

import java.util.List;
import java.util.Optional;

public interface InterviewRepositoryService {
    List<Interview> findInterviewsByRoundsId(String roundId);

    void saveAll(List<Interview> interviews);

    Optional<Interview> findById(String interviewId);

    void save(Interview interview);

    List<Interview> findInterviewsByEmployee(Employee employee);

    int countByRoundsId(String roundId);

    List<InterviewProjection> findInterviewsByEmployeeAndRounds(Employee employeeId, Rounds roundId);

    String getInterviewFeedback(String previousRoundId, String userId);

    List<Interview> getPassedContestants(Rounds round);

    Interview findByEmployeeIdAndUserId(String employeeId, String userId);

    List<Interview> findByRounds(Rounds round);


    List<Interview> findInterviewsByUser(User user);

    List<Interview> findAllByEmployeeAndRounds(Employee employee, Rounds rounds1);

    boolean existsByRoundId(Rounds roundId);
}
