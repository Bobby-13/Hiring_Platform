package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.Employee;
import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.util.enums.ContestStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
    Contest findContestByContestId(String contestId);


    @Query("SELECT u FROM User u JOIN u.contest c WHERE c.contestId = ?1")
    List<User> findUsersAssignedToTheContest(String contestId);


    @Query("SELECT user FROM User user JOIN user.contest c WHERE c = :contest AND user.isPassed = true")
    List<User> findPassedStudents(Contest contest);

    @Query("SELECT e FROM Employee e JOIN e.contest c WHERE c.contestId =?1")
    List<Employee> getAssignedEmployee(String contestId);

    List<Contest> findContestsByContestStatus(ContestStatus contestStatus);
    @Transactional
    @Modifying
    void deleteByContestId(String contestId);

    @Query("SELECT c FROM Contest c JOIN c.employees e WHERE e.employeeId = :employeeId")
    Set<Contest> getContestAssignedToEmployee(String employeeId);

    @Query("SELECT c FROM Contest c JOIN c.employees e WHERE e.employeeId = :employeeId")
    Optional<List<Contest>> findContestByEmployeeId(@Param("employeeId") String employeeId);

    Contest findContestByRounds(Rounds rounds);

    Integer countContestByContestStatus(ContestStatus contestStatus);

}
