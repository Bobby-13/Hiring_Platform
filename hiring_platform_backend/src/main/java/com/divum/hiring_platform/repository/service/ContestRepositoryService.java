package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.util.enums.ContestStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContestRepositoryService {

    List<User> findUsersAssignedToTheContest(String contestId);

    List<User> findPassedStudents(Contest contest);

    List<Contest> findContestsByContestStatus(ContestStatus contestStatus);

    void deleteByContestId(String contestId);

    List<Contest> findAll();

    void save(Contest contest);

    Set<Contest> getContestAssignedForEmployee(String employeeId);

    Optional<List<Contest>> findContestByEmployeeId(String employeeId);

    Contest findContestByRounds(Rounds rounds);

    Optional<Contest> findById(String contestId);

    List<Employee> getEmployeeAssignedToTheContest(String contestId);

    Integer countContestByContestStatus(ContestStatus contestStatus);

}
