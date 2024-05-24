package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.repository.ContestRepository;
import com.divum.hiring_platform.repository.service.ContestRepositoryService;
import com.divum.hiring_platform.util.enums.ContestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContestRepositoryServiceImpl implements ContestRepositoryService {

    private final ContestRepository contestRepository;

    @Override
    public void save(Contest contest) {
        contestRepository.save(contest);
    }

    @Override
    public Set<Contest> getContestAssignedForEmployee(String employeeId) {
        return contestRepository.getContestAssignedToEmployee(employeeId);
    }
    @Override
    public List<User> findUsersAssignedToTheContest(String contestId) {
        return contestRepository.findUsersAssignedToTheContest(contestId);
    }
    @Override
    public List<User> findPassedStudents(Contest contest) {
        return contestRepository.findPassedStudents(contest);
    }

    @Override
    public List<Contest> findContestsByContestStatus(ContestStatus contestStatus) {
        return contestRepository.findContestsByContestStatus(contestStatus);
    }

    @Override
    public void deleteByContestId(String contestId) {
        contestRepository.deleteByContestId(contestId);
    }

    @Override
    public List<Contest> findAll() {
        return contestRepository.findAll();
    }

    public Optional<List<Contest>> findContestByEmployeeId(String employeeId) {
        return contestRepository.findContestByEmployeeId(employeeId);
    }

    @Override
    public Contest findContestByRounds(Rounds rounds) {
        return contestRepository.findContestByRounds(rounds);
    }

    @Override
    public Optional<Contest> findById(String contestId) {
        return contestRepository.findById(contestId);
    }

    @Override
    public List<Employee> getEmployeeAssignedToTheContest(String contestId) {
        return contestRepository.getAssignedEmployee(contestId);
    }

    @Override
    public Integer countContestByContestStatus(ContestStatus contestStatus) {
        return contestRepository.countContestByContestStatus(contestStatus);
    }
}
