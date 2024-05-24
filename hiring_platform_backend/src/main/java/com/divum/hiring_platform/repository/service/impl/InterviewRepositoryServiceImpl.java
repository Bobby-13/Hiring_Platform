package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.dto.InterviewProjection;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Interview;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.repository.InterviewRepository;
import com.divum.hiring_platform.repository.service.InterviewRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewRepositoryServiceImpl implements InterviewRepositoryService {

    private final InterviewRepository interviewRepository;

    @Override
    public List<Interview> findInterviewsByRoundsId(String roundId) {
        return interviewRepository.findInterviewsByRoundsId(roundId);
    }

    @Override
    public void saveAll(List<Interview> interviews) {
        interviewRepository.saveAll(interviews);
    }

    @Override
    public Interview findByEmployeeIdAndUserId(String employeeId, String userId) {
        return interviewRepository.findInterviewsByEmployeeEmployeeIdAndUserUserId(Long.valueOf(employeeId), userId);
    }

    @Override
    public List<Interview> findByRounds(Rounds round) {
        return interviewRepository.findByRounds(round);
    }

    @Override
    public Optional<Interview> findById(String interviewId) {
        return interviewRepository.findById(interviewId);
    }

    @Override
    public void save(Interview interview) {
        interviewRepository.save(interview);
    }

    @Override
    public List<Interview> findInterviewsByEmployee(Employee employee) {
        return interviewRepository.findInterviewsByEmployee(employee);
    }

    @Override
    public int countByRoundsId(String roundId) {
        return interviewRepository.countByRoundsId(roundId);
    }


    @Override
    public String getInterviewFeedback(String previousRoundId, String userId) {
        return interviewRepository.findInterviewByRoundsIdAndUserUserId(previousRoundId, userId).getFeedBack();
    }

    @Override
    public List<Interview> getPassedContestants(Rounds round) {
        return interviewRepository.getPassedContestants(round);
    }

    @Override
    public List<InterviewProjection> findInterviewsByEmployeeAndRounds(Employee employeeId, Rounds roundId) {
        return interviewRepository.findInterviewsByEmployeeAndRounds(employeeId, roundId);
    }


    @Override
    public List<Interview> findInterviewsByUser(User user) {
        return interviewRepository.findInterviewsByUser(user);
    }

    @Override
    public List<Interview> findAllByEmployeeAndRounds(Employee employee, Rounds rounds1) {
        return interviewRepository.findAllByEmployeeAndRounds(employee,rounds1);
    }


    @Override
    public boolean existsByRoundId(Rounds roundId) {
        return interviewRepository.existsByRounds(roundId);
    }

}
