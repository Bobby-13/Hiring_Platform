package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

    @Query("SELECT contest FROM User WHERE userId =?1")
    Set<Contest> getParticipatedContest(String userId);


    @Query("SELECT COUNT(*) FROM User WHERE :contest MEMBER OF contest")
    Long countUsersByContest(@Param("contest") Contest contest);


    User findUserByUserId(String userId);
}
